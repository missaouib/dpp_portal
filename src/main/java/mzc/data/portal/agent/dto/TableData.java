package mzc.data.portal.agent.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableData {
    private String tableName;
    private List<TableColumnData> columns;
    private String type;
    private long recordCount;
    private String location;
    private String typeOfData;
    private Instant createdAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GetTableParam {
        private String database;
        private String schema;
        private String table;
    }
}
