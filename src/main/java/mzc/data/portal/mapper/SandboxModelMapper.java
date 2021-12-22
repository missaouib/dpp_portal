package mzc.data.portal.mapper;

import mzc.data.portal.dto.SandboxModelData;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SandboxModelMapper {

    void addSandboxModel(SandboxModelData.AddSandboxModelParam param);
    List<SandboxModelData> findSandboxModels();
    SandboxModelData.GetSandboxModelInfoParam getSandboxModelInfo(long sandboxModelIdx); //model idx에 해당하는 모델 정보를 겟
    void updateSandboxModel(SandboxModelData.UpdateSandboxModelParam param);

    SandboxModelData getSandboxModelDetailSpecImage(long idx);
}
