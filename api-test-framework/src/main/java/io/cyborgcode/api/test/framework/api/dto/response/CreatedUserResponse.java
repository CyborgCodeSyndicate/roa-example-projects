package io.cyborgcode.api.test.framework.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatedUserResponse {

   private String name;
   private String job;
   private String id;
   private String createdAt;

}
