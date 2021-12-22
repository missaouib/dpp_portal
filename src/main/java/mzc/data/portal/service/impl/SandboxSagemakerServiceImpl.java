package mzc.data.portal.service.impl;

import lombok.extern.slf4j.Slf4j;
import mzc.data.portal.dto.CloudSandboxInstanceData;
import mzc.data.portal.dto.SandboxData;
import mzc.data.portal.dto.SandboxModelData;
import mzc.data.portal.dto.UserData;
import mzc.data.portal.enums.SandboxStatus;
import mzc.data.portal.enums.SandboxType;
import mzc.data.portal.mapper.SandboxMapper;
import mzc.data.portal.service.CloudSandboxInstanceService;
import mzc.data.portal.service.SandboxModelService;
import mzc.data.portal.service.SandboxSagemakerService;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@ComponentScan
public class SandboxSagemakerServiceImpl extends BaseServiceImpl implements SandboxSagemakerService {
    
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
    public SandboxSagemakerServiceImpl(SandboxMapper sandboxMapper, CloudSandboxInstanceService cloudSandboxInstanceService, SandboxModelService sandboxModelService){
        this.sandboxMapper = sandboxMapper;
        this.cloudSandboxInstanceService = cloudSandboxInstanceService;
        this.sandboxModelService = sandboxModelService;
    }

    @Override
    public void acceptSandbox(SandboxData.UpdateSandboxParam param) {

        String instanceUuid = UUID.randomUUID().toString();
        UserData userData = getUser();
        long sandboxIdx = param.getIdx();

        SandboxData sandboxData = null;
        SandboxModelData sandboxModelData = null;

        try {
            sandboxData = sandboxMapper.getSandbox(sandboxIdx);

            if(sandboxData == null) {
                throw new IllegalArgumentException("sandboxData is Null");
            }
            sandboxModelData = sandboxModelService.getSandboxDetailSpecImage(sandboxData.getSandboxModelIdx());

            if(sandboxModelData == null) {
                throw new IllegalArgumentException("sandboxModelData is Null");
            }

            CloudSandboxInstanceData.AddCloudSandboxInstanceParam addCloudSandboxInstanceParam =
                    CloudSandboxInstanceData.AddCloudSandboxInstanceParam.builder().idx(sandboxIdx).instanceId(instanceUuid).amiId(sandboxModelData.getCloudImageId()).instanceVolume(sandboxData.getVolume()).instanceType(sandboxModelData.getSandboxSpecInstanceType()).sandboxType(SandboxType.SAGEMAKER).build();

            cloudSandboxInstanceService.addInstance(addCloudSandboxInstanceParam);

            //sandboxMapper.updateSandbox(param);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new IllegalStateException(e.getMessage());
        }

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




}
