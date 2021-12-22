package mzc.data.portal.agent.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ObjectData {

    private String objectName;
    private boolean dir;
    private String size;
    private ZonedDateTime lastModified;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GetObjectsParam {
        private String bucketName;
        private String prefix;
    }
}
