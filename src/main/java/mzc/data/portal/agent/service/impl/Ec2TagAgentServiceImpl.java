package mzc.data.portal.agent.service.impl;

import lombok.extern.slf4j.Slf4j;
import mzc.data.portal.agent.dto.TagData;
import mzc.data.portal.agent.service.Ec2TagAgentService;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.CreateTagsRequest;
import software.amazon.awssdk.services.ec2.model.Tag;
import software.amazon.awssdk.services.sts.model.AssumeRoleResponse;
import software.amazon.awssdk.services.sts.model.Credentials;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class Ec2TagAgentServiceImpl implements Ec2TagAgentService {

    @Override
    @Retryable(value = AwsServiceException.class, backoff = @Backoff(delay = 200))
    public void setEc2Tag(String resourceId, List<TagData> cloudTags) {
        log.debug("set: {}", resourceId);
        log.debug("set: {}", cloudTags);

        try (Ec2Client ec2Client = createEc2Client()) {

            List<Tag> tags = convertTags(cloudTags);
            CreateTagsRequest tagsRequest = CreateTagsRequest.builder().resources(resourceId).tags(tags).build();

            ec2Client.createTags(tagsRequest);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    private List<Tag> convertTags(List<TagData> cloudTags) {
        return cloudTags.stream().map(t -> Tag.builder().key(t.getKey()).value(t.getValue()).build()).collect(Collectors.toList());
    }

    private static Ec2Client createEc2Client() {
        return Ec2Client.builder().region(software.amazon.awssdk.regions.Region.AP_NORTHEAST_2).build();
    }

}
