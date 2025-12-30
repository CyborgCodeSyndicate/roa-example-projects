package io.cyborgcode.roa.usage.api;

import io.cyborgcode.roa.api.core.Endpoint;
import io.restassured.http.Method;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public enum Endpoints implements Endpoint<Endpoints> {

    GET_ALL_USERS(Method.GET, "/users"),
    GET_USER(Method.GET, "/users/{id}"),
    POST_CREATE_USER(Method.POST, "/users"),
    UPDATE_USER(Method.PUT, "/users/{id}"),
    DELETE_USER(Method.DELETE, "/users/{id}");

    private final Method method;
    private final String url;

    Endpoints(Method method, String url) {
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
    public Endpoints enumImpl() {
        return this;
    }

    @Override
    public RequestSpecification defaultConfiguration() {
        return Endpoint.super.defaultConfiguration()
                .contentType(ContentType.JSON)
                .header("x-api-key", "reqres_4e24d3aef2cf4378a2bb06828bd8993b");
    }

}
