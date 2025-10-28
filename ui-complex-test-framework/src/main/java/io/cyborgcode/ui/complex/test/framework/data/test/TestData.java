package io.cyborgcode.ui.complex.test.framework.data.test;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"system:properties", "classpath:${test.data.file}.properties"})
public interface TestData extends Config {

   @Key("username")
   String username();

   @Key("password")
   String password();

   @Key("seller.email")
   String sellerEmail();

   @Key("seller.password")
   String sellerPassword();

   @Key("order.customer.name")
   String customerName();

   @Key("order.customer.details")
   String customerDetails();

   @Key("order.phone.number")
   String phoneNumber();

   @Key("order.location")
   String location();

   @Key("order.product")
   String product();

}
