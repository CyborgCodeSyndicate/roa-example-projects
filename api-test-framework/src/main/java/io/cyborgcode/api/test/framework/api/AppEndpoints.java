package io.cyborgcode.api.test.framework.api;

import io.cyborgcode.roa.api.core.Endpoint;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.specification.RequestSpecification;

import static io.cyborgcode.api.test.framework.data.constants.Headers.API_KEY_HEADER;
import static io.cyborgcode.api.test.framework.data.constants.Headers.API_KEY_VALUE;

/**
 * Enum-based definition of all API endpoints used in the example tests.
 * <p>
 * Each constant specifies the HTTP method and relative URL for a given operation and
 * implements {@link Endpoint} to integrate with the ROA
 * fluent API. A shared default configuration (JSON content type, common headers, base URL)
 * is applied to all endpoints via {@link #defaultConfiguration()}.
 * </p>
 * This centralizes endpoint metadata to keep tests consistent, discoverable, and easy to maintain.
 */
public enum AppEndpoints implements Endpoint<AppEndpoints> {

   GET_ALL_USERS(Method.GET, "/users?{page}"),
   GET_USER(Method.GET, "/users/{id}"),
   POST_CREATE_USER(Method.POST, "/users"),
   POST_LOGIN_USER(Method.POST, "/login"),
   DELETE_USER(Method.DELETE, "/users/{id}"),
   GET_ALL_RESOURCES(Method.GET, "/unknown?{page}"),
   GET_RESOURCE(Method.GET, "/unknown/{id}");

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
      spec.header(API_KEY_HEADER, API_KEY_VALUE);
      return spec;
   }

}
