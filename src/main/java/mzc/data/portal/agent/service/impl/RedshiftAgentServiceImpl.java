package mzc.data.portal.agent.service.impl;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import mzc.data.portal.agent.dto.DatabaseData;
import mzc.data.portal.agent.dto.SchemaData;
import mzc.data.portal.agent.dto.TableColumnData;
import mzc.data.portal.agent.dto.TableData;
import mzc.data.portal.agent.service.RedshiftAgentService;
import mzc.data.portal.dto.QueryRedshiftData;
import mzc.data.portal.mapper.DataSearchRedshiftMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.athena.model.Column;
import software.amazon.awssdk.services.athena.model.TableMetadata;
import software.amazon.awssdk.services.redshiftdata.RedshiftDataClient;
import software.amazon.awssdk.services.redshiftdata.model.*;
import software.amazon.awssdk.services.redshiftdata.model.DescribeStatementRequest;
import software.amazon.awssdk.services.redshiftdata.model.ListDatabasesRequest;
import software.amazon.awssdk.services.redshiftdata.model.ListDatabasesResponse;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class RedshiftAgentServiceImpl implements RedshiftAgentService {

    private DataSearchRedshiftMapper dataSearchRedshiftMapper;

    /**
     * 생성자 주입
     * @param dataSearchRedshiftMapper
     */
    @Autowired
    public RedshiftAgentServiceImpl (DataSearchRedshiftMapper dataSearchRedshiftMapper){
        this.dataSearchRedshiftMapper = dataSearchRedshiftMapper;
    }


    private static final String redshiftClusterId = "bumblebee";
    private static final String redshiftDbUser = "bumblebee";
    private static final String redshiftDatabase = "prd";

    private RedshiftDataClient createRedshiftDataClient() {
        return RedshiftDataClient.builder().region(Region.AP_NORTHEAST_2).build();
    }

    @Override
    public List<DatabaseData> databases() {
        try (RedshiftDataClient redshiftDataClient = createRedshiftDataClient()) {
            ListDatabasesRequest request = ListDatabasesRequest.builder()
                    .clusterIdentifier(redshiftClusterId)
                    .dbUser(redshiftDbUser)
                    .database(redshiftDatabase)
                    .build();

            ListDatabasesResponse response = redshiftDataClient.listDatabases(request);

            List<DatabaseData> databases = new ArrayList<>();

            for (String database : response.databases()) {
                databases.add(convertDatabase(database));
            }

            return databases;
        }
    }


    @Override
    public List<SchemaData> schemas(String database) {

        try (RedshiftDataClient redshiftDataClient = createRedshiftDataClient()) {
            ListSchemasRequest request = ListSchemasRequest.builder()
                    .clusterIdentifier(redshiftClusterId)
                    .dbUser(redshiftDbUser)
                    .database(database)
                    .build();

            ListSchemasResponse response = redshiftDataClient.listSchemas(request);

            List<SchemaData> schemas = new ArrayList<>();

            for (String schema : response.schemas()) {
                schemas.add(convertSchema(schema));
            }

            return schemas;
        }
    }

    public List<TableData> tables(String database, String schema) {

        try (RedshiftDataClient redshiftDataClient = createRedshiftDataClient()) {
            ListTablesRequest request = ListTablesRequest.builder()
                    .clusterIdentifier(redshiftClusterId)
                    .dbUser(redshiftDbUser)
                    .database(database)
                    .schemaPattern(schema)
                    .build();

            ListTablesResponse response = redshiftDataClient.listTables(request);

            List<TableData> tables = new ArrayList<>();

            for (TableMember table : response.tables()) {
                tables.add(convertTable(table));
            }

            return tables;
        }
    }

    public TableData table(TableData.GetTableParam param) {
        try (RedshiftDataClient redshiftDataClient = createRedshiftDataClient()) {
            DescribeTableRequest request = DescribeTableRequest.builder()
                    .clusterIdentifier(redshiftClusterId)
                    .dbUser(redshiftDbUser)
                    .database(param.getDatabase())
                    .schema(param.getSchema())
                    .table(param.getTable())
                    .build();


            //TableData table = new TableData();
            DescribeTableResponse response = redshiftDataClient.describeTable(request);

            TableData table = convertTableMeta(response);

            return table;
        }
    }

    public QueryRedshiftData.MetaSimpleQueryData simpleQuery(QueryRedshiftData.QueryDataParam param) {

        //String queryString = "select * from dev2 limit 100";
        String queryString = param.getDataSearchQuery();

        List<LinkedHashMap<String, Object>> queryResults = new ArrayList<>();
        List<LinkedHashMap<String, Object>> columnResults = new ArrayList<>();

        QueryRedshiftData.MetaSimpleQueryData redshiftSimpleQueryResult = null;

        try (RedshiftDataClient redshiftDataClient = createRedshiftDataClient()) {

            dataSearchRedshiftMapper.addQueryHistoryLog(param);

            String statementId = submitRedshiftQuery(redshiftDataClient, queryString);

            checkStatement(redshiftDataClient, statementId);

            GetStatementResultRequest resultRequest = GetStatementResultRequest.builder()
                    .id(statementId)
                    .build();

            GetStatementResultResponse response = redshiftDataClient.getStatementResult(resultRequest);

            List<ColumnMetadata> columnInfoList = new ArrayList<>();
            List<List<Field>> results = new ArrayList<>();

            columnInfoList = response.columnMetadata();
            results = response.records();

            //Column
            for (int k = 0; k < columnInfoList.size(); k++) {
                LinkedHashMap<String, Object> columnResult = Maps.newLinkedHashMap();
                columnResult.put("text", columnInfoList.get(k).name());
                columnResult.put("value", columnInfoList.get(k).name());
                columnResults.add(columnResult);
            }

            //Row
            for (int i = 0; i < results.size(); i++) {
                LinkedHashMap<String, Object> queryResult = Maps.newLinkedHashMap();
                for(int j = 0; j < columnInfoList.size(); j++) {
                    queryResult.put(columnInfoList.get(j).name(), results.get(i).get(j).stringValue());
                }
                queryResults.add(queryResult);
            }

            redshiftSimpleQueryResult = redshiftSimpleQueryResult.builder().columnResults(columnResults).queryResults(queryResults).build();

            return redshiftSimpleQueryResult;

        } catch (RedshiftDataException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }


    }


    public QueryRedshiftData.MetaSimpleQueryData simplePreviewQuery(QueryRedshiftData.QueryDataPreviewParam param) {

        //String queryString = "select * from dev2 limit 100";
        String queryString = param.getDataSearchQuery();

        List<LinkedHashMap<String, Object>> queryResults = new ArrayList<>();
        List<LinkedHashMap<String, Object>> columnResults = new ArrayList<>();

        QueryRedshiftData.MetaSimpleQueryData redshiftSimpleQueryResult = null;

        try (RedshiftDataClient redshiftDataClient = createRedshiftDataClient()) {


            String statementId = submitRedshiftQuery(redshiftDataClient, queryString);

            checkStatement(redshiftDataClient, statementId);

            GetStatementResultRequest resultRequest = GetStatementResultRequest.builder()
                    .id(statementId)
                    .build();

            GetStatementResultResponse response = redshiftDataClient.getStatementResult(resultRequest);

            List<ColumnMetadata> columnInfoList = new ArrayList<>();
            List<List<Field>> results = new ArrayList<>();

            columnInfoList = response.columnMetadata();
            results = response.records();

            //Column
            for (int k = 0; k < columnInfoList.size(); k++) {
                LinkedHashMap<String, Object> columnResult = Maps.newLinkedHashMap();
                columnResult.put("text", columnInfoList.get(k).name());
                columnResult.put("value", columnInfoList.get(k).name());
                columnResults.add(columnResult);
            }

            //Row
            for (int i = 0; i < results.size(); i++) {
                LinkedHashMap<String, Object> queryResult = Maps.newLinkedHashMap();
                for(int j = 0; j < columnInfoList.size(); j++) {
                    queryResult.put(columnInfoList.get(j).name(), results.get(i).get(j).stringValue());
                }
                queryResults.add(queryResult);
            }

            redshiftSimpleQueryResult = redshiftSimpleQueryResult.builder().columnResults(columnResults).queryResults(queryResults).build();

            return redshiftSimpleQueryResult;

        } catch (RedshiftDataException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }


    }



    public static void checkStatement(RedshiftDataClient redshiftDataClient,String sqlId ) {

        try {

            DescribeStatementRequest statementRequest = DescribeStatementRequest.builder()
                    .id(sqlId)
                    .build() ;

            // Wait until the sql statement processing is finished.
            boolean finished = false;
            String status = "";
            int i = 0;
            while (!finished) {

                DescribeStatementResponse response = redshiftDataClient.describeStatement(statementRequest);
                status = response.statusAsString();
                //System.out.println("..."+status);
                if (status.compareTo("FINISHED") == 0) {
                    break;
                }
                // 횟수 제한
                if(i > 30) {
                    break;
                }
                i++;
                Thread.sleep(1000);
            }

            System.out.println("The statement is finished!");

        } catch (RedshiftDataException | InterruptedException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    private static String submitRedshiftQuery(RedshiftDataClient redshiftDataClient, String queryString) {
        try {
            ExecuteStatementRequest statementRequest = ExecuteStatementRequest.builder()
                    .clusterIdentifier(redshiftClusterId)
                    .database(redshiftDatabase)
                    .dbUser(redshiftDbUser)
                    .sql(queryString)
                    .build();

            ExecuteStatementResponse response = redshiftDataClient.executeStatement(statementRequest);
            return response.id();
        } catch (RedshiftDataException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    private DatabaseData convertDatabase(String database) {
        DatabaseData databaseData = new DatabaseData();

        databaseData.setDatabaseName(database);

        return databaseData;
    }

    private SchemaData convertSchema(String schema) {
        SchemaData schemaData = new SchemaData();

        schemaData.setSchemaName(schema);

        return schemaData;
    }

    private TableData convertTable(TableMember tableMember) {
        TableData tableData = new TableData();

        tableData.setTableName(tableMember.name());
        //tableData.setCreatedAt(tableMetadata.createTime());

        return tableData;
    }

    private TableData convertTableMeta(DescribeTableResponse tableResponse) {
        TableData tableData = new TableData();

        tableData.setTableName(tableResponse.tableName());
/*
        Map<String, String> parameter = tableMetadata.parameters();
        long recordCount = Long.parseLong(parameter.get("recordCount"));
        String location = parameter.get("location");
        String typeOfData = parameter.get("typeOfData");

        tableData.setRecordCount(recordCount);
        tableData.setLocation(location);
        tableData.setTypeOfData(typeOfData);
*/

        List<ColumnMetadata> columns = tableResponse.columnList();
        List<TableColumnData> tableColumns = new ArrayList<>();

        for (ColumnMetadata column : columns) {
            TableColumnData tableColumnData = new TableColumnData();
            tableColumnData.setColumn(column.name());
            tableColumnData.setType(column.typeName());
            tableColumnData.setLength(column.length());

            tableColumns.add(tableColumnData);
        }

        tableData.setColumns(tableColumns);

        return tableData;
    }

}
