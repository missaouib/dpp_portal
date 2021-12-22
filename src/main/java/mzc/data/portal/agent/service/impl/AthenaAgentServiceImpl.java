package mzc.data.portal.agent.service.impl;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import mzc.data.portal.agent.dto.DatabaseData;
import mzc.data.portal.agent.dto.TableData;
import mzc.data.portal.agent.dto.TableColumnData;
import mzc.data.portal.agent.service.AthenaAgentService;
import mzc.data.portal.dto.QueryData;
import mzc.data.portal.mapper.DataSearchAthenaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.athena.AthenaClient;
import software.amazon.awssdk.services.athena.model.*;
import software.amazon.awssdk.services.athena.paginators.GetQueryResultsIterable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class AthenaAgentServiceImpl implements AthenaAgentService {

    private DataSearchAthenaMapper dataMapper;

    /**
     * 생성자 주입
     * @param dataMapper
     */
    @Autowired
    public AthenaAgentServiceImpl (DataSearchAthenaMapper dataMapper){
        this.dataMapper = dataMapper;
    }


    private static final String athenaDataBase = "bumblebee";
    private static final String athenaOutputBucket = "s3://mzc-dsc-dpp";

    private AthenaClient createAthenaClient() {
        return AthenaClient.builder().region(Region.AP_NORTHEAST_2).build();
    }

    @Override
    public List<DatabaseData> databases() {
        try (AthenaClient athenaClient = createAthenaClient()) {
            ListDatabasesRequest request = ListDatabasesRequest.builder().catalogName("AwsDataCatalog").build();

            ListDatabasesResponse response = athenaClient.listDatabases(request);

            List<DatabaseData> databases = new ArrayList<>();

            for (Database database : response.databaseList()) {
                databases.add(convertDatabase(database));
            }

            return databases;
        }
    }

    @Override
    public List<TableData> tables(String databaseName) {
        try (AthenaClient athenaClient = createAthenaClient()) {
            ListTableMetadataRequest request = ListTableMetadataRequest.builder().catalogName("AwsDataCatalog").databaseName(databaseName).build();
            ListTableMetadataResponse response = athenaClient.listTableMetadata(request);

            List<TableData> tables = new ArrayList<>();

            for (TableMetadata tableMetadata : response.tableMetadataList()) {
                tables.add(convertTable(tableMetadata));
            }

            return tables;
        }
    }


    @Override
    public TableData table(TableData.GetTableParam param) {
        String databaseName = param.getDatabase();
        String tableName = param.getTable();

        try (AthenaClient athenaClient = createAthenaClient()) {
            GetTableMetadataRequest request = GetTableMetadataRequest.builder().catalogName("AwsDataCatalog")
                    .databaseName(databaseName).tableName(tableName).build();

            GetTableMetadataResponse response = athenaClient.getTableMetadata(request);


            TableData table = convertTableMeta(response.tableMetadata());

            return table;
        }
    }

    @Override
    public QueryData.MetaSimpleQueryData simpleQuery(QueryData.QueryDataParam param) {
        //String queryString = "select * from bumblebee.aa_plain";
        String queryString = param.getDataSearchQuery();

        /*UserData userData = getUser();
        long userIdx = userData.getIdx();

        param.setUserIdx(userIdx);*/

        List<LinkedHashMap<String, Object>> queryResults = new ArrayList<>();
        List<LinkedHashMap<String, Object>> columnResults = new ArrayList<>();

        QueryData.MetaSimpleQueryData athenaSimpleQueryResult = null;
        String queryExecutionId = null;

        try (AthenaClient athenaClient = createAthenaClient()) {

            dataMapper.addQueryHistoryLog(param);

            queryExecutionId = submitAthenaQuery(athenaClient, queryString);

            waitForQueryToComplete(athenaClient, queryExecutionId);

            GetQueryResultsRequest getQueryResultsRequest = GetQueryResultsRequest.builder()
                    .queryExecutionId(queryExecutionId).build();

            GetQueryResultsIterable getQueryResultsResults = athenaClient.getQueryResultsPaginator(getQueryResultsRequest);

            List<ColumnInfo> columnInfoList = new ArrayList<>();
            List<Row> results = new ArrayList<>();

            for (GetQueryResultsResponse result : getQueryResultsResults) {
                columnInfoList = result.resultSet().resultSetMetadata().columnInfo();
                results = result.resultSet().rows();
            }

            //Column
            for (int k = 0; k < columnInfoList.size(); k++) {
                LinkedHashMap<String, Object> columnResult = Maps.newLinkedHashMap();
                columnResult.put("text", columnInfoList.get(k).name());
                columnResult.put("value", columnInfoList.get(k).name());
                columnResults.add(columnResult);

            }

            //Row
            for (int i = 1; i < results.size(); i++) {
                LinkedHashMap<String, Object> queryResult = Maps.newLinkedHashMap();
                List<Datum> allData = results.get(i).data();
                for (int j = 0; j < allData.size(); j++) {
                    queryResult.put(results.get(0).data().get(j).varCharValue(),allData.get(j).varCharValue());
                }
                queryResults.add(queryResult);
            }

            athenaSimpleQueryResult = athenaSimpleQueryResult.builder().columnResults(columnResults).queryResults(queryResults).build();

            return athenaSimpleQueryResult;


        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("error");
            // log.error(e.getMessage());
        }
    }


    @Override
    public QueryData.MetaSimpleQueryData simplePreviewQuery(QueryData.QueryDataPreviewParam param) {
        //String queryString = "select * from bumblebee.aa_plain";
        String queryString = param.getDataSearchQuery();

        /*UserData userData = getUser();
        long userIdx = userData.getIdx();

        param.setUserIdx(userIdx);*/

        List<LinkedHashMap<String, Object>> queryResults = new ArrayList<>();
        List<LinkedHashMap<String, Object>> columnResults = new ArrayList<>();

        QueryData.MetaSimpleQueryData athenaSimpleQueryResult = null;
        String queryExecutionId = null;

        try (AthenaClient athenaClient = createAthenaClient()) {

            queryExecutionId = submitAthenaQuery(athenaClient, queryString);

            waitForQueryToComplete(athenaClient, queryExecutionId);

            GetQueryResultsRequest getQueryResultsRequest = GetQueryResultsRequest.builder()
                    .queryExecutionId(queryExecutionId).build();

            GetQueryResultsIterable getQueryResultsResults = athenaClient.getQueryResultsPaginator(getQueryResultsRequest);

            List<ColumnInfo> columnInfoList = new ArrayList<>();
            List<Row> results = new ArrayList<>();

            for (GetQueryResultsResponse result : getQueryResultsResults) {
                columnInfoList = result.resultSet().resultSetMetadata().columnInfo();
                results = result.resultSet().rows();
            }

            //Column
            for (int k = 0; k < columnInfoList.size(); k++) {
                LinkedHashMap<String, Object> columnResult = Maps.newLinkedHashMap();
                columnResult.put("text", columnInfoList.get(k).name());
                columnResult.put("value", columnInfoList.get(k).name());
                columnResults.add(columnResult);

            }

            //Row
            for (int i = 1; i < results.size(); i++) {
                LinkedHashMap<String, Object> queryResult = Maps.newLinkedHashMap();
                List<Datum> allData = results.get(i).data();
                for (int j = 0; j < allData.size(); j++) {
                    queryResult.put(results.get(0).data().get(j).varCharValue(),allData.get(j).varCharValue());
                }
                queryResults.add(queryResult);
            }

            athenaSimpleQueryResult = athenaSimpleQueryResult.builder().columnResults(columnResults).queryResults(queryResults).build();

            return athenaSimpleQueryResult;


        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("error");
            // log.error(e.getMessage());
        }
    }


    private static String submitAthenaQuery(AthenaClient athenaClient, String queryString) {

        try {

            // The QueryExecutionContext allows us to set the Database.
            QueryExecutionContext queryExecutionContext = QueryExecutionContext.builder()
                    .database(athenaDataBase).build();

            // The result configuration specifies where the results of the query should go in S3 and encryption options
            ResultConfiguration resultConfiguration = ResultConfiguration.builder()
                    // You can provide encryption options for the output that is written.
                    // .withEncryptionConfiguration(encryptionConfiguration)
                    .outputLocation(athenaOutputBucket).build();

            // Create the StartQueryExecutionRequest to send to Athena which will start the query.
            StartQueryExecutionRequest startQueryExecutionRequest = StartQueryExecutionRequest.builder()
                    .queryString(queryString)
                    .queryExecutionContext(queryExecutionContext)
                    .resultConfiguration(resultConfiguration).build();

            StartQueryExecutionResponse startQueryExecutionResponse = athenaClient.startQueryExecution(startQueryExecutionRequest);
            return startQueryExecutionResponse.queryExecutionId();

        } catch (AthenaException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    private static void waitForQueryToComplete(AthenaClient athenaClient, String queryExecutionId) throws InterruptedException {
        GetQueryExecutionRequest getQueryExecutionRequest = GetQueryExecutionRequest.builder()
                .queryExecutionId(queryExecutionId).build();

        GetQueryExecutionResponse getQueryExecutionResponse;
        boolean isQueryStillRunning = true;
        while (isQueryStillRunning) {
            getQueryExecutionResponse = athenaClient.getQueryExecution(getQueryExecutionRequest);
            String queryState = getQueryExecutionResponse.queryExecution().status().state().toString();
            if (queryState.equals(QueryExecutionState.FAILED.toString())) {
                throw new RuntimeException("Query Failed to run with Error Message: " + getQueryExecutionResponse
                        .queryExecution().status().stateChangeReason());
            } else if (queryState.equals(QueryExecutionState.CANCELLED.toString())) {
                throw new RuntimeException("Query was cancelled.");
            } else if (queryState.equals(QueryExecutionState.SUCCEEDED.toString())) {
                isQueryStillRunning = false;
            } else {
                // Sleep an amount of time before retrying again.
                Thread.sleep(1000);
            }
            System.out.println("Current Status is: " + queryState);
        }
    }

    private DatabaseData convertDatabase(Database database) {
        DatabaseData databaseData = new DatabaseData();

        databaseData.setDatabaseName(database.name());

        return databaseData;
    }

    private TableData convertTable(TableMetadata tableMetadata) {
        TableData tableData = new TableData();

        tableData.setTableName(tableMetadata.name());
        tableData.setCreatedAt(tableMetadata.createTime());

        return tableData;
    }

    private TableData convertTableMeta(TableMetadata tableMetadata) {
        TableData tableData = new TableData();

        tableData.setTableName(tableMetadata.name());
        tableData.setCreatedAt(tableMetadata.createTime());
        tableData.setType(tableMetadata.tableType());

        Map<String, String> parameter = tableMetadata.parameters();
        long recordCount = Long.parseLong(parameter.get("recordCount"));
        String location = parameter.get("location");
        String typeOfData = parameter.get("typeOfData");

        tableData.setRecordCount(recordCount);
        tableData.setLocation(location);
        tableData.setTypeOfData(typeOfData);

        List<Column> columns = tableMetadata.columns();
        List<TableColumnData> tableColumns = new ArrayList<>();

        for (Column column : columns) {
            TableColumnData tableColumnData = new TableColumnData();
            tableColumnData.setColumn(column.name());
            tableColumnData.setType(column.type());

            tableColumns.add(tableColumnData);
        }

        tableData.setColumns(tableColumns);

        return tableData;
    }
}
