package io.cyborgcode.ui.complex.test.framework.db.queries;

import io.cyborgcode.roa.db.query.DbQuery;

/**
 * Database schema setup and seed data queries for test initialization.
 * <p>
 * This enum contains DDL (Data Definition Language) and DML (Data Manipulation Language)
 * statements used to bootstrap the in-memory H2 database before test execution. These
 * queries are typically invoked from {@link io.cyborgcode.ui.complex.test.framework.db.hooks.DbHookFunctions}
 * as part of the {@code @DbHook} lifecycle.
 * </p>
 * <p>
 * Available setup operations:
 * <ul>
 *   <li>{@link #CREATE_TABLE_ORDERS} — creates the orders table schema</li>
 *   <li>{@link #CREATE_TABLE_SELLERS} — creates the sellers table schema</li>
 *   <li>{@link #INSERT_ORDERS} — populates orders with seed data</li>
 *   <li>{@link #INSERT_SELLERS} — populates sellers with seed data</li>
 * </ul>
 * </p>
 * <p>
 * This centralized approach ensures consistent database state across all tests and
 * makes schema changes easy to track and version-control.
 * </p>
 */
public enum DbSetupQueries implements DbQuery<DbSetupQueries> {

   CREATE_TABLE_ORDERS(
         "CREATE TABLE orders ("
               + "id INT PRIMARY KEY, "
               + "customerName VARCHAR(255), "
               + "customerDetails VARCHAR(255), "
               + "phoneNumber VARCHAR(50), "
               + "location VARCHAR(255), "
               + "product VARCHAR(255)"
               + ")"
   ),
   CREATE_TABLE_SELLERS(
         "CREATE TABLE sellers ("
               + "id INT PRIMARY KEY, "
               + "email VARCHAR(255), "
               + "password VARCHAR(255)"
               + ")"
   ),
   INSERT_ORDERS(
         "INSERT INTO orders (id, customerName, customerDetails, phoneNumber, location, product) VALUES " +
               "(1, 'John Terry', 'Address', '+1-555-7777', 'Bakery', 'Strawberry Bun'), " +
               "(2, 'Petar Terry', 'Address', '+1-222-7778', 'Store', 'Strawberry Bun')"
   ),
   INSERT_SELLERS(
         "INSERT INTO sellers (id, email, password) VALUES " +
               "(1, 'admin@vaadin.com', 'admin'), " +
               "(2, 'admin@vaadin.com', 'admin')"
   );

   private final String query;

   DbSetupQueries(final String query) {
      this.query = query;
   }

   @Override
   public String query() {
      return query;
   }

   @Override
   public DbSetupQueries enumImpl() {
      return this;
   }

}
