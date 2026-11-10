# Exercise Verification: Account-Level Approval-Threshold Evaluation

**Date:** 2026-11-09
**Result:** standalone exercise implemented and unit-verified.
**Not delivered:** live transfer integration or enforced payment approval.

## Exercise Outputs

| Exercise | Artifacts / evidence |
|---|---|
| 1: Specs | [Story](../../docs/product/stories/S5-EVAL-account-approval-threshold-evaluation.md), [spec.md](spec.md), PRD v3 links, nine acceptance criteria, explicit scope answer, Jira sync notes in the story |
| 2: Plan and tasks | [design.md](design.md), [plan.md](plan.md), [tasks.md](tasks.md), [research.md](research.md), [data-model.md](data-model.md), [analysis.md](analysis.md) |
| 3: Implement and verify | Three new production Java types, two test classes, actual Maven/Surefire results below |

The slide's 10/10/15-minute allocations were not used as a delivery SLA and were
not independently timed. All stages were executed inline using the installed
Spec Kit skill instructions and bundled scripts; no nested agent or full-cycle
CLI dispatcher was run. No extension hooks were registered.

## Environment

- Windows / PowerShell 7; Microsoft portable OpenJDK 21.0.13.0.2.
- JDK installed outside the repository under
  `%LOCALAPPDATA%\BankingSystemDemo\tools\java21\jdk-21.0.13.0.2+1`.
- Downloaded from Microsoft's documented distribution; archive SHA-256 verified:
  `c5706a9cf482ef1a0622a86eea5b13d557b07f04554572af061e2d02b58c0f99`.
- Existing Maven wrapper 3.9.16 and existing POM/dependencies, unchanged.
- Commands run from `backend`, with JAVA_HOME and process PATH pointing at that JDK.
- No Docker CLI/runtime available on PATH; no database or application server started.

## Runs and Results

| Run | Command | Exit | Result |
|---|---|---:|---|
| Baseline compile | `.\mvnw.cmd -B "-DskipTests" test-compile` | 0 | Existing backend and tests compile before new test/code files |
| Tests-first check | `.\mvnw.cmd -B "-Dtest=ApprovalThresholdEvaluatorTest,TransferServiceCompatibilityTest" test` | 1 | Expected compilation failure: evaluator/rule/decision types did not yet exist; no behavioral tests executed at this point |
| Focused green | Same focused command after implementation | 0 | 37 tests, 0 failures, 0 errors, 0 skipped |
| Broader unit regression | `.\mvnw.cmd -B "-Dtest=ApprovalThresholdEvaluatorTest,TransferServiceCompatibilityTest,AmortizationCalculatorTest,RateLimitFilterTest" test` | 0 | 42 tests, 0 failures, 0 errors, 0 skipped |

The later broad run includes the same 37 focused cases; these are not 79 distinct
tests. Of the 42 unit cases, 35 are evaluator cases, 2 are new legacy-service
compatibility cases, 2 are existing amortization cases, and 3 are existing
rate-limit cases.

Raw reports are under `backend/target/surefire-reports/`. Captured Maven logs:
`backend/target/evaluator-red.log`, `evaluator-focused.log`,
`evaluator-regression.log`. These generated files are ignored build output.
Baseline setup log remains outside the repo under
`%LOCALAPPDATA%\BankingSystemDemo\baseline-build.log`.

Warnings: Mockito/Byte Buddy emits the Java 21 dynamic-agent-loading advisory.
It did not cause a test failure. No production JVM security setting was changed
to suppress it.

## Acceptance Evidence

| Criterion | Executed evidence |
|---|---|
| EVAL-AC01 | `noApplicableRuleNeverInventsAThreshold`: five valid amounts including the supported maximum and a value above the illustrative bank-bound fixture |
| EVAL-AC02–04 | `evaluatesExactNumericBoundary`: 12 cases for below/equal/above, minimum/maximum, differing decimal scales, scientific representations, and no hard-coded 100,000.00 default/cap |
| EVAL-AC05–06 | `invalidThresholdNeverMeansNoRule`: eight cases including null, zero/negative, over-precision, representable overflow, and extreme exponent |
| EVAL-AC07 | `rejectsInvalidAmountEvenWithoutARule`: eight cases checked with absent and present rules; `missingRuleSelectionIsNotEvidenceOfNoRule`: one case |
| EVAL-AC08 | `repeatedAndInterleavedSnapshotsDoNotChangeInputsOrLeakDecisions`: repeated independent snapshots and unchanged input checks; evaluator has no mutable fields or external dependencies |
| EVAL-AC09 | Two `TransferServiceCompatibilityTest` cases verify completed response fields, balanced posting-request delegation/key forwarding, and ownership rejection before ledger/events; legacy production files unchanged |

The compatibility tests mock the ledger. They prove retained service delegation
and response behavior, not real database balances or transaction concurrency.
No stronger ledger-integration guarantee is claimed.

## Source/Artifact Checks

- Git diff checks confirmed no changes to TransferService, Account, transfer DTOs,
  LedgerService, or backend/pom.xml.
- Tracked whitespace diff check passed.
- Specification requirements-quality checklist: 16/16; document quality only.
- Pre-implementation analysis: 13 requirements/outcomes covered by 10 tasks,
  no blocking inconsistency; see analysis.md.
- Inputs, outcomes, code, tests, source story, and PRD DD-21/22 agree on absence,
  equality, invalid configuration, exact representation, and no-live-integration.
- No test expectation was weakened. The extreme-exponent tests exercise the
  existing supported-money range; they do not introduce a new business policy.

## Not Run / Remaining Limitations

- `TransferIntegrationTest` and the other container-backed integration suites
  were not run because their container infrastructure is unavailable.
- No browser/HTTP/QA scenario ran: this increment exposes no new UI or endpoint.
- No throughput/performance benchmark, mutation-testing, production deployment,
  or full approval-workflow verification is claimed.
- Architecture governance remains **not consulted**, constitution 0.2.0 remains
  **unratified**, and PRD policy remains **proposed demo policy**.
- S3's configuration story/spec are preserved as deferred work, not implemented.
- Jira sync details are documented; no issue, comment, commit, or push was created.

## Demo Narrative

1. Start with PRD FR-003 and DD-21/22; open the story and specification.
2. Show the >= boundary and the distinction between no rule and corrupt policy.
3. Open design.md and tasks.md; identify the unchanged transfer-posting boundary.
4. Run the focused command and show all 37 checks passing.
5. Explain that REQUIRED is a decision result, not a pending-payment workflow.
6. For any future behavioral change, amend spec/plan first, regenerate code/tasks,
   rerun tests, and preserve traceability rather than hand-editing around the spec.
