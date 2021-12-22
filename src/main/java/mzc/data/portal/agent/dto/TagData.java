package mzc.data.portal.agent.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagData {
    public static TagData of(String key, String value) {
        return TagData.builder().key(key).value(value).build();
    }

    String key;
    String value;
}
