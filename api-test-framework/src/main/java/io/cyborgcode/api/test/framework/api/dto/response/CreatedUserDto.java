package io.cyborgcode.api.test.framework.api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatedUserDto {

   private String name;
   private String job;
   private String id;
   private String createdAt;
   @JsonProperty("_meta")
   private Meta meta;

}
