package mzc.data.portal.service;

import mzc.data.portal.dto.SandboxData;

import java.util.List;

public interface SandboxService {

    List<SandboxData.Models> ec2SandboxModels();
    List<SandboxData.Models> sagemakerSandboxModels();

    void addSandbox(SandboxData.AddSandboxParam param);

    List<SandboxData> sandboxes();

    void terminateSandbox(SandboxData.TerminateSandboxParam param);

    SandboxData getSandbox(long idx);

    List<SandboxData> findSandboxDetailCloudSandboxInstanceList();

    SandboxData getSandboxDetailData(long idx);

    StringBuffer acceptSandbox(SandboxData.UpdateSandboxParam param);

    void rejectSandbox(long idx);

//    void autoStop(List<SandboxData.AutoStopSandboxParam> param);
    void autoStop(List<String> param);

    List<SandboxData> allSandboxes();

    long getSandboxCount(String status);

    long getUserSandboxCount(String status, long userId);
}
