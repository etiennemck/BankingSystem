# Exercise Design: Account-Level Approval-Threshold Evaluation

This is the slide's **design.md** output. [plan.md](plan.md) is the authoritative
Spec Kit technical plan; [spec.md](spec.md) owns behavior. No live enforcement.

## Existing-Code Touchpoints

- `backend/src/main/java/com/bank/transfer/web/TransferController.java` accepts
  authenticated submissions and an optional idempotency key.
- `backend/src/main/java/com/bank/transfer/dto/TransferRequest.java` constrains
  positive amounts to 15 integer/2 fractional digits.
- `backend/src/main/java/com/bank/transfer/service/TransferService.java` checks
  source ownership, active accounts, destination/currency, delegates posting,
  publishes events, and returns COMPLETED.
- `backend/src/main/java/com/bank/ledger/service/LedgerService.java` owns posting
  integrity. The evaluator must not call it.
- `backend/src/test/java/com/bank/transfer/TransferIntegrationTest.java` already
  covers actual postings, no-double-post retries, insufficient funds, and ownership.

## Evaluation Flow

1. Reject invalid transfer amount or missing rule-selection input.
2. Explicit absence of applicable rule → NOT_REQUIRED.
3. Present rule with invalid/missing threshold → INVALID_CONFIGURATION.
4. Valid amount below valid threshold → NOT_REQUIRED.
5. Valid amount equal to or above valid threshold → REQUIRED.

Inputs/outputs are immutable. No default threshold, floating-point conversion,
rounding, cache, persistence, logging, or domain events are needed.

## Model and Contract Impact

Add internal `AccountApprovalRule` and `ApprovalDecision` types plus a stateless
`ApprovalThresholdEvaluator`. No Account field, database table, migration,
REST request/response, UI, or existing service changes.

A future integration must authoritatively resolve applicability and handle
REQUIRED/INVALID_CONFIGURATION before posting; unreadable policy is not no rule.
It also needs the deferred approval lifecycle. This exercise does not implement it.

## Acceptance-to-Test Map

| Source | Verification |
|---|---|
| EVAL-AC01 | No-rule valid amount parameter cases; no default applied |
| EVAL-AC02–04 | Below/equal/above matrix and numeric scale/scientific equality |
| EVAL-AC05–06 | Null/zero/negative/precision/range threshold matrix |
| EVAL-AC07 | Null/invalid amount and null rule-selection cases |
| EVAL-AC08 | Repeated/interleaved snapshots and immutable input assertions |
| EVAL-AC09 | Existing transfer service mocked response/posting/key-forwarding and ownership-denial checks; existing transfer source unchanged |

## Governance

Architecture principles/standards/ADRs **not consulted**: applicable sources remain
unidentified. This prototype claims no governance approval. Local constitution
0.2.0 remains draft. Inspect full integration requirements before deployment.
