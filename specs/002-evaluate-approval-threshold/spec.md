# Feature Specification: Account-Level Approval-Threshold Evaluation

**Feature Branch**: `main` (unchanged; no Git extension)
**Feature Directory**: `specs/002-evaluate-approval-threshold`
**Created**: 2026-11-09
**Status**: Draft demo specification, not approved bank policy
**Input**: [S5-EVAL story](../../docs/product/stories/S5-EVAL-account-approval-threshold-evaluation.md)
and [PRD version 3](../../docs/product/dbb-prd-transaction-limits-approval-controls.md),
FR-003–006/017, NFR-3/4, DD-21/22, plus the user's supplied hands-on exercise.

This replaces S3 as the selected exercise, not as configuration backlog scope.
The user explicitly selected a standalone evaluator and unit tests. Live transfer
submission integration is excluded. Local FR-* IDs below are not PRD FR-* IDs.

## Clarifications

### Session 2026-11-09

- Q: Should the exercise build only the evaluator or also connect it to live transfer submission? → A: Standalone evaluator and unit tests, as selected by the user.
- Boundary equality, absent versus corrupt policy, exact money representation,
  and the absence of a default are documented demo decisions in PRD DD-21/22;
  they are not recorded as additional user approvals.

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Determine whether approval is needed (Priority: P1)

As a Payment Initiator, I want my transfer amount evaluated against my source
account's applicable threshold, so proportional authorization can be applied
consistently without imposing a rule where none applies.

**Why this priority**: This is the exact decision capability in the exercise.
It is useful and testable independently of policy administration and approval UI.

**Independent Test**: Supply synthetic amounts and applicable-rule snapshots,
observe the decision/error, and verify all nine source acceptance criteria.
No database, payment execution, or deployed approval workflow is needed.

**Acceptance Scenarios**:

1. **Given** a valid amount and explicitly no applicable rule, **when** evaluated,
   **then** return Approval not required without inventing a default. (EVAL-AC01)
2. **Given** threshold 100.00, **when** evaluating 99.99, 100.00, or 100.01,
   **then** return respectively Approval not required, Approval required,
   and Approval required. Numerically equal representations such as 100 and
   100.00 give the same decision. (EVAL-AC02–04)
3. **Given** a present rule whose threshold is missing, zero, negative,
   over-precision, or outside supported money representation, **when** evaluated,
   **then** return Invalid configuration, never straight-through permission.
   (EVAL-AC05–06)
4. **Given** invalid amount input or missing rule-selection input, **when**
   evaluating, **then** reject invalid input rather than returning a permissive
   decision, even when no rule is applicable. (EVAL-AC07)
5. **Given** repeated evaluations and snapshots for different accounts, **when**
   evaluated in any order, **then** each result depends only on its inputs and
   changes no state. (EVAL-AC08)
6. **Given** existing legacy transfer processing, **when** exercising successful
   posting delegation and ownership denial, **then** retain existing behavior,
   successful response fields, and authorization checks. (EVAL-AC09)

### Edge Cases

- A present rule with no threshold is distinct from explicitly absent policy.
- An explicitly disabled rule is resolved by a future authoritative adapter into
  no applicable rule; policy-read failure must never be mapped to absence.
- Lowest representable positive amount: 0.01. Largest supported amount:
  999,999,999,999,999.99, from the current 15-integer/2-fractional-digit contract.
- Supplied values with more than two fractional decimal places are invalid even
  if the extra digits are zeros. No normalization rounds invalid input into validity.
- Negative-scale numeric representations are allowed if the represented amount
  fits the same integer-digit limit; numeric equality is representation-independent.
- 0, negative amounts, missing amounts, and missing rule-selection input are errors.
- Rule thresholds outside supported money representation are configuration errors.
  The evaluator introduces no separate universal threshold or hard payment cap.
- Repeated and interleaved calls must not cache or leak account decisions.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: Accept a transfer amount and an explicit selection of either no
  applicable source-account rule or a present rule snapshot.
- **FR-002**: Reject invalid amount input before producing a decision: amounts must
  be positive with at most 15 integer and 2 fractional decimal digits.
- **FR-003**: For a valid amount with no applicable rule, return Approval not required.
- **FR-004**: For a present valid rule, compare numerically and return Approval
  required if and only if the amount is at least the threshold.
- **FR-005**: For a present rule with missing/nonpositive/over-precision/out-of-range
  threshold, return Invalid configuration as a distinct nonpermissive outcome.
- **FR-006**: Reject missing rule-selection input; it is not proof that no rule applies.
- **FR-007**: Never round monetary inputs, invent thresholds, or hard-code the
  bank-bound fixture of 100,000.00 as a universal evaluator limit.
- **FR-008**: Return deterministic results without persistence, posting,
  network calls, account mutations, audit mutations, or retained per-account state.
- **FR-009**: Leave existing transfer submission, ownership checks, successful
  response contract, and posting delegation unchanged; do not wire in this increment.

### Key Entities *(include if feature involves data)*

- **Transfer amount**: positive money value in the source account's currency.
- **Applicable rule selection**: explicit absence or a present rule snapshot;
  present-but-missing threshold represents corrupt configuration.
- **Approval threshold**: positive, representable money value in the same currency.
- **Evaluation outcome**: Approval required, Approval not required, or
  Invalid configuration. None of these outcomes moves funds.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: From baseline 0 standalone transfer-threshold evaluators, demonstrate
  1 component with all 9 source acceptance criteria covered by passing unit checks.
- **SC-002**: All below/equal/above, absent rule, missing threshold, malformed
  money representation, repeated-call, and no-rule compatibility cases produce
  their specified outcomes; zero invalid policies are treated as no rule.
- **SC-003**: Evaluation causes zero state mutations, postings, or external calls;
  existing transfer source and public contracts have zero changes.
- **SC-004**: Publish the exercise artifacts spec.md, design.md, tasks.md, and
  executable unit-test results; report unrun integration checks explicitly.

## Assumptions

- Proposed demo policies only; no bank, risk, regulatory, or commercial approval.
- The future trusted caller resolves applicability and supplies same-currency
  values. Authentication, active/staged enrollment, rule lookup, bank-bound
  validation, and policy version selection are outside this pure component.
- No UI, schema migration, REST endpoint, new dependency, pending request,
  approval/rejection action, reservation, audit event, or activation is delivered.
- Existing behavior is preserved by leaving transfer execution unchanged and
  adding focused compatibility checks, not by claiming a live approval control.
- The 10/10/15-minute slide allocations are teaching guidance, not measured
  elapsed-time targets or commitments.
- S3 and its configuration spec remain intact as separate deferred work.
- Jira synchronization is documented in the story; no external issue is created.

### Architecture Alignment

**NOT CONSULTED.** Applicable enterprise/portfolio governance sources remain
unidentified. The user previously chose to continue this local demo with that
limitation. The [draft constitution](../../.specify/memory/constitution.md),
version 0.2.0, supplies local guardrails only and remains unratified. No approved
alignment, corpus silence, or policy exception is claimed.
