package io.cyborgcode.ui.complex.test.framework.db.queries;

import io.cyborgcode.roa.db.query.DbQuery;

public enum AppQueries implements DbQuery<AppQueries> {

   QUERY_SELLER("SELECT * FROM sellers WHERE id = {id}"),
   QUERY_SELLER_EMAIL("SELECT email FROM sellers WHERE id = {id}"),
   QUERY_SELLER_PASSWORD("SELECT password FROM sellers WHERE id = {id}"),
   QUERY_ORDER("SELECT * FROM orders WHERE id = {id}"),
   QUERY_ORDER_ALL("SELECT * FROM orders"),
   QUERY_ORDER_PRODUCT("SELECT product FROM orders WHERE id = {id}"),
   QUERY_ORDER_DELETE("DELETE FROM orders WHERE id = {id}");

   private final String query;

   AppQueries(final String query) {
      this.query = query;
   }

   @Override
   public String query() {
      return query;
   }

   @Override
   public AppQueries enumImpl() {
      return this;
   }

}
