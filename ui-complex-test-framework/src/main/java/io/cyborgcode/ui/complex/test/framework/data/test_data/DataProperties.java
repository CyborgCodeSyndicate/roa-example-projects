package io.cyborgcode.ui.complex.test.framework.data.test_data;

import io.cyborgcode.utilities.config.PropertyConfig;
import org.aeonbits.owner.Config;

/**
 * Central configuration interface for test data used by the example test suite.
 *
 * <p>Backed by the Owner library and {@link PropertyConfig}, it:
 *
 * <ul>
 *   <li>Loads values from system properties and a configurable properties file (via {@code
 *       test.data.file}.properties on the classpath)
 *   <li>Provides strongly-typed accessors for commonly used values such as {@code username} and
 *       {@code password}
 *   <li>Serves as the single source of truth for credentials and other shared test inputs
 * </ul>
 *
 * <p>This allows the tests to read configuration in a consistent, framework-friendly way without
 * hardcoding values in the test code.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"system:properties", "classpath:${test.data.file}.properties"})
public interface DataProperties extends PropertyConfig {

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
