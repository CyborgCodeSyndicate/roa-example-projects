package io.cyborgcode.ui.complex.test.framework.ui.model;

import io.cyborgcode.ui.complex.test.framework.ui.elements.InputFields;
import io.cyborgcode.roa.ui.annotations.InsertionElement;
import lombok.*;

import static io.cyborgcode.ui.complex.test.framework.ui.elements.InputFields.Data.PASSWORD_FIELD;
import static io.cyborgcode.ui.complex.test.framework.ui.elements.InputFields.Data.USERNAME_FIELD;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Seller {

   private String name;
   private String surname;

   @InsertionElement(locatorClass = InputFields.class, elementEnum = USERNAME_FIELD, order = 1)
   private String email;

   @InsertionElement(locatorClass = InputFields.class, elementEnum = PASSWORD_FIELD, order = 2)
   private String password;

}
