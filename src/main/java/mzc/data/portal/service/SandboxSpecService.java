package mzc.data.portal.service;

import mzc.data.portal.dto.SandboxSpecData;

import java.util.List;

public interface SandboxSpecService {

    List<SandboxSpecData> sandboxSpecs(String type); //샌드박스 스펙들 가져오기
    SandboxSpecData.GetSandboxSpecInfoParam getSandboxSpecInfo(long sandboxSpecIdx); //특정한 샌드박스 스펙 인포 겟

}
