package io.cyborgcode.ui.complex.test.framework.db.hooks;

import io.cyborgcode.roa.db.hooks.DbHookFlow;
import io.cyborgcode.roa.db.service.DatabaseService;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.Map;

/**
 * Registry of database hook flows for the example test suite.
 *
 * <p>Each enum constant represents a reusable database setup/teardown operation that can be
 * referenced from {@code @DbHook} annotations. The underlying {@link TriConsumer} receives the
 * {@link DatabaseService}, shared storage, and optional arguments to execute database operations
 * before/after tests.
 *
 * <p>Hook flows are typically used for:
 *
 * <ul>
 *   <li>initializing in-memory or other databases with schema and seed data,
 *   <li>executing queries and storing results in test storage for assertions,
 *   <li>cleaning up database state after test execution.
 * </ul>
 *
 * <p>The nested {@link Data} class provides string constants for annotation-based references.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public enum DbHookFlows implements DbHookFlow<DbHookFlows> {
   INITIALIZE_H2((service, storage, args) -> DbHookFunctions.initializeH2(service)),
   QUERY_SAVE_IN_STORAGE_H2(DbHookFunctions::getFromDbSaveInStorage);

   public static final class Data {

      public static final String INITIALIZE_H2 = "INITIALIZE_H2";
      public static final String QUERY_SAVE_IN_STORAGE_H2 = "QUERY_SAVE_IN_STORAGE_H2";

      private Data() {
      }

   }

   private final TriConsumer<DatabaseService, Map<Object, Object>, String[]> flow;

   DbHookFlows(final TriConsumer<DatabaseService, Map<Object, Object>, String[]> flow) {
      this.flow = flow;
   }

   @Override
   public TriConsumer<DatabaseService, Map<Object, Object>, String[]> flow() {
      return flow;
   }

   @Override
   public DbHookFlows enumImpl() {
      return this;
   }

}
