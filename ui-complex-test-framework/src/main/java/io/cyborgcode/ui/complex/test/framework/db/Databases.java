package io.cyborgcode.ui.complex.test.framework.db;

import io.cyborgcode.roa.db.config.DbType;

import java.sql.Driver;

/**
 * Registry of supported database types for the test framework.
 *
 * <p>Each enum constant represents a database driver and connection protocol that can be used with
 * ROA database testing capabilities. This abstraction allows tests to switch between database
 * types (e.g., H2, PostgreSQL, MySQL) without changing test code.
 *
 * <p>Currently supported:
 *
 * <ul>
 *   <li>{@link #H2} — in-memory H2 database for fast, isolated testing
 * </ul>
 *
 * <p>The framework uses this enum in conjunction with database configuration to establish JDBC
 * connections and execute queries via the {@code RING_OF_DB}.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public enum Databases implements DbType<Databases> {
   H2(new org.h2.Driver(), "jdbc:h2");

   private final Driver driver;
   private final String protocol;

   Databases(final Driver driver, final String protocol) {
      this.driver = driver;
      this.protocol = protocol;
   }

   @Override
   public Driver driver() {
      return driver;
   }

   @Override
   public String protocol() {
      return protocol;
   }

    @Override
    public Databases enumImpl() {
        return this;
    }

}
