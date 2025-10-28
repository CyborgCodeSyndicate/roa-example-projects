package io.cyborgcode.ui.complex.test.framework.db.hooks;

import io.cyborgcode.roa.db.hooks.DbHookFlow;
import io.cyborgcode.roa.db.query.QueryResponse;
import io.cyborgcode.roa.db.service.DatabaseService;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.Map;

import static io.cyborgcode.ui.complex.test.framework.db.Queries.QUERY_SELLER;
import static io.cyborgcode.ui.complex.test.framework.db.hooks.QueriesH2.*;

public enum DbHookFlows implements DbHookFlow {

   INITIALIZE_H2(DbHookFlows::initializeH2),
   QUERY_SAVE_IN_STORAGE_H2(DbHookFlows::getFromDbSaveInStorage);


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
   public Enum<?> enumImpl() {
      return this;
   }


   public static void initializeH2(DatabaseService service, Map<Object, Object> storage, String[] arguments) {
      service.query(CREATE_TABLE_ORDERS);
      service.query(CREATE_TABLE_SELLERS);
      service.query(INSERT_ORDERS);
      service.query(INSERT_SELLERS);
   }


   public static void getFromDbSaveInStorage(DatabaseService service, Map<Object, Object> storage, String[] arguments) {
      String id = arguments[0];
      QueryResponse dasdas = service.query(QUERY_SELLER.withParam("dasdas", id));
      storage.put("Something", dasdas);
   }

}
