package io.cyborgcode.roa.usage.common.service;

import io.cyborgcode.roa.framework.annotation.Ring;
import io.cyborgcode.roa.framework.chain.FluentService;
import static io.cyborgcode.roa.usage.common.base.Rings.*;
import io.cyborgcode.roa.usage.api.ExampleEndpoints;

/**
 * Custom service for complex, multi-step workflows.
 * <p>
 * Add methods here that combine UI, API, and DB operations.
 * Example: createUserWithOrder(), setupTestEnvironment(), etc.
 * </p>
 */
@Ring("Custom")
public class CustomService extends FluentService {

    /**
     * Example method demonstrating custom flow structure.
     * <p>
     * This shows how to orchestrate multiple operations. You can:
     * - Call other rings (API, UI, DB) from within this method
     * - Execute complex multi-step business logic
     * - Return 'this' for method chaining
     * </p>
     * <p>
     * TODO: Replace this with real business workflows like:
     * - createUserWithProfile(String name, String email)
     * - completeCheckoutFlow(Order order)
     * - setupTestEnvironment(String scenario)
     * </p>
     */
    public CustomService exampleFlow() {
        // Example - replace with your operations:
        // quest.use(RING_OF_UI).getNavigation().navigate("https://example.com");
        // quest.use(RING_OF_API).request(ExampleEndpoints.EXAMPLE_POST, payload);
        // quest.use(RING_OF_DB).query(ExampleDbQueries.VERIFY_DATA);

        System.out.println("Executing example custom flow");
        return this;
    }
}
