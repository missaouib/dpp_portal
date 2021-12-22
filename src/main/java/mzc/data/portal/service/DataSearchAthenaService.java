package mzc.data.portal.service;

import mzc.data.portal.dto.DataSearchAthenaData;
import mzc.data.portal.dto.QueryData;

import java.util.List;

public interface DataSearchAthenaService {

    List<DataSearchAthenaData> dataSearch(); //Query 정보 가져오기

    List<QueryData> queryLog(); //Query history log 가져오기

    void addDataSearch(DataSearchAthenaData.AddDataSearchParam param);

    void deleteDataSearch(DataSearchAthenaData.DeleteDataSearchParam param);
}
