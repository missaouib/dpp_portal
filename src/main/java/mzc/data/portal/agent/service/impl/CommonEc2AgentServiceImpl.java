package mzc.data.portal.agent.service.impl;

import mzc.data.portal.agent.dto.TagData;
import software.amazon.awssdk.services.ec2.model.Tag;

import java.util.List;
import java.util.stream.Collectors;

public abstract class CommonEc2AgentServiceImpl {
    protected List<Tag> convertTags(List<TagData> cloudTags) {
        return cloudTags.stream().map(t -> Tag.builder().key(t.getKey()).value(t.getValue()).build()).collect(Collectors.toList());
    }
}
