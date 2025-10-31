package io.cyborgcode.ui.complex.test.framework.db.queries;

import io.cyborgcode.roa.db.config.DatabaseConfiguration;
import io.cyborgcode.roa.db.query.DbQuery;

public enum Queries implements DbQuery<Queries> {

   QUERY_SELLER("SELECT * FROM sellers WHERE id = {id}"),
   QUERY_SELLER_EMAIL("SELECT email FROM sellers WHERE id = {id}"),
   QUERY_SELLER_PASSWORD("SELECT password FROM sellers WHERE id = {id}"),
   QUERY_ORDER("SELECT * FROM orders WHERE id = {id}"),
   QUERY_ORDER_ALL("SELECT * FROM orders"),
   QUERY_ORDER_PRODUCT("SELECT product FROM orders WHERE id = {id}"),
   QUERY_ORDER_DELETE("DELETE FROM orders WHERE id = {id}");

   private final String query;

   Queries(final String query) {
      this.query = query;
   }

   @Override
   public String query() {
      return query;
   }

   @Override
   public DatabaseConfiguration config() {
      return DbQuery.super.config();
   }

   @Override
   public Queries enumImpl() {
      return this;
   }

}
