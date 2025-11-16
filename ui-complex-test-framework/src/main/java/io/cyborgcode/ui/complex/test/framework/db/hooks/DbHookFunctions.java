package io.cyborgcode.ui.complex.test.framework.db.hooks;

import io.cyborgcode.roa.db.query.QueryResponse;
import io.cyborgcode.roa.db.service.DatabaseService;

import java.util.Map;

import static io.cyborgcode.ui.complex.test.framework.db.queries.AppQueries.QUERY_SELLER;
import static io.cyborgcode.ui.complex.test.framework.db.queries.DbSetupQueries.CREATE_TABLE_ORDERS;
import static io.cyborgcode.ui.complex.test.framework.db.queries.DbSetupQueries.CREATE_TABLE_SELLERS;
import static io.cyborgcode.ui.complex.test.framework.db.queries.DbSetupQueries.INSERT_ORDERS;
import static io.cyborgcode.ui.complex.test.framework.db.queries.DbSetupQueries.INSERT_SELLERS;

/**
 * Implementation functions for database hook operations.
 *
 * <p>This utility class provides the actual database setup/teardown logic referenced by 
 * {@link DbHookFlows} enum constants. Each method performs database operations that execute before
 * or after tests as part of the database hook lifecycle.
 *
 * <p>This class hook functions integrate with ROA {@code @DbHook} annotation to execute database operations
 * at specific test lifecycle points (BEFORE, AFTER), enabling consistent database state management
 * across the test suite.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public class DbHookFunctions {

   private DbHookFunctions() {
   }

   /**
    * Initializes the in-memory H2 database for test execution.
    * <p>
    * This method creates the required schema (orders and sellers tables)
    * and inserts seed data to establish a consistent database state before
    * running tests. It is typically invoked as part of a database hook flow
    * to ensure each test starts with a predictable dataset.
    * </p>
    *
    * @param service the {@link DatabaseService} used to execute schema and seed data queries
    */
   public static void initializeH2(DatabaseService service) {
      service.query(CREATE_TABLE_ORDERS);
      service.query(CREATE_TABLE_SELLERS);
      service.query(INSERT_ORDERS);
      service.query(INSERT_SELLERS);
   }

   /**
    * Executes a parameterized query and saves the result in shared storage.
    * <p>
    * This function queries the database table using the provided ID (from {@code arguments[0]}),
    * retrieves the result as a {@link QueryResponse}, and stores it in the given {@code storage} map
    * under a fixed key. This enables subsequent test steps to access the database query result via
    * shared storage, supporting database-driven assertions and validations.
    * </p>
    *
    * @param service  the {@link DatabaseService} used to execute the query
    * @param storage  the shared storage map for saving query results
    * @param arguments the query arguments; expects the first argument to be the seller ID
    */
   public static void getFromDbSaveInStorage(DatabaseService service, Map<Object, Object> storage, String[] arguments) {
      String id = arguments[0];
      QueryResponse sellerResponse = service.query(QUERY_SELLER.withParam("id", id));
      storage.put("SellerResponseDb", sellerResponse);
   }

}
