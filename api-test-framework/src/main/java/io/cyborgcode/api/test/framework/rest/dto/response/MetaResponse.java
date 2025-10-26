package io.cyborgcode.api.test.framework.rest.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetaResponse {

   @JsonProperty("powered_by")
   private String poweredBy;
   @JsonProperty("upgrade_url")
   private String upgradeUrl;
   @JsonProperty("docs_url")
   private String docsUrl;
   @JsonProperty("template_gallery")
   private String templateGallery;
   private String message;
   private List<MetaFeatures> features;
   @JsonProperty("upgrade_cta")
   private String upgradeCta;

}
