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
public class Meta {

   @JsonProperty("powered_by")
   private String poweredBy;
   @JsonProperty("docs_url")
   private String docsUrl;
   @JsonProperty("upgrade_url")
   private String upgradeUrl;
   @JsonProperty("example_url")
   private String exampleUrl;
   private String variant;
   private String message;
   private Cta cta;
   private String context;

}
