package io.kestra.core.models.flows.sla;

import io.kestra.core.exceptions.InternalException;
import io.kestra.core.models.executions.Execution;
import io.kestra.core.runners.RunContext;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@SuperBuilder
@Getter
@NoArgsConstructor
public class MaxDurationSLA extends SLA {
    @NotNull
    private Duration duration;

    @Override
    public Optional<Violation> evaluate(RunContext runContext, Execution execution) throws InternalException {
        Duration executionDuration = Duration.between(execution.getState().getStartDate(), Instant.now());
        if (executionDuration.compareTo(this.getDuration()) > 0) {
            String reason = "execution duration of " + executionDuration.truncatedTo(ChronoUnit.MILLIS) + " exceed the maximum duration of " + this.getDuration() + ".";
            return Optional.of(new Violation(this.getId(), this.getBehavior(), reason));
        }

        return Optional.empty();
    }
}
