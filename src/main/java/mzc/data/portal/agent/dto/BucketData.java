package mzc.data.portal.agent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BucketData {
    private String bucketName;
    private ZonedDateTime creationDate;
}
