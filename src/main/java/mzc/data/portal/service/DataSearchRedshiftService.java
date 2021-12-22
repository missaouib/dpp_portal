package mzc.data.portal.service;

import mzc.data.portal.dto.DataSearchRedshiftData;
import mzc.data.portal.dto.QueryData;
import mzc.data.portal.dto.QueryRedshiftData;

import java.util.List;

public interface DataSearchRedshiftService {

    List<DataSearchRedshiftData> dataSearchRedshift();

    List<QueryRedshiftData> queryLog(); //Query history log 가져오기

    void addDataSearch(DataSearchRedshiftData.AddDataSearchParam param);

    void deleteDataSearch(DataSearchRedshiftData.DeleteDataSearchParam param);
}


