package io.cyborgcode.ui.complex.test.framework.preconditions;

import io.cyborgcode.roa.ui.parameters.DataIntercept;


public enum BakeryInterceptRequests implements DataIntercept<BakeryInterceptRequests> {
   INTERCEPT_REQUEST_AUTH("?v-r=uidl"),
   INTERCEPT_REQUEST_LOGIN("/login");


   public static final class Data {

      public static final String INTERCEPT_REQUEST_AUTH = "INTERCEPT_REQUEST_AUTH";
      public static final String INTERCEPT_REQUEST_LOGIN = "INTERCEPT_REQUEST_LOGIN";

      private Data() {
      }
   }


   private final String endpointSubString;

   BakeryInterceptRequests(final String endpointSubString) {
      this.endpointSubString = endpointSubString;
   }

   @Override
   public String getEndpointSubString() {
      return endpointSubString;
   }

   @Override
   public BakeryInterceptRequests enumImpl() {
      return this;
   }

}
