package io.cyborgcode.api.test.framework.api.dto.response;

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
public class GetUsersDto {

   private int page;
   @JsonProperty("per_page")
   private int perPage;
   private int total;
   @JsonProperty("total_pages")
   private int totalPages;
   private List<UserData> data;
   private Support support;
   @JsonProperty("_meta")
   private Meta meta;

}
