package mzc.data.portal.service.impl;

import lombok.extern.slf4j.Slf4j;
import mzc.data.portal.dto.DataSearchRedshiftData;
import mzc.data.portal.dto.QueryRedshiftData;
import mzc.data.portal.dto.UserData;
import mzc.data.portal.enums.DataSearchDeleteYN;
import mzc.data.portal.mapper.DataSearchRedshiftMapper;
import mzc.data.portal.service.DataSearchRedshiftService;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@ComponentScan
public class DataSearchRedshiftServiceImpl extends BaseServiceImpl implements DataSearchRedshiftService {

    private DataSearchRedshiftMapper dataSearchRedshiftMapper;

    /**
     * 생성자 주입
     * @param dataSearchRedshiftMapper
     */
    @Autowired
    public DataSearchRedshiftServiceImpl(DataSearchRedshiftMapper dataSearchRedshiftMapper){
        this.dataSearchRedshiftMapper = dataSearchRedshiftMapper;
    }

    @Override
    public List<DataSearchRedshiftData> dataSearchRedshift() {
        List<DataSearchRedshiftData> dataSearchRedshiftData = new ArrayList<>();

        try {

            UserData userData = getUser();
            long userIdx = userData.getIdx();

            dataSearchRedshiftData = dataSearchRedshiftMapper.findDataSearchInfo(userIdx);

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("시스템 오류입니다.관리자에게 문의하세요.");
        } return dataSearchRedshiftData;
    }

    @Override
    public List<QueryRedshiftData> queryLog() {
        List<QueryRedshiftData> queryLog = new ArrayList<>();

        try {

            UserData userData = getUser();
            long userIdx = userData.getIdx();

            queryLog = dataSearchRedshiftMapper.findQueryLog(userIdx);

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("시스템 오류입니다.관리자에게 문의하세요.");
        } return queryLog;
    }

    @Override
    public void addDataSearch(DataSearchRedshiftData.AddDataSearchParam param) {
        Assert.notNull(param, "AddDataSearchParam must be not null");

        try {
            param.setDataSearchDeleteYN(DataSearchDeleteYN.N);
            dataSearchRedshiftMapper.addDataSearch(param);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("시스템 오류입니다.관리자에게 문의하세요.");
        }
    }

    @Override
    public void deleteDataSearch(DataSearchRedshiftData.DeleteDataSearchParam param) {
        Assert.notNull(param, "DeleteDataSearchParam must be not null");

        try {
            param.setDataSearchDeleteYN(DataSearchDeleteYN.Y);
            dataSearchRedshiftMapper.deleteDataSearch(param);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("시스템 오류입니다.관리자에게 문의하세요.");
        }
    }


}
