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
 * <p>
 * This utility class provides the actual database setup/teardown logic referenced by
 * {@link DbHookFlows} enum constants. Each method performs database operations that
 * execute before or after tests as part of the database hook lifecycle.
 * </p>
 * <p>
 * Key hook functions:
 * <ul>
 *   <li>{@link #initializeH2(DatabaseService)} — creates H2 in-memory database schema
 *       and populates seed data for testing. This runs before tests to establish a
 *       known database state.</li>
 *   <li>{@link #getFromDbSaveInStorage(DatabaseService, Map, String[])} — executes a
 *       query and stores results in shared storage for access during test execution.</li>
 * </ul>
 * </p>
 * <p>
 * These functions integrate with ROA's {@code @DbHook} annotation to execute database
 * operations at specific test lifecycle points (BEFORE, AFTER), enabling consistent
 * database state management across the test suite.
 * </p>
 */
public class DbHookFunctions {

    private DbHookFunctions() {
    }

    public static void initializeH2(DatabaseService service) {
        service.query(CREATE_TABLE_ORDERS);
        service.query(CREATE_TABLE_SELLERS);
        service.query(INSERT_ORDERS);
        service.query(INSERT_SELLERS);
    }

    public static void getFromDbSaveInStorage(DatabaseService service, Map<Object, Object> storage, String[] arguments) {
        String id = arguments[0];
        QueryResponse dasdas = service.query(QUERY_SELLER.withParam("id", id));
        storage.put("Something", dasdas);
    }

}
