# Implementation Plan: Standalone Approval-Threshold Evaluation

**Branch**: `main` (unchanged) | **Date**: 2026-11-09 | **Spec**: [spec.md](spec.md)
**Input**: S5-EVAL, PRD version 3 DD-21/22, and explicit user selection of standalone scope.

## Summary

Add one stateless Java evaluator and two immutable domain types to the transfer
module. Evaluate an explicit optional applicable-rule snapshot using exact
BigDecimal comparison. Keep all current Spring services, endpoints, data models,
migrations, and ledger behavior untouched. Unit-test the evaluator and add
Mockito compatibility tests around the existing TransferService.

## Technical Context

**Language/Version**: Java 21, as configured in `backend/pom.xml`.
**Primary Dependencies**: Existing JUnit Jupiter, AssertJ, Mockito; no additions.
**Storage**: None for the evaluator; no schema or migration changes.
**Testing**: Maven wrapper/Surefire; pure unit and mocked service compatibility checks.
**Target Platform**: Existing JVM backend; Windows PowerShell demonstration.
**Project Type**: Internal component within existing Spring Boot application.
**Performance Goals**: Deterministic in-memory validation/comparison; no network
or storage calls. No production latency or throughput claim.
**Constraints**: Positive amounts, <=15 integer and <=2 fractional digits,
exact numeric equality, no default, typed invalid-configuration outcome,
no live integration, and no mutable state.
**Scale/Scope**: One method, one rule record, one outcome enum, two test classes.

## Constitution Check

| Guardrail | Pre-research | Post-design |
|---|---|---|
| Source authority | Pass: demo PRD/story cited; no approval inferred | Pass: proposed decisions retained |
| Payment integrity / compatibility | Pass: evaluator-only scope | Pass: TransferService unchanged; compatibility tests planned |
| Scoped authorization | Pass: no new entry point or grants | Pass: existing ownership checks preserved |
| Durable configuration | Not applicable: no configuration mutation | Not applicable: no audit/policy persistence added |
| Traceable verification | Pass: 9 source criteria, explicit test tasks | Pass: mapping in design.md/tasks.md |
| Enterprise/portfolio governance | NOT CONSULTED: sources not identified | NOT CONSULTED; not an approved architecture plan |
| Constitution adoption | Draft 0.2.0, not ratified | Same limitation; no human sign-off asserted |

This is a local prototype design, not a production governance gate pass. The user
authorized exercise alignment and selected standalone implementation. The earlier
choice to proceed with governance not consulted remains recorded; identify owners
and consult standards/ADRs before production or live integration.

## Project Structure

### Documentation (this feature)

```text
specs/002-evaluate-approval-threshold/
  spec.md
  plan.md
  design.md
  research.md
  data-model.md
  quickstart.md
  tasks.md
  analysis.md
  verification.md
  checklists/requirements.md
```

`design.md` is the exercise-facing design summary; this file is the Spec Kit
technical plan. `tasks.md` follows the task stage, not the plan stage.

### Source Code (repository root)

```text
backend/src/main/java/com/bank/transfer/
  domain/AccountApprovalRule.java
  domain/ApprovalDecision.java
  service/ApprovalThresholdEvaluator.java
backend/src/test/java/com/bank/transfer/
  ApprovalThresholdEvaluatorTest.java
  TransferServiceCompatibilityTest.java
```

**Structure Decision**: Follow existing package ownership and Java/JUnit style.
Use a plain final evaluator, not an injectable Spring bean, because no runtime
service invokes it in this increment. No public HTTP contracts or contracts/
folder are needed for this internal-only component.

## Implementation Decisions

- Method: `ApprovalDecision evaluate(BigDecimal amount, Optional<AccountApprovalRule> rule)`.
- `Optional.empty()` is explicitly no applicable rule. A null Optional is invalid input.
- `AccountApprovalRule(BigDecimal threshold)` is an immutable input snapshot.
  A nullable threshold intentionally models invalid stored configuration for evaluation.
- Outcomes: `NOT_REQUIRED`, `REQUIRED`, `INVALID_CONFIGURATION`.
- Reject invalid amount/selection with `IllegalArgumentException` before comparison.
- Validate rule threshold with the same positive/15-integer/2-fractional
  representation; invalid threshold returns INVALID_CONFIGURATION.
- Compare valid values using `BigDecimal.compareTo`, never floating point or equals.
- No account IDs in the pure component: caller selects the account policy before
  evaluation. No active/staged inference, bank-bound lookup, or ignored lookup failures.

## Verification Strategy

Write executable tests first, then implementation. Test no rule across valid
amount ranges; below/equal/above; scale/scientific representations; smallest/largest
values; missing/invalid rule and amount; repeat/interleaved evaluations.
Add mocked legacy service checks for the completed response/posting contract,
unchanged idempotency-key forwarding, and ownership denial before posting.
These checks are not a substitute for database-backed ledger integration tests.

Focused command from backend:

```powershell
.\mvnw.cmd -B "-Dtest=ApprovalThresholdEvaluatorTest,TransferServiceCompatibilityTest" test
```

Broader non-container unit command additionally includes existing calculator and
rate-limit tests; document exact commands/results in verification.md. Existing
TransferIntegrationTest requires container-backed infrastructure and must be
reported as unrun if unavailable. Do not change the POM or weaken tests to force a pass.

## Complexity Tracking

No local guardrail violation requires a complexity exception. The external
governance review is an open limitation, not a resolved exception or corpus-silence finding.
