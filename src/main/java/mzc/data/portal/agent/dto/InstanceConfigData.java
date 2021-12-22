package mzc.data.portal.agent.dto;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
public class InstanceConfigData {
    private List<String> subnetIds;
    private List<String> windowsSecurityGroup;
    private String windowsHostName;
    private List<String> linuxSecurityGroup;
    private String ebsDeviceName;
    private String iamRoleArn;
    private String linuxUserId;
    private String linuxUserPasswd;
    private String hostnamePrefix;
    private String instanceName;
    private String adjoinBucket;
    private String adjoinDocument;
}
