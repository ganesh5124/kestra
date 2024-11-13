package io.kestra.core.services;

import io.kestra.core.models.executions.Execution;
import io.kestra.core.models.flows.Flow;
import io.kestra.core.models.flows.sla.Violation;
import io.kestra.core.runners.RunContext;
import io.kestra.core.runners.RunContextFactory;
import io.kestra.core.utils.ListUtils;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;

@Singleton
public class SLAService {
    @Inject
    private RunContextFactory runContextFactory;

    public List<Violation> handleSLA(Flow flow, Execution execution) {
        RunContext runContext = runContextFactory.of(flow, execution);
        return ListUtils.emptyOnNull(flow.getSla()).stream()
            .map(
                sla -> {
                    try {
                        return sla.evaluate(runContext, execution);
                    } catch (Exception e) {
                        runContext.logger().error("Ignoring SLA '{}' because of the error : {}", sla.getId(), e.getMessage(), e);
                        return Optional.<Violation>empty();
                    }
                }
            )
            .flatMap(violation -> violation.stream())
            .peek(violation -> runContext.logger().warn("SLA '{}' violated : {}", violation.slaId(), violation.reason()))
            .toList();
    }
}
