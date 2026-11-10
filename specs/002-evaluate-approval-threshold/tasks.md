# Tasks: Account-Level Approval-Threshold Evaluation

**Input:** [spec.md](spec.md), [plan.md](plan.md), [design.md](design.md),
[research.md](research.md), [data-model.md](data-model.md), [quickstart.md](quickstart.md).
**Tests:** explicitly required by the exercise and user-selected standalone scope.
**Organization:** one P1 user story, with setup and cross-cutting evidence tasks.
No Jira issues, commits, or deployment tasks are authorized.

## Phase 1: Setup

- [x] T001 Verify portable Java 21/Maven wrapper and baseline compilation using `backend/mvnw.cmd`; record environment and result in `specs/002-evaluate-approval-threshold/verification.md`.

## Phase 2: Foundational

- [x] T002 Confirm no live integration and compare the nine source acceptance criteria with `specs/002-evaluate-approval-threshold/design.md` and `backend/src/main/java/com/bank/transfer/service/TransferService.java`.

No new database, authorization framework, migrations, or external service setup.

## Phase 3: US1 — Determine Approval Need (Priority: P1)

**Goal:** evaluate explicit rule snapshots exactly, distinguish corruption from
absence, and retain legacy transfer behavior without wiring the component in.
**Independent test:** pure evaluator matrix plus mocked legacy service checks.

### Tests first

- [x] T003 [P] [US1] Write `backend/src/test/java/com/bank/transfer/ApprovalThresholdEvaluatorTest.java` for EVAL-AC01–08: absent/below/equal/above, missing/invalid policy, "positive, at most 15 integer and 2 fractional digits", scientific-scale boundaries, invalid amount/selection, and repeat/interleaved evaluations; capture expected pre-implementation failure.
- [x] T004 [P] [US1] Write `backend/src/test/java/com/bank/transfer/TransferServiceCompatibilityTest.java` for EVAL-AC09: completed response/posting contract, idempotency-key forwarding, and ownership denial without posting.

### Implementation

- [x] T005 [P] [US1] Add immutable `backend/src/main/java/com/bank/transfer/domain/AccountApprovalRule.java` with "threshold: BigDecimal, nullable to represent corrupt/missing configured data"; do not validate in the record constructor.
- [x] T006 [P] [US1] Add `backend/src/main/java/com/bank/transfer/domain/ApprovalDecision.java` with exactly `NOT_REQUIRED`, `REQUIRED`, `INVALID_CONFIGURATION`.
- [x] T007 [US1] Add stateless `backend/src/main/java/com/bank/transfer/service/ApprovalThresholdEvaluator.java`: non-null Optional selection, explicit empty means no rule, "positive, at most 15 integer and 2 fractional digits", invalid amount/selection throws, invalid threshold returns INVALID_CONFIGURATION, compareTo >= 0 means REQUIRED; no rounding/default/side effects.
- [x] T008 [US1] Run focused evaluator and compatibility tests through `backend/mvnw.cmd`; inspect `backend/target/surefire-reports/` and fix against the spec/plan, not by weakening expected outcomes.

## Phase 4: Polish and Cross-Cutting Verification

- [x] T009 Run the broader existing non-container unit selection from `specs/002-evaluate-approval-threshold/quickstart.md`; check unchanged `backend/src/main/java/com/bank/transfer/service/TransferService.java` and report any unavailable container-backed regression.
- [x] T010 Publish actual commands/counts, acceptance-to-test mapping, exercise outputs, analysis summary, skipped checks, and governance/no-live-enforcement limitations in `specs/002-evaluate-approval-threshold/verification.md`.

## Dependencies and Execution Order

T001 → T002 → T003/T004 → expected red check → T005/T006 → T007 → T008 → T009 → T010.
T003/T004 can be authored in parallel after scope confirmation; T005/T006 can be
authored independently after test contracts are fixed. Execute inline for this
small exercise rather than introducing coordination overhead.

## Requirement Coverage

| Spec requirement | Tasks |
|---|---|
| FR-001 | T003, T005, T007 |
| FR-002 | T003, T007 |
| FR-003 | T003, T007 |
| FR-004 | T003, T007 |
| FR-005 | T003, T005, T006, T007 |
| FR-006 | T003, T007 |
| FR-007 | T003, T007 |
| FR-008 | T003, T005, T006, T007 |
| FR-009 | T004, T009 |

## Implementation Strategy

One story is the complete exercise MVP. Finish all its tests and evidence before
claiming completion. Models are internal inputs/outputs; no endpoint/integration
task is needed. If the observed result contradicts intended behavior, revise
spec.md or plan.md explicitly before regenerating code and record the change.
Only mark task completion when its work is done and verified; test completion
does not grant human approval or waive unconsulted architecture governance.
