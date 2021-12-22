package mzc.data.portal.agent.service;

import mzc.data.portal.agent.dto.DatabaseData;
import mzc.data.portal.agent.dto.TableData;
import mzc.data.portal.dto.QueryData;

import java.util.List;

public interface AthenaAgentService {
    List<DatabaseData> databases();

    List<TableData> tables(String databaseName);

    TableData table(TableData.GetTableParam param);

    QueryData.MetaSimpleQueryData simpleQuery(QueryData.QueryDataParam param);

    QueryData.MetaSimpleQueryData simplePreviewQuery(QueryData.QueryDataPreviewParam param);

}
