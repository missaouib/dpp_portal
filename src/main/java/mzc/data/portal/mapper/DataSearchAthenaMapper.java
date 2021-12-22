package mzc.data.portal.mapper;
import mzc.data.portal.dto.DataSearchAthenaData;
import mzc.data.portal.dto.QueryData;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface DataSearchAthenaMapper {

    List<DataSearchAthenaData> findDataSearchInfo(long idx);
    void addDataSearch(DataSearchAthenaData.AddDataSearchParam param);
    void deleteDataSearch(DataSearchAthenaData.DeleteDataSearchParam param);
    void addQueryHistoryLog(QueryData.QueryDataParam param);
    List<QueryData> findQueryLog(long idx);
}
