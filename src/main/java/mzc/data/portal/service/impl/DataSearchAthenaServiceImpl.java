package mzc.data.portal.service.impl;

import lombok.extern.slf4j.Slf4j;
import mzc.data.portal.dto.DataSearchAthenaData;
import mzc.data.portal.dto.QueryData;
import mzc.data.portal.dto.UserData;
import mzc.data.portal.enums.DataSearchDeleteYN;
import mzc.data.portal.mapper.DataSearchAthenaMapper;
import mzc.data.portal.service.DataSearchAthenaService;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@ComponentScan
public class DataSearchAthenaServiceImpl extends BaseServiceImpl implements DataSearchAthenaService {

    private DataSearchAthenaMapper dataMapper;

    /**
     * 생성자 주입
     * @param dataMapper
     */
    @Autowired
    public DataSearchAthenaServiceImpl(DataSearchAthenaMapper dataMapper){
        this.dataMapper = dataMapper;
    }


    @Override
    public List<DataSearchAthenaData> dataSearch() {
        List<DataSearchAthenaData> dataSearchData = new ArrayList<>();

        try {

            UserData userData = getUser();
            long userIdx = userData.getIdx();

            dataSearchData = dataMapper.findDataSearchInfo(userIdx);

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("시스템 오류입니다.관리자에게 문의하세요.");
        } return dataSearchData;
    }

    @Override
    public List<QueryData> queryLog() {
        List<QueryData> queryLog = new ArrayList<>();

        try {

            UserData userData = getUser();
            long userIdx = userData.getIdx();

            queryLog = dataMapper.findQueryLog(userIdx);

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("시스템 오류입니다.관리자에게 문의하세요.");
        } return queryLog;
    }

    @Override
    public void addDataSearch(DataSearchAthenaData.AddDataSearchParam param) {
        Assert.notNull(param, "AddDataSearchParam must be not null");

        try {
            param.setDataSearchDeleteYN(DataSearchDeleteYN.N);
            dataMapper.addDataSearch(param);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("시스템 오류입니다.관리자에게 문의하세요.");
        }
    }

    @Override
    public void deleteDataSearch(DataSearchAthenaData.DeleteDataSearchParam param) {
        Assert.notNull(param, "DeleteDataSearchParam must be not null");

        try {
            param.setDataSearchDeleteYN(DataSearchDeleteYN.Y);
            dataMapper.deleteDataSearch(param);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("시스템 오류입니다.관리자에게 문의하세요.");
        }
    }
}
