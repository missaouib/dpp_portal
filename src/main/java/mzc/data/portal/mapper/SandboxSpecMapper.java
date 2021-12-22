package mzc.data.portal.mapper;

import mzc.data.portal.dto.SandboxSpecData;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SandboxSpecMapper {

    List<SandboxSpecData> findSandboxSpecs(String type);
    SandboxSpecData.GetSandboxSpecInfoParam getSandboxSpecInfo(long sandboxSpecIdx); //model spec idx에 해당하는 정보를 겟
}
