package io.cyborgcode.api.test.framework.rest.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

   DataResponse data;
   SupportResponse support;
   @JsonProperty("_meta")
   private MetaResponse meta;

}
