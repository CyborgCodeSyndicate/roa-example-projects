package io.cyborgcode.api.test.framework.api.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserData {

   private int id;
   private String email;
   @JsonProperty("first_name")
   private String firstName;
   @JsonProperty("last_name")
   private String lastName;
   private String avatar;

}
