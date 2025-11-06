package io.cyborgcode.ui.complex.test.framework.api;

import io.cyborgcode.roa.api.core.Endpoint;
import io.restassured.http.Method;

import java.util.List;
import java.util.Map;

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
   public Map<String, List<String>> headers() {
      return Map.of(
            "Content-Type", List.of("application/json"),
            "Accept", List.of("application/json")
      );
   }

}
