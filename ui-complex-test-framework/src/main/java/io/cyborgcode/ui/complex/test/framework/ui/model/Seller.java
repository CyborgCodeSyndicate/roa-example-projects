package io.cyborgcode.ui.complex.test.framework.ui.model;

import io.cyborgcode.ui.complex.test.framework.ui.elements.InputFields;
import io.cyborgcode.roa.ui.annotations.InsertionElement;
import lombok.*;

import static io.cyborgcode.ui.complex.test.framework.ui.elements.InputFields.Data.PASSWORD_FIELD;
import static io.cyborgcode.ui.complex.test.framework.ui.elements.InputFields.Data.USERNAME_FIELD;

/**
 * Domain model representing a seller/user in the Bakery Flow application.
 * <p>
 * This class is used for authentication and user management tests. Fields annotated with
 * {@link InsertionElement @InsertionElement} enable automatic form filling via ROA's
 * insertion mechanism: {@code quest.use(RING_OF_UI).insertion().insertData(seller)}
 * will automatically populate username and password fields in the correct order.
 * </p>
 * <p>
 * The {@code @InsertionElement} annotation maps each field to a specific UI element
 * and execution order, abstracting the manual input logic and making tests more maintainable.
 * </p>
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Seller {

   private String name;
   private String surname;

   @InsertionElement(locatorClass = InputFields.class, elementEnum = USERNAME_FIELD, order = 1)
   private String username;

   @InsertionElement(locatorClass = InputFields.class, elementEnum = PASSWORD_FIELD, order = 2)
   private String password;

}
