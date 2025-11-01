package io.cyborgcode.ui.complex.test.framework.db.hooks;

import io.cyborgcode.roa.db.hooks.DbHookFlow;
import io.cyborgcode.roa.db.service.DatabaseService;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.Map;

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
