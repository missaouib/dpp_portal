package mzc.data.portal.service.impl;

import lombok.extern.slf4j.Slf4j;
import mzc.data.portal.dto.SandboxImageData;
import mzc.data.portal.dto.UserData;
import mzc.data.portal.enums.SandboxImageStatus;
import mzc.data.portal.enums.SandboxStatus;
import mzc.data.portal.mapper.SandboxImageMapper;
import mzc.data.portal.service.SandboxImageService;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@ComponentScan
public class SandboxImageServiceImpl extends BaseServiceImpl implements SandboxImageService {

    private SandboxImageMapper sandboxImageMapper;

    /**
     * 생성자 주입
     * @param sandboxImageMapper
     */
    @Autowired
    public SandboxImageServiceImpl(SandboxImageMapper sandboxImageMapper){
        this.sandboxImageMapper = sandboxImageMapper;
    }

    @Override
    public List<SandboxImageData> sandboxImages() {
        List<SandboxImageData> sandboxImages = new ArrayList<>();
        try {
            sandboxImages = sandboxImageMapper.findSandboxImages();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("시스템 오류입니다.관리자에게 문의하세요.");
        } return sandboxImages;
    }

    @Override
    public SandboxImageData.GetSandboxImageInfoParam getSandboxImageInfo(long sandboxImageIdx) {
        SandboxImageData.GetSandboxImageInfoParam sandboxImageInfo = null;

        try {
            sandboxImageInfo = sandboxImageMapper.getSandboxImageInfo(sandboxImageIdx);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("시스템 오류입니다. 관리자에게 문의하세요.");
        }
        return sandboxImageInfo;
    }

    //샌드박스 이미지 생성하기
    @Override
    public void addSandboxImage(SandboxImageData.AddSandboxImageParam param) {
        Assert.notNull(param, "AddSandboxImageParam must be not null");

        try {
            UserData userData = getUser();
            param.setSandboxImageCreatedUserIdx(userData.getIdx());
            param.setSandboxImageStatus(SandboxImageStatus.ACTIVE);
            sandboxImageMapper.addSandboxImage(param);

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("시스템 오류입니다.관리자에게 문의하세요.");
        }
    }

    //샌드박스 이미지 업데이트
    @Override
    public void updateSandboxImage(SandboxImageData.UpdateSandboxImageParam param) {

        UserData userData = getUser();
        param.setSandboxImageUpdatedUserIdx(userData.getIdx());

        try {
            sandboxImageMapper.updateSandboxImage(param);

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("시스템 오류입니다.관리자에게 문의하세요.");
        }
    }

}
