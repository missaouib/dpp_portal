package mzc.data.portal.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;

@Data
@Getter
public class QueryRedshiftData {
    public static Object MetaSimpleQueryData;
    private long userIdx;
    private long queryIdx;
    private String dataSearchQuery;
    private LocalDateTime queryCreatedAt;


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MetaSimpleQueryData {
        private List<LinkedHashMap<String, Object>> columnResults;
        private List<LinkedHashMap<String, Object>> queryResults;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class QueryDataParam {
        private long userIdx;
        private long queryIdx;
        private String dataSearchQuery;
        private LocalDateTime queryCreatedAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class QueryDataPreviewParam {
        private long userIdx;
        private long queryIdx;
        private String dataSearchQuery;
        private LocalDateTime queryCreatedAt;
        private String database;
        private String schema;
        private String table;
    }

    @NoArgsConstructor
    public static class MetaSimpleQueryDataObjRes extends ApiResponse<QueryRedshiftData.MetaSimpleQueryData> implements Serializable {
        public MetaSimpleQueryDataObjRes(int status, String error, QueryRedshiftData.MetaSimpleQueryData data) {
            super(status, error, data);
        }
    }

}
