# Internal Evaluation Data Model

No persisted model or schema migration.

## AccountApprovalRule

- Immutable Java record.
- `threshold`: BigDecimal, nullable to represent corrupt/missing configured data.
- A valid threshold is **positive, at most 15 integer and 2 fractional digits**.
- Invalid thresholds are accepted into the snapshot but return INVALID_CONFIGURATION
  when evaluated; they are never treated as absent rules.
- No account ID, enabled flag, or currency: the future trusted caller selects the
  applicable account rule and same-currency values before evaluation.

## Applicable-rule selection

- `Optional<AccountApprovalRule>`: required non-null selection.
- Empty means **no applicable rule**; includes a disabled rule resolved upstream.
- Present means validate the snapshot's threshold.
- Null selection is an input error, not an empty selection.

## ApprovalDecision

Enum values: `NOT_REQUIRED`, `REQUIRED`, `INVALID_CONFIGURATION`.
No state transition, approval record, posting, or permission grant is implied.

## Transfer amount

BigDecimal, **positive, at most 15 integer and 2 fractional digits**.
Invalid amount throws IllegalArgumentException even for an empty rule selection.
Supported scientific representations retain exact value; never round.
