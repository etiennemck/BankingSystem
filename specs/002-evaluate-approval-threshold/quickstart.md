# Exercise Run Guide

## Prerequisites

Java 21 and the repository's Maven wrapper. No Docker, database, application
server, policy editor, or live transfer is needed for the evaluator/unit checks.
Set JAVA_HOME to an installed Java 21 JDK before running the wrapper.

## Exercise 1 — Specification

Read [story S5-EVAL](../../docs/product/stories/S5-EVAL-account-approval-threshold-evaluation.md),
[spec.md](spec.md), and the linked PRD. Explain the >= boundary, explicit absence,
Invalid configuration result, and no-live-integration decision.

## Exercise 2 — Design and Tasks

Read [design.md](design.md), [plan.md](plan.md), and [tasks.md](tasks.md). Inspect
the listed transfer/ledger components. Explain why this slice changes neither
the account schema nor the public transfer API.

## Exercise 3 — Implement and Verify

From the repository root in PowerShell:

```powershell
cd backend
.\mvnw.cmd -B "-Dtest=ApprovalThresholdEvaluatorTest,TransferServiceCompatibilityTest" test
```

Expected results: no-rule/below → NOT_REQUIRED; equal/above → REQUIRED;
missing/invalid threshold → INVALID_CONFIGURATION; invalid amount/selection →
IllegalArgumentException; existing transfer compatibility checks pass.

Broader existing non-container unit checks:

```powershell
.\mvnw.cmd -B "-Dtest=ApprovalThresholdEvaluatorTest,TransferServiceCompatibilityTest,AmortizationCalculatorTest,RateLimitFilterTest" test
```

Container-backed regression when the existing infrastructure is available:

```powershell
.\mvnw.cmd -B "-Dtest=TransferIntegrationTest" test
```

Surefire XML/text output is under `backend/target/surefire-reports/`.
Record actual commands, environment, counts, and skipped checks in verification.md.
If a result misses the intended behavior, amend the spec or plan explicitly before
regenerating code; do not silently redefine success or weaken the tests.
