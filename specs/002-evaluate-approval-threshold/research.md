# Research and Decisions

## Exact monetary comparison

- **Decision:** use existing Java 21 BigDecimal and `compareTo`.
- **Evidence:** TransferRequest uses BigDecimal/@Digits(15,2); ledger and transfer
  services already use BigDecimal; the POM already supplies JUnit/AssertJ/Mockito.
- **Rationale:** equality must be numeric and exact without a new dependency.
- **Alternatives rejected:** double loses decimal precision; equals distinguishes
  numerically equal scales; rounding would accept invalid input.

## Explicit absence versus invalid configuration

- **Decision:** Optional empty means no applicable rule; a present immutable rule
  can hold a null threshold that evaluates to INVALID_CONFIGURATION.
- **Rationale:** the exercise explicitly distinguishes no rule from missing/invalid
  configured thresholds. No permissive fallback on corrupt data.
- **Alternative rejected:** a nullable threshold alone conflates those cases.

## Integration boundary

- **Decision:** standalone method only, as selected by the user.
- **Evidence:** TransferService has immediate posting and no pending/approval model.
- **Rationale:** connecting only a predicate would imply unsupported enforcement.
- **Alternative deferred:** source-account policy storage, lookup, transaction-time
  evaluation, and approval request lifecycle belong to later feature work.

## Validation and tests

- **Decision:** reuse the existing 15-integer/2-fractional positive amount shape;
  validate integer digits as precision minus scale, including negative scale.
- **Rationale:** exact representable bounds and no scientific-notation loophole.
- **Test approach:** pure JUnit tests and Mockito service compatibility checks
  without Spring context, database, Kafka, or Docker.
- **Environment:** Java 21 absent from PATH; a checksum-verified Microsoft portable
  JDK was provisioned under the user's local application data, outside the repo.
  Maven is supplied by the existing wrapper. No build-file changes are planned.

## Governance status

**NOT CONSULTED:** sources/owners not identified. No external position or compliance
claim is inferred. Technology choices above are justified by local repository
evidence, not labelled FIS standards. No independent research agent was needed:
the technology and scope decisions were bounded by the existing code and user answer.
