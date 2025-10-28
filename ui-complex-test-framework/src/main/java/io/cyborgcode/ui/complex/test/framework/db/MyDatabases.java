package io.cyborgcode.ui.complex.test.framework.db;

import io.cyborgcode.roa.db.config.DbType;

import java.sql.Driver;

public enum MyDatabases implements DbType<MyDatabases> {
   POSTGRESQL(new org.postgresql.Driver(), "jdbc:postgresql"),
   H2(new org.h2.Driver(), "jdbc:h2");

   private final Driver driver;
   private final String protocol;


   MyDatabases(final Driver driver, final String protocol) {
      this.driver = driver;
      this.protocol = protocol;
   }


   @Override
   public MyDatabases enumImpl() {
      return this;
   }


   @Override
   public Driver driver() {
      return driver;
   }


   @Override
   public String protocol() {
      return protocol;
   }
}
