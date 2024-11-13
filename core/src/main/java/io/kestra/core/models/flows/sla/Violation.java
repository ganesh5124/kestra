package io.kestra.core.models.flows.sla;

public record Violation(String slaId, SLA.Behavior behavior, String reason) {
}
