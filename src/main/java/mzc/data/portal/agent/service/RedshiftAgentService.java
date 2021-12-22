package mzc.data.portal.agent.service;

import mzc.data.portal.agent.dto.DatabaseData;
import mzc.data.portal.agent.dto.SchemaData;
import mzc.data.portal.agent.dto.TableData;
import mzc.data.portal.dto.QueryRedshiftData;

import java.util.List;

public interface RedshiftAgentService {

    List<DatabaseData> databases();

    List<SchemaData> schemas(String databaseName);

    List<TableData> tables(String databaseName, String schema);

    TableData table(TableData.GetTableParam param);

    //TableData table(TableData.GetTableParam param);

    QueryRedshiftData.MetaSimpleQueryData simpleQuery(QueryRedshiftData.QueryDataParam param);

    QueryRedshiftData.MetaSimpleQueryData simplePreviewQuery(QueryRedshiftData.QueryDataPreviewParam param);

}
