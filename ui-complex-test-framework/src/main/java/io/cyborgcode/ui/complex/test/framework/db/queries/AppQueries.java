package io.cyborgcode.ui.complex.test.framework.db.queries;

import io.cyborgcode.roa.db.query.DbQuery;

/**
 * Registry of database queries for the Bakery Flow application.
 * <p>
 * Each enum constant represents a parameterized SQL query that can be executed via
 * ROA's database ring ({@code RING_OF_DB}). Queries support parameter substitution
 * using the {@code {paramName}} syntax, which is replaced at runtime via
 * {@link #withParam(String, Object)}.
 * </p>
 * <p>
 * This centralized query registry:
 * <ul>
 *   <li>makes SQL statements discoverable and reusable,</li>
 *   <li>prevents SQL duplication across test code,</li>
 *   <li>enables type-safe query execution via ROA's fluent API,</li>
 *   <li>supports parameterized queries for test flexibility.</li>
 * </ul>
 * </p>
 */
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
