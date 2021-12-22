package mzc.data.portal.service;

import mzc.data.portal.dto.SandboxImageData;
import mzc.data.portal.dto.SandboxModelData;
import mzc.data.portal.dto.SandboxSpecData;

import java.util.List;

public interface SandboxImageService {

    List<SandboxImageData> sandboxImages(); //샌드박스 이미지 가져오기
    SandboxImageData.GetSandboxImageInfoParam getSandboxImageInfo(long sandboxImageIdx);
    void addSandboxImage(SandboxImageData.AddSandboxImageParam param); //샌드박스 모델 생성하기
    void updateSandboxImage(SandboxImageData.UpdateSandboxImageParam param); //샌드박스 이미지 업데이트

}
