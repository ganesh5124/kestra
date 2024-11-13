package io.kestra.core.models.flows.sla;

import io.kestra.core.exceptions.InternalException;
import io.kestra.core.models.executions.Execution;
import io.kestra.core.runners.RunContext;
import io.kestra.core.utils.TruthUtils;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Optional;

@SuperBuilder
@Getter
@NoArgsConstructor
public class ExecutionConditionSLA extends SLA {
    @NotNull
    @NotEmpty
    private String condition;

    @Override
    public Optional<Violation> evaluate(RunContext runContext, Execution execution) throws InternalException {
        String result = runContext.render(this.getCondition());
        if (TruthUtils.isTruthy(result)) {
            String reason = "execution condition violation: " + this.getCondition() + ".";
            return Optional.of(new Violation(this.getId(), this.getBehavior(), reason));
        }

        return Optional.empty();
    }
}
