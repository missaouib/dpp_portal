package mzc.data.portal.service;

import mzc.data.portal.dto.SandboxModelData;

import java.util.List;

public interface SandboxModelService {

    void addSandboxModel(SandboxModelData.AddSandboxModelParam param); //샌드박스 모델 생성하기
    List<SandboxModelData> sandboxModelList();
    SandboxModelData.GetSandboxModelInfoParam getSandboxModelInfo(long sandboxModelIdx); //특정한 샌드박스모델 인포 겟
    void updateSandboxModel(SandboxModelData.UpdateSandboxModelParam param); //샌드박스 모델 업데이트

    SandboxModelData getSandboxDetailSpecImage(long idx);

}
