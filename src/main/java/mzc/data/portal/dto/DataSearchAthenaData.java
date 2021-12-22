package mzc.data.portal.dto;


import lombok.*;
import mzc.data.portal.enums.DataSearchDeleteYN;

import java.time.LocalDateTime;

@Data
@Getter
public class DataSearchAthenaData {

    private long userIdx;
    private long dataSearchIdx;
    private String userId;
    private String dataSearchQuery;
    private LocalDateTime dataSearchCreatedAt;
    private DataSearchDeleteYN dataSearchDeleteYN;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AddDataSearchParam {
        private long userIdx;
        private long dataSearchIdx;
        private String userId;
        private String dataSearchQuery;
        private LocalDateTime dataSearchCreatedAt;
        private DataSearchDeleteYN dataSearchDeleteYN;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DeleteDataSearchParam {
        private long userIdx;
        private long dataSearchIdx;
        private DataSearchDeleteYN dataSearchDeleteYN;

    }

    @NoArgsConstructor
    public static class DataSearchCustomRes<T> extends ApiResponse<T> {
        public DataSearchCustomRes(int status, String error, T data) {
            super(status, error, data);
        }
    }
}
