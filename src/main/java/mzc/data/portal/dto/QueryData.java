package mzc.data.portal.dto;

import lombok.*;
import mzc.data.portal.enums.DataSearchDeleteYN;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;

@Data
@Getter
public class QueryData {
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
        private String schema;
        private String table;
    }

    /*@Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class QueryHistoryLogData{
        private long userIdx;
        private long queryIdx;
        private String dataSearchQuery;
        private LocalDateTime queryCreatedAt;
    }*/

    @NoArgsConstructor
    public static class MetaSimpleQueryDataObjRes extends ApiResponse<MetaSimpleQueryData> implements Serializable {
        public MetaSimpleQueryDataObjRes(int status, String error, MetaSimpleQueryData data) {
            super(status, error, data);
        }
    }

    /*@NoArgsConstructor
    public static class QueryDataCustomRes<T> extends ApiResponse<T> {
        public QueryDataCustomRes(int status, String error, T data) {
            super(status, error, data);
        }
    }*/
}
