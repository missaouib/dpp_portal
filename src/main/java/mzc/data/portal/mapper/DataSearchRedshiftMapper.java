package mzc.data.portal.mapper;

import mzc.data.portal.dto.DataSearchRedshiftData;
import mzc.data.portal.dto.QueryData;
import mzc.data.portal.dto.QueryRedshiftData;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataSearchRedshiftMapper {

    List<DataSearchRedshiftData> findDataSearchInfo(long idx);
    void addDataSearch(DataSearchRedshiftData.AddDataSearchParam param);
    void deleteDataSearch(DataSearchRedshiftData.DeleteDataSearchParam param);
    void addQueryHistoryLog(QueryRedshiftData.QueryDataParam param);//query 로그 저장 로직
    List<QueryRedshiftData> findQueryLog(long idx);

    }
