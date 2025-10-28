package io.cyborgcode.ui.complex.test.framework.rest.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Pet {

   private Long id;
   private Category category;
   private String name;
   private List<String> photoUrls;
   private List<Tag> tags;
   private Status status;

}
