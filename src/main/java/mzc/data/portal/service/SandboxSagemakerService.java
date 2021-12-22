package mzc.data.portal.service;

import mzc.data.portal.dto.SandboxData;

public interface SandboxSagemakerService {
    SandboxData getSandboxDetailData(long idx);

    void acceptSandbox(SandboxData.UpdateSandboxParam param);

    void rejectSandbox(long idx);

    void terminateSandbox(SandboxData.TerminateSandboxParam param);
}
