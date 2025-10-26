package io.cyborgcode.api.test.framework.preconditions;

import io.cyborgcode.roa.framework.parameters.PreQuestJourney;
import io.cyborgcode.roa.framework.quest.SuperQuest;
import java.util.function.BiConsumer;

public enum Preconditions implements PreQuestJourney<Preconditions> {

   CREATE_NEW_USER(PreconditionFunctions::createNewUser);

   public static final class Data {

      private Data() {
      }

      public static final String CREATE_NEW_USER = "CREATE_NEW_USER";

   }

   private final BiConsumer<SuperQuest, Object[]> function;

   Preconditions(final BiConsumer<SuperQuest, Object[]> function) {
      this.function = function;
   }

   @Override
   public BiConsumer<SuperQuest, Object[]> journey() {
      return function;
   }

   @Override
   public Preconditions enumImpl() {
      return this;
   }

}
