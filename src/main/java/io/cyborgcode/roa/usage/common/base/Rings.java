package io.cyborgcode.roa.usage.common.base;


import io.cyborgcode.roa.api.service.fluent.RestServiceFluent;
import io.cyborgcode.roa.usage.common.service.UserService;
import lombok.experimental.UtilityClass;


@UtilityClass
public class Rings {

    public static final Class<RestServiceFluent> RING_OF_API = RestServiceFluent.class;
    public static final Class<UserService> RING_OF_USER = UserService.class;

}
