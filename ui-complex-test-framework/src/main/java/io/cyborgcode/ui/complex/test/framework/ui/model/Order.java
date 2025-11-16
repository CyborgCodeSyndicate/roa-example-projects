package io.cyborgcode.ui.complex.test.framework.ui.model;

import io.cyborgcode.ui.complex.test.framework.ui.elements.InputFields;
import io.cyborgcode.ui.complex.test.framework.ui.elements.SelectFields;
import io.cyborgcode.roa.ui.annotations.InsertionElement;
import lombok.*;

import static io.cyborgcode.ui.complex.test.framework.ui.elements.InputFields.Data.*;
import static io.cyborgcode.ui.complex.test.framework.ui.elements.SelectFields.Data.LOCATION_DDL;
import static io.cyborgcode.ui.complex.test.framework.ui.elements.SelectFields.Data.PRODUCTS_DDL;

/**
 * Domain model representing a customer order in the test application.
 *
 * <p>This class encapsulates all order-related data and is used throughout the test suite for
 * creating, validating, and managing orders. Fields annotated with {@link InsertionElement} enable
 * automatic form filling via ROA insertion mechanism: {@code
 * quest.use(RING_OF_UI).insertion().insertData(order)} will automatically populate all order form
 * fields (inputs and selects) in the correct sequence.
 *
 * <p>The {@code @InsertionElement} annotation specifies the UI element mapping and execution order,
 * allowing tests to work at the domain level without coupling to low-level UI interactions. This
 * pattern significantly reduces test maintenance when UI structures change.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Order {

   private int id;

   @InsertionElement(locatorClass = InputFields.class, elementEnum = CUSTOMER_FIELD, order = 1)
   private String customerName;

   @InsertionElement(locatorClass = InputFields.class, elementEnum = DETAILS_FIELD, order = 2)
   private String customerDetails;

   @InsertionElement(locatorClass = InputFields.class, elementEnum = NUMBER_FIELD, order = 3)
   private String phoneNumber;

   @InsertionElement(locatorClass = SelectFields.class, elementEnum = LOCATION_DDL, order = 4)
   private String location;

   @InsertionElement(locatorClass = SelectFields.class, elementEnum = PRODUCTS_DDL, order = 5)
   private String product;

   private String productDetails;
   private String productCount;
   private String time;
   private String date;

}
