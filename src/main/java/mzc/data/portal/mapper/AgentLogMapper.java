package mzc.data.portal.mapper;

import mzc.data.portal.dto.AgentLogData;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentLogMapper {
    void add(AgentLogData.AddAgentLogParam param);

    void update(AgentLogData.UpdateAgentLogParam param);

    AgentLogData get(AgentLogData.GetAgentLogParam param);
}
