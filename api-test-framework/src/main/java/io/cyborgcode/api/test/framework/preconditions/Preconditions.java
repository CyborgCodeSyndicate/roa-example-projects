package io.cyborgcode.api.test.framework.preconditions;

import io.cyborgcode.roa.framework.annotation.PreQuest;
import io.cyborgcode.roa.framework.parameters.PreQuestJourney;
import io.cyborgcode.roa.framework.quest.SuperQuest;
import java.util.function.BiConsumer;

/**
 * Registry of reusable pre-test journeys for the Reqres tutorial suite.
 * <p>
 * Each enum constant represents a named precondition that can be referenced from
 * {@link PreQuest @PreQuest} annotations.
 * The underlying {@link BiConsumer} receives the active {@link SuperQuest} and any
 * journey data, and is responsible for preparing the required state (e.g. creating users)
 * before the actual test method executes.
 * <p>
 * Centralizing preconditions here keeps setup logic:
 * <ul>
 *   <li>discoverable and type-safe,</li>
 *   <li>reusable across multiple tests,</li>
 *   <li>aligned with the ROA {@link PreQuestJourney} contract.</li>
 * </ul>
 */
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
