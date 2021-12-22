package mzc.data.portal.service.impl;

import lombok.extern.slf4j.Slf4j;
import mzc.data.portal.dto.CloudSandboxInstanceData;
import mzc.data.portal.dto.SandboxData;
import mzc.data.portal.dto.SandboxModelData;
import mzc.data.portal.dto.UserData;
import mzc.data.portal.enums.CloudSandboxInstanceStatus;
import mzc.data.portal.enums.SandboxStatus;
import mzc.data.portal.enums.SandboxType;
import mzc.data.portal.enums.UserGrade;
import mzc.data.portal.mapper.SandboxMapper;
import mzc.data.portal.mapper.UserMapper;
import mzc.data.portal.service.*;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.thymeleaf.util.StringUtils.concat;
import static org.thymeleaf.util.StringUtils.length;

@Slf4j
@Service
@ComponentScan
public class SandboxServiceImpl extends BaseServiceImpl implements SandboxService {

    private SandboxMapper sandboxMapper;
    private CloudSandboxInstanceService cloudSandboxInstanceService;
    private SandboxModelService sandboxModelService;

    /**
     * 생성자 주입
     * @param sandboxMapper
     * @param cloudSandboxInstanceService
     * @param sandboxModelService
     */
    @Autowired
    public SandboxServiceImpl(SandboxMapper sandboxMapper, @Lazy CloudSandboxInstanceService cloudSandboxInstanceService, SandboxModelService sandboxModelService){
        this.sandboxMapper = sandboxMapper;
        this.cloudSandboxInstanceService = cloudSandboxInstanceService; // 순환참조가 되어 있음.
        this.sandboxModelService = sandboxModelService;
    }

    @Override
    public List<SandboxData.Models> ec2SandboxModels() {
        List<SandboxData.Models> models = new ArrayList<>();
        try {
            SandboxData.ModelParam param = SandboxData.ModelParam.builder().type("EC2").build();
            models = sandboxMapper.findSandboxModels(param);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("시스템 오류입니다. 관리자에게 문의하세요.");
        }
        return models;
    }

    @Override
    public List<SandboxData.Models> sagemakerSandboxModels() {
        List<SandboxData.Models> models = new ArrayList<>();
        try {
            SandboxData.ModelParam param = SandboxData.ModelParam.builder().type("SAGEMAKER").build();
            models = sandboxMapper.findSandboxModels(param);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("시스템 오류입니다. 관리자에게 문의하세요.");
        }
        return models;
    }

    @Override
    public void addSandbox(SandboxData.AddSandboxParam param) {
        Assert.notNull(param, "AddSandboxParam must be not null");
        try {
            //userData에서 Idx값 가져오기
            UserData userData = getUser();
            long userIdx = userData.getIdx();

            //param에 사용자 idx와 sandbox Status를 set.
            param.setUserIdx(userIdx);
            param.setStatus(SandboxStatus.WAITING);

            sandboxMapper.addSandbox(param);

            //로그 입력
//            addAgentLog(param);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("시스템 오류입니다. 관리자에게 문의하세요.");
        }
    }

    //사용자가 생성한 샌드박스 목록 조회
    @Override
    public List<SandboxData> sandboxes() {

        List<SandboxData> sandboxes = new ArrayList<>();

        try {
            //접속 사용자 id 가져 옴. 접속 사용자가 생성한 샌드박스 목록을 조회하기 위함.
            UserData userData = getUser();
            long userId = userData.getIdx();

            sandboxes = sandboxMapper.findUserSandboxByUserId(userId);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("시스템 오류입니다. 관리자에게 문의하세요.");
        }
        return sandboxes;
    }

    @Override
    public List<SandboxData> findSandboxDetailCloudSandboxInstanceList() {

        List<SandboxData> sandboxes = new ArrayList<>();
        try {
            sandboxes = sandboxMapper.findSandboxDetailCloudSandboxInstanceList();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("시스템 오류입니다. 관리자에게 문의하세요.");
        }
        return sandboxes;
    }

    @Override
    public SandboxData getSandboxDetailData(long idx) {

        SandboxData sandboxData = null;

        try {
            sandboxData = sandboxMapper.findSandboxDetailCloudSandboxInstance(idx);

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("시스템 오류입니다. 관리자에게 문의하세요.");
        }

        return sandboxData;
    }

