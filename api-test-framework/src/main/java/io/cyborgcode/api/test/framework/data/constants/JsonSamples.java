package io.cyborgcode.api.test.framework.data.constants;

import java.util.Collections;
import java.util.Map;
import lombok.experimental.UtilityClass;

/**
 * Reusable JSON payload samples and helpers for response/body assertions.
 */
@UtilityClass
public class JsonSamples {

   public static Map<String, Object> emptyJson() {
      return Collections.emptyMap();
   }

}
