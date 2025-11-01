package io.cyborgcode.ui.complex.test.framework.db.hooks;

import io.cyborgcode.roa.db.query.QueryResponse;
import io.cyborgcode.roa.db.service.DatabaseService;

import java.util.Map;

import static io.cyborgcode.ui.complex.test.framework.db.queries.AppQueries.QUERY_SELLER;
import static io.cyborgcode.ui.complex.test.framework.db.queries.H2Queries.CREATE_TABLE_ORDERS;
import static io.cyborgcode.ui.complex.test.framework.db.queries.H2Queries.CREATE_TABLE_SELLERS;
import static io.cyborgcode.ui.complex.test.framework.db.queries.H2Queries.INSERT_ORDERS;
import static io.cyborgcode.ui.complex.test.framework.db.queries.H2Queries.INSERT_SELLERS;

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
