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
public class GetUsersResponse {

   private int page;
   @JsonProperty("per_page")
   private int perPage;
   private int total;
   @JsonProperty("total_pages")
   private int totalPages;
   private List<DataResponse> data;
   private SupportResponse support;
   @JsonProperty("_meta")
   private MetaResponse meta;

}
