package mzc.data.portal.service.impl;

import lombok.extern.slf4j.Slf4j;
import mzc.data.portal.dto.SandboxModelData;
import mzc.data.portal.dto.UserData;
import mzc.data.portal.mapper.SandboxModelMapper;
import mzc.data.portal.service.SandboxModelService;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@ComponentScan
public class SandboxModelServiceImpl extends BaseServiceImpl implements SandboxModelService {

    private SandboxModelMapper sandboxModelMapper;

    /**
     * 생성자 주입
     * @param sandboxModelMapper
     */
    @Autowired
    public SandboxModelServiceImpl(SandboxModelMapper sandboxModelMapper){
        this.sandboxModelMapper = sandboxModelMapper;
    }

    //샌드박스 모델 생성하기
    @Override
    public void addSandboxModel(SandboxModelData.AddSandboxModelParam param) {
        Assert.notNull(param, "AddSandboxModelParam must be not null");

        try {
            //To do!
            param.setSandboxModelTags("{}");
            sandboxModelMapper.addSandboxModel(param);

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("시스템 오류입니다.관리자에게 문의하세요.");
        }
    }

    @Override
    public List<SandboxModelData> sandboxModelList() {

        List<SandboxModelData> sandboxModelList = new ArrayList<>();

        try {
            sandboxModelList = sandboxModelMapper.findSandboxModels();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("시스템 오류입니다. 관리자에게 문의하세요.");
        }
        return sandboxModelList;
    }

    @Override
    public SandboxModelData.GetSandboxModelInfoParam getSandboxModelInfo(long sandboxModelIdx) {

        SandboxModelData.GetSandboxModelInfoParam sandboxModelInfo = null;

        try {
            sandboxModelInfo = sandboxModelMapper.getSandboxModelInfo(sandboxModelIdx);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("시스템 오류입니다. 관리자에게 문의하세요.");
        }
        return sandboxModelInfo;
    }

    //샌드박스 모델 업데이트
    @Override
    public void updateSandboxModel(SandboxModelData.UpdateSandboxModelParam param) {

        UserData userData = getUser();
        param.setSandboxModelUpdatedUserIdx(userData.getIdx());

        try {
            sandboxModelMapper.updateSandboxModel(param);

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("시스템 오류입니다.관리자에게 문의하세요.");
        }
    }

    @Override
    public SandboxModelData getSandboxDetailSpecImage(long idx) {

        SandboxModelData sandboxModelData = null;

        try {
            sandboxModelData = sandboxModelMapper.getSandboxModelDetailSpecImage(idx);
            if(sandboxModelData == null) {
                throw new IllegalArgumentException("생성된 모델이 없습니다.");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("시스템 오류입니다.관리자에게 문의하세요.");
        }

        return sandboxModelData;
    }

}
