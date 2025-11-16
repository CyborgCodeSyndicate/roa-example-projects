package io.cyborgcode.ui.complex.test.framework.api;

import io.cyborgcode.roa.api.core.Endpoint;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.specification.RequestSpecification;

import java.util.List;
import java.util.Map;

/**
 * Enum-based definition of all API endpoints used in the test application.
 * <p>
 * Each constant specifies the HTTP method and relative URL for a given operation and
 * implements {@link Endpoint} to integrate with the ROA fluent API. Endpoints support
 * hybrid testing scenarios where UI tests need to verify backend state or perform
 * API-based authentication and data validation.
 * </p>
 * <p>
 *A shared default configuration (JSON content type, common headers, base URL)
 * is applied to all endpoints via {@link #defaultConfiguration()}.
 * </p>
 * This centralizes endpoint metadata to keep tests consistent, discoverable, and easy to maintain.
 */
public enum AppEndpoints implements Endpoint<AppEndpoints> {

   ENDPOINT_BAKERY(Method.POST, "/storefront"),
   ENDPOINT_BAKERY_GET(Method.GET, "/"),
   ENDPOINT_BAKERY_LOGIN(Method.POST, "/login");

   private final Method method;
   private final String url;

   AppEndpoints(final Method method, final String url) {
      this.method = method;
      this.url = url;
   }

   @Override
   public Method method() {
      return method;
   }

   @Override
   public String url() {
      return url;
   }

   @Override
   public AppEndpoints enumImpl() {
      return this;
   }

   @Override
   public RequestSpecification defaultConfiguration() {
      RequestSpecification spec = Endpoint.super.defaultConfiguration();
      spec.contentType(ContentType.JSON);
      spec.header("header-key", "header-value");
      return spec;
   }

   @Override
   public Map<String, List<String>> headers() {
      return Map.of(
            "Content-Type", List.of("application/json"),
            "Accept", List.of("application/json")
      );
   }

}