    @Override
    public void terminateSandbox(SandboxData.TerminateSandboxParam param) {
        Assert.notNull(param, "TerminateSandboxParam must be not null");
        try {
            //userData에서 Idx값 가져오기
            UserData userData = getUser();
            long userIdx = userData.getIdx();

            //param set : 종료 신청하는 사용자 idx, 샌드박스 상태 "PROGRESS"
            param.setUserIdx(userIdx);
            param.setStatus(SandboxStatus.PROGRESS);
            sandboxMapper.terminateSandbox(param);

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("시스템 오류입니다. 관리자에게 문의하세요.");
        }
    }


    @Override
    public SandboxData getSandbox(long idx) {

        SandboxData sandboxData = null;

        try {
            sandboxData = sandboxMapper.getSandbox(idx);

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("시스템 오류입니다. 관리자에게 문의하세요.");
        }
        return sandboxData;
    }


    /**
     * Admin 권한: 샌드박스 승인
     * @param param
     * @return
     */
    @Override
    @Transactional
    public StringBuffer acceptSandbox(SandboxData.UpdateSandboxParam param) {

        String instanceUuid = UUID.randomUUID().toString();
        UserData userData = getUser();
        long sandboxIdx = param.getIdx();

        SandboxData sandboxData = null;
        SandboxModelData sandboxModelData = null;

        try {
            sandboxData = sandboxMapper.getSandbox(sandboxIdx);

            if (sandboxData == null) {
                throw new IllegalArgumentException("sandboxData is Null");
            }
            sandboxModelData = sandboxModelService.getSandboxDetailSpecImage(sandboxData.getSandboxModelIdx());

            if (sandboxModelData == null) {
                throw new IllegalArgumentException("sandboxModelData is Null");
            }

            CloudSandboxInstanceData.AddCloudSandboxInstanceParam addCloudSandboxInstanceParam =
                    CloudSandboxInstanceData.AddCloudSandboxInstanceParam.builder().idx(sandboxIdx).instanceId(instanceUuid).amiId(sandboxModelData.getCloudImageId()).instanceVolume(sandboxData.getVolume()).instanceType(sandboxModelData.getSandboxSpecInstanceType()).sandboxType(SandboxType.EC2).build();

            CloudSandboxInstanceData.AddCloudSandboxInstanceParam addEc2Result = cloudSandboxInstanceService.addInstance(addCloudSandboxInstanceParam);

            //sandboxMapper.updateSandbox(param);
            StringBuffer result = new StringBuffer();
            result.append(addEc2Result.getInstanceId().length()>0 ? "SUCCESS": "FAIL");
            return result;

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new IllegalStateException(e.getMessage());
        }

    }

    @Override
    @Transactional
    public void rejectSandbox(long idx) {

        UserData userData = getUser();
        try {
            SandboxData.UpdateSandboxParam updateParam =
                    SandboxData.UpdateSandboxParam.builder().idx(idx).sandboxStatus(SandboxStatus.REJECTED).userIdx(userData.getIdx()).build();
            sandboxMapper.updateSandbox(updateParam);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new IllegalStateException(e.getMessage());
        }

    }

    //샌드박스 자동중지
    @Override
    public void autoStop(List<String> param) {
        Assert.notNull(param, "AutoStopSandboxParam must be not null");

        try {
            //관리자 id 가져오기
            UserData userData = getUser();
            long userId = userData.getIdx();

            //TODO 모든 샌드박스 0으로 reset -> upsert 방법 찾으면 적용
            sandboxMapper.autoStopReset();

            //mapper에 id를 in으로 처리
            sandboxMapper.autoStop(param, userId);

//            //로그 입력
//            addAgentLog(param);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("시스템 오류입니다. 관리자에게 문의하세요.");
        }
    }

    //전체 샌드박스 목록 조회 : 샌드박스 자동중지 페이지에서 사용
    @Override
    public List<SandboxData> allSandboxes() {
        List<SandboxData> sandboxes = new ArrayList<>();
        try {
            sandboxes = sandboxMapper.findSandboxes();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("시스템 오류입니다. 관리자에게 문의하세요.");
        }
        return sandboxes;
    }

    @Override
    public long getSandboxCount(String status) {
        long sandboxCnt = 0;

        try {
            sandboxCnt = sandboxMapper.findSandboxByStatus(status);

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("시스템 오류입니다. 관리자에게 문의하세요.");
        }


        return sandboxCnt;
    }


    @Override
    public long getUserSandboxCount(String status, long userId) {
        long usersandboxCnt = 0;

        try {
           /* UserData userData = getUser();
            long userId = userData.getIdx();*/

            usersandboxCnt = sandboxMapper.findUserSandboxByStatus(status, userId);

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("시스템 오류입니다. 관리자에게 문의하세요.");
        }

        return usersandboxCnt;
    }

}
