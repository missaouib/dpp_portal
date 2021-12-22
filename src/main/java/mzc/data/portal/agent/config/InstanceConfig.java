package mzc.data.portal.agent.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class InstanceConfig {

    @Value("${cloud.instance.subnet-id}")
    private String instanceSubnetId;

    @Value("${cloud.instance.security-group}")
    private String instanceSecurityGroup;

    @Value("${cloud.instance.ebs-device-name}")
    private String instanceDeviceName;
}
