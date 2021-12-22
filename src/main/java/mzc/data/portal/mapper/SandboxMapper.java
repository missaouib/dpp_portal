package mzc.data.portal.mapper;

import mzc.data.portal.dto.SandboxData;
import mzc.data.portal.enums.SandboxType;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SandboxMapper {

    List<SandboxData.Models> findSandboxModels(SandboxData.ModelParam param);

    void addSandbox(SandboxData.AddSandboxParam param);

    List<SandboxData> findSandboxes();

    SandboxData getSandbox(long idx);

    void updateSandbox(SandboxData.UpdateSandboxParam param);

    void terminateSandbox(SandboxData.TerminateSandboxParam param);

    SandboxData findSandboxDetailCloudSandboxInstance(long idx);

    List<SandboxData> findSandboxDetailCloudSandboxInstanceList();

    List<SandboxData> findCreatedSandboxDetailCloudSandboxInstanceList(SandboxType sandboxType);

    void autoStopReset();

    void autoStop(List<String> idx, long userId);

    List<SandboxData> findUserSandboxByUserId(long userId);

    long findSandboxByStatus(String status);

    long findUserSandboxByStatus(@Param("status") String status, @Param("userId") long userId);



}
