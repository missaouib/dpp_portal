package mzc.data.portal.agent.service;

import mzc.data.portal.agent.dto.BucketData;
import mzc.data.portal.agent.dto.ObjectData;

import java.util.List;

public interface ObjectAgentService {
    List<BucketData> listBuckets();

    List<ObjectData> listObjects(ObjectData.GetObjectsParam param);

    byte[] downloadObject(ObjectData.GetObjectsParam param);
}
