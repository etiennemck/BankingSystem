# S5-EVAL — Evaluate an account's transfer approval threshold

**Status:** DRAFT demo story, selected for the hands-on exercise on 2026-11-09.
**Parent capability:** S5 governed transfer submission; this is only its pure
evaluation slice, not a replacement for submission/approval workflow delivery.
**Source:** [PRD version 3](../dbb-prd-transaction-limits-approval-controls.md),
FR-003–006, FR-017, NFR-3/4, DD-04, DD-21/22.
**Specification:** [002 evaluator spec](../../../specs/002-evaluate-approval-threshold/spec.md).
No Jira epic/key supplied or issue created.

## User Story

As a Payment Initiator, I want the source account's applicable approval threshold
evaluated consistently, so higher-value transfers can be identified for approval
while accounts without an applicable rule retain existing behavior.

## Exercise Boundary

The user explicitly selected **standalone evaluator and unit tests**, not live
transfer integration. Supply a synthetic authoritative rule snapshot to the
evaluator; do not pretend a request header or browser value is authoritative.
There is no new API, persistence, company model, UI, pending request, approval
action, posting behavior, audit mutation, or background task.

The result is a reusable decision component. Live transfers still execute as
before; this increment does not enforce controls. A future adapter must distinguish
disabled/absent rules from unreadable or corrupt enabled policy.

## Acceptance Criteria

| ID | Given / when / then |
|---|---|
| EVAL-AC01 | Given a valid transfer amount and no applicable rule, when evaluated, then return Approval not required without a default threshold. |
| EVAL-AC02 | Given a valid applicable threshold, when the amount is below it, then return Approval not required. |
| EVAL-AC03 | Given a valid applicable threshold, when the amount numerically equals it, then return Approval required, regardless of decimal scale. |
| EVAL-AC04 | Given a valid applicable threshold, when the amount exceeds it, then return Approval required. |
| EVAL-AC05 | Given a present rule with no threshold, when evaluated, then return Invalid configuration, not Approval not required. |
| EVAL-AC06 | Given a present rule with zero, negative, excessive precision, or an amount outside the supported money representation, when evaluated, then return Invalid configuration without rounding. |
| EVAL-AC07 | Given a null/nonpositive/over-precision/out-of-range transfer amount, when evaluated, then reject invalid input even if no rule applies. Missing rule-selection input is also rejected rather than treated as no rule. |
| EVAL-AC08 | Given repeated calls or different account snapshots, when evaluated, then results are deterministic, independent, and have no side effects. |
| EVAL-AC09 | Given the existing transfer service, when a legacy transfer is submitted, then its posting delegation, successful response, and ownership checks remain unchanged; the evaluator is not wired into the service. |

## Clarified Demo Decisions

- Equality requires approval (`amount >= threshold`), consistent with PRD FR-003.
- No applicable rule is explicit absence. An enabled rule missing its threshold
  is invalid configuration, not absence. No threshold is invented.
- Invalid configuration is a separate result; no result is an instruction to post.
- Money inputs use the existing request representation: positive, at most
  15 integer and 2 fractional decimal digits. Compare exactly; never round.
- Bank bounds are validated by future configuration work, not hard-coded here.
- Currency/context and authoritative rule lookup are future caller responsibilities.

## Success and Verification

Baseline: 0 standalone approval-threshold evaluators in the inspected transfer
module. Target: 1 reusable component, all 9 acceptance criteria covered, zero
configuration evaluation side effects, and zero existing transfer code changes.
Run real Java unit tests and report exact test results; documentation is not
evidence that the implementation passes.

## What to Sync to Jira Later

If publication is requested, use a supplied parent epic, preview before creating,
and carry EVAL-AC01–09, the no-live-integration boundary, proposed demo policy
status, missing/invalid configuration decisions, and the spec link. No Jira writes
are authorized by preparing this story.
