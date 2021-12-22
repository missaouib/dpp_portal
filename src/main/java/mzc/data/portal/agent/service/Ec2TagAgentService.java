package mzc.data.portal.agent.service;

import mzc.data.portal.agent.dto.TagData;

import java.util.List;

public interface Ec2TagAgentService {
    void setEc2Tag(String resourceId, List<TagData> tags);
}
