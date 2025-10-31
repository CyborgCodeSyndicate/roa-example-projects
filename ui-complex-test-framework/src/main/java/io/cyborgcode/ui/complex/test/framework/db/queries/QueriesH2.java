package io.cyborgcode.ui.complex.test.framework.db.queries;

import io.cyborgcode.roa.db.query.DbQuery;

public enum QueriesH2 implements DbQuery<QueriesH2> {

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

   QueriesH2(final String query) {
      this.query = query;
   }

   @Override
   public String query() {
      return query;
   }

   @Override
   public QueriesH2 enumImpl() {
      return this;
   }

}
