package io.cyborgcode.ui.complex.test.framework.ui.interceptor;

import io.cyborgcode.roa.ui.parameters.DataIntercept;

/**
 * Registry of network request interception patterns for capturing UI traffic.
 * <p>
 * Each enum constant defines a URL substring pattern used to intercept and capture
 * specific network requests made by the Vaadin UI. This enables tests to extract
 * runtime data from AJAX responses, validate backend communication, and create
 * context-aware test data.
 * </p>
 * <p>
 * Available interception patterns:
 * <ul>
 *   <li>{@link #INTERCEPT_REQUEST_AUTH} — captures Vaadin UIDL (UI Definition Language) requests</li>
 *   <li>{@link #INTERCEPT_REQUEST_LOGIN} — captures login endpoint requests</li>
 * </ul>
 * </p>
 * <p>
 * These interceptors integrate with ROA's {@code @Intercept} annotation and are used
 * in conjunction with {@link io.cyborgcode.ui.complex.test.framework.data.extractor.DataExtractorFunctions}
 * to parse response bodies and extract values for assertions or late-bound data creation.
 * </p>
 * <p>
 * The nested {@link Data} class provides string constants for annotation-based references.
 * </p>
 */
public enum RequestsInterceptor implements DataIntercept<RequestsInterceptor> {

   INTERCEPT_REQUEST_AUTH("?v-r=uidl"),
   INTERCEPT_REQUEST_LOGIN("/login");

   public static final class Data {

      public static final String INTERCEPT_REQUEST_AUTH = "INTERCEPT_REQUEST_AUTH";
      public static final String INTERCEPT_REQUEST_LOGIN = "INTERCEPT_REQUEST_LOGIN";

      private Data() {
      }

   }

   private final String endpointSubString;

   RequestsInterceptor(final String endpointSubString) {
      this.endpointSubString = endpointSubString;
   }

   @Override
   public String getEndpointSubString() {
      return endpointSubString;
   }

   @Override
   public RequestsInterceptor enumImpl() {
      return this;
   }

}
