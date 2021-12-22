package mzc.data.portal.agent.dto;

import lombok.Data;

@Data
public class TableColumnData {
    private String column;
    private String type;
    private long length;
}
