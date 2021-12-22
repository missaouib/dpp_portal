package mzc.data.portal.mapper;
import mzc.data.portal.dto.SandboxImageData;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SandboxImageMapper {

    List<SandboxImageData> findSandboxImages();
    SandboxImageData.GetSandboxImageInfoParam getSandboxImageInfo(long sandboxImageIdx);
    void addSandboxImage(SandboxImageData.AddSandboxImageParam param); //샌드박스 모델 생성하기
    void updateSandboxImage(SandboxImageData.UpdateSandboxImageParam param); //샌드박스 이미지 업데이트
}
