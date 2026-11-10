# Feature Specification: Staged Account Approval Threshold Configuration

**Feature Branch**: `main` (unchanged; optional Git extension not installed)

**Feature Directory**: `specs/001-account-approval-threshold`

**Created**: 2026-11-09

**Status**: Draft demonstration specification; not approved for production

**Input**: User request to use spec-driven-development on
[story S3](../../docs/product/stories/S3-configure-account-approval-threshold.md),
derived from [PRD version 2](../../docs/product/dbb-prd-transaction-limits-approval-controls.md).
Use staged configuration only; preserve the proposed demo decisions and all 17
source acceptance scenarios. Do not implement transfer enforcement or activation.

**ID convention**: FR-* and SC-* below are local to this specification. References
to program requirements are explicitly labelled PRD FR-*; S3-AC* are stable source
story scenario IDs.

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Prepare and safely maintain an account policy (Priority: P1)

As a Company Administrator, I want to save different account approval thresholds
within bank bounds, so I can prepare and review proportional controls before
they are activated.

**Why this priority**: This is S3's first usable configuration increment. Accurate
draft status, authorization, version checks, and durable evidence are mandatory
parts of this journey, not optional later hardening.

**Independent Test**: With two STAGED companies, dedicated accounts, configured bank
bounds, and authenticated administrator/member/auditor personas, demonstrate
configuration, reload, validation, conflicts, denials, and unchanged money movement.
No live approval workflow or other payment-control story is needed.

**Acceptance Scenarios**:

1. **Given** Admin A belongs to STAGED Company A, **when** opening its account
   configuration, **then** show only linked company accounts, currency, bank
   bounds/version, policy state/version, last actor/time where present, and a
   persistent **Draft — not enforced** warning. (S3-AC01)
2. **Given** an unconfigured account, **when** viewing it, **then** show an empty
   threshold and Unconfigured state without creating a policy or change record.
   (S3-AC02)
3. **Given** current versions, **when** Admin A enables a 10,000.00 USD threshold,
   **then** save policy version 1, confirm success, and preserve the value/version
   after reload. Saving 25,000.00 on another account leaves the first unchanged.
   These are deliberate example entries, not defaults. (S3-AC03, S3-AC04)
4. **Given** inclusive demo bounds of 0.01–100,000.00 USD, **when** entering either
   endpoint, **then** permit the save; **when** entering an invalid amount,
   **then** show a useful error and preserve existing state. (S3-AC05)
5. **Given** a valid enabled policy and permission to disable rules, **when** the
   administrator explicitly disables it, **then** save a null effective threshold,
   advance its version once, retain the former value in evidence, and require an
   explicit amount to re-enable it. (S3-AC06)
6. **Given** a newer policy or bank-bounds version exists than the editor read,
   **when** saving, **then** refuse with a reload-required conflict, preserve the
   newer policy, and record no successful change for the rejected save.
   (S3-AC07, S3-AC08)
7. **Given** a non-admin member, another company's admin, expired authentication,
   or revoked membership, **when** attempting direct access or a save, **then**
   enforce current rights, disclose no other company's information, and make no
   change. Scoped auditors can read but never save; bank administration alone
   grants no company policy-writing right. (S3-AC09, S3-AC10, S3-AC11)
8. **Given** current versions and unchanged policy values, **when** saving again,
   **then** confirm unchanged success without a new version or change record.
   A stale retry still conflicts. (S3-AC14)
9. **Given** any saved draft configuration, **when** reloading or reviewing it,
   **then** retain the warning, provide no usable activation control, and leave
   balances, ownership, ledger entries, and existing transfers unchanged.
   (S3-AC15)

---

### User Story 2 - Review trustworthy policy-change evidence (Priority: P2)

As a Company Administrator, scoped Auditor, or Bank Administrator, I want to
inspect policy changes within my read scope, so I can explain who changed what
without acquiring policy-writing authority.

**Why this priority**: Reviewable evidence makes the saved controls explainable.
P2 denotes demonstration sequencing, not optional security or audit delivery;
the complete S3 increment requires both journeys.

**Independent Test**: Using synthetic committed policy changes, authorized
company and bank readers can inspect before/after evidence. Verify denied reads
for another company and denied writes for read-only reviewers. Evidence review
can be tested without submitting or approving any transfer.

**Acceptance Scenarios**:

1. **Given** a successful policy mutation, **when** an entitled reader reviews its
   evidence, **then** within 5 seconds they can identify actor, company/account,
   before/after values, policy/bounds versions, UTC time, and correlation ID,
   with exactly one change record for that mutation. (S3-AC12)
2. **Given** durable audit recording is unavailable, **when** a policy save is
   attempted, **then** refuse the save with retry guidance; policy, version,
   and successful-change evidence remain unchanged. (S3-AC13)
3. **Given** rejected validation, a stale save, a no-op save, or an ordinary read,
   **when** reviewing change history, **then** none is represented as a successful
   policy change. Denied attempts are security evidence, not successful changes.
   (S3-AC02, S3-AC05, S3-AC07, S3-AC14)

### Edge Cases

- No bank bounds or unavailable bounds: display unavailability, block saves,
  and never invent fallback values. A prohibition on rule disabling must be
  enforced independently of hidden/disabled controls. (S3-AC16)
- Ineligible account: an unlinked, inactive, non-USD, or system account cannot
  receive a valid policy save; another company's existence is not disclosed.
  (S3-AC10, S3-AC16)
- Invalid amounts include zero, negatives, 0.001, 100,000.01 under the demo bounds,
  blank/nonnumeric values, and any supplied amount with more than two decimal
  places. Neither the editor nor authoritative validation rounds them. (S3-AC05)
- Two first saves against an unconfigured account compete on the explicit
  unconfigured version; only one can create version 1. (S3-AC07; PRD DD-19)
- A changed bank-bounds version conflicts even if the amount still fits.
  Valid controlled bounds changes must not invalidate existing saved policies.
  (S3-AC08; PRD DD-05)
- A lost save response is not proof of failure: instruct reload to discover
  committed state before another attempt; never overwrite newer data automatically.
  (S3-AC14)
- While loading, submitting, unavailable, or in conflict, present the actual state
  rather than an invented zero threshold or successful save. Preserve editable
  input after validation errors. (PRD NFR-5, NFR-7)
- Keyboard-only and assistive-technology users can reach inputs/actions and
  understand validation, save status, draft status, and reload-required conflicts.
  Do not rely on color alone. (S3-AC17)

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: Authenticate every configuration/evidence action using the existing
  sign-in experience; reject unauthenticated or expired authentication.
- **FR-002**: Check current company membership and company-administrator rights
  for every policy write. Account ownership, bank-admin status, cached claims,
  or visible controls alone MUST NOT grant writing rights.
- **FR-003**: Enforce account/company scope for reads and writes without revealing
  whether an unauthorized company/account exists.
- **FR-004**: Allow company administrators and company auditors to read within
  their company; allow bank administrators and explicit institution auditors
  institution-wide read oversight. Auditors MUST NOT mutate settings; ordinary
  company members MUST NOT access the editor's policy data.
- **FR-005**: Identify the selected company/account with masked account number,
  USD currency, saved state/version, authoritative bounds/version, and last
  change actor/time when a saved policy exists.
- **FR-006**: Distinguish Unconfigured, Disabled, and Enabled policy states.
  Viewing Unconfigured MUST NOT write state or prefill a threshold.
- **FR-007**: Accept an explicit USD amount only for an enabled rule, inside
  inclusive current bank bounds and with at most two decimal places. Validate
  both in the editor and authoritatively; reject malformed, blank, nonpositive,
  excessive-precision, and out-of-range amounts without rounding.
- **FR-008**: Persist valid changes independently per account without a deployment
  or restart. First creation becomes version 1; each later real change advances
  the account policy version once and survives reload.
- **FR-009**: Permit explicit disabling only if current bank bounds allow it;
  save no effective threshold while disabled and require a supplied valid value
  on re-enabling. Do not inherit a company threshold.
- **FR-010**: Require expected account-policy and bank-bounds versions for saves,
  including an explicit Unconfigured version on first creation.
- **FR-011**: Reject a stale version in either dimension, including competing
  creation, without changing state; explain that reload is required.
- **FR-012**: Treat a same-value save using current versions as unchanged success,
  without incrementing a version or duplicating successful-change evidence.
  Stale same-value saves still conflict.
- **FR-013**: Commit each policy mutation and durable audit evidence together.
  If either cannot be durably saved, commit neither. An asynchronously queued
  notification alone MUST NOT satisfy this requirement.
- **FR-014**: Record actor, company/account, action, before/after values,
  policy/bounds versions, UTC time, and correlation ID per committed mutation.
  Provide entitled readers access within 5 seconds; retain evidence for at least
  90 days, with no demo purge requirement and no alteration/deletion action.
- **FR-015**: Do not record reads, validation failures, version conflicts, or
  no-op saves as successful changes. Record denied attempts as security evidence,
  excluding credentials and sensitive payment detail.
- **FR-016**: Refuse saves when authoritative bounds are absent/unreadable,
  disabling is prohibited, or the target is not an eligible active USD non-system
  account linked to the company. No permissive fallback is allowed.
- **FR-017**: Label every displayed/saved policy persistently
  **Draft — not enforced**. Activation MUST be unavailable throughout S3.
- **FR-018**: Policy operations MUST NOT alter balances, ledger entries,
  ownership, existing transfer behavior, or existing transfer response contracts.
- **FR-019**: Show clear success, validation, conflict, unavailable, loading, and
  save-in-progress feedback. Preserve input after validation failure and never
  automatically resubmit stale writes with new versions.
- **FR-020**: Make new views/actions keyboard-operable, correctly labelled,
  focus-visible, and understandable without color-only signals; announce
  validation and save results to assistive technology. Target WCAG 2.2 AA and
  record checks rather than claiming certification.
- **FR-021**: Use only synthetic demo data; mask account numbers to last four
  digits in lists and exclude credentials, full account numbers, and payment
  free text from operational logs and captured evidence.
- **FR-022**: Provide documented isolated setup with persisted STAGED companies,
  registered users, explicit memberships/roles, account links, and authoritative
  bank bounds. No caller-supplied role/company header or browser-only mock may
  substitute for real authorization or persistence.
- **FR-023**: Provide setup, repeatability, role, limitations, and failure/reload
  guidance without deleting pre-existing user data or audit evidence.

### Source Acceptance Coverage

The source story owns its 17 acceptance IDs. This table maps their coverage,
not test execution. All rows remain required for S3 acceptance.

| Source scenario | Local requirements | Journey / edge coverage |
|---|---|---|
| S3-AC01 | FR-003, FR-005, FR-017 | US1.1 |
| S3-AC02 | FR-006, FR-015 | US1.2; US2.3 |
| S3-AC03 | FR-007, FR-008 | US1.3 |
| S3-AC04 | FR-008, FR-009 | US1.3 |
| S3-AC05 | FR-007, FR-015, FR-019 | US1.4; invalid-amount edge |
| S3-AC06 | FR-008, FR-009, FR-014 | US1.5 |
| S3-AC07 | FR-010, FR-011 | US1.6; competing-creation edge |
| S3-AC08 | FR-010, FR-011 | US1.6; bounds-version edge |
| S3-AC09 | FR-002, FR-004 | US1.7; US2 independent test |
| S3-AC10 | FR-003, FR-016 | US1.7; ineligible-account edge |
| S3-AC11 | FR-001, FR-002 | US1.7 |
| S3-AC12 | FR-013, FR-014 | US2.1 |
| S3-AC13 | FR-013, FR-016, FR-019 | US2.2 |
| S3-AC14 | FR-010, FR-012, FR-015, FR-019 | US1.8; lost-response edge |
| S3-AC15 | FR-017, FR-018 | US1.9 |
| S3-AC16 | FR-007, FR-009, FR-016 | Missing-bounds / ineligible-account edges |
| S3-AC17 | FR-019, FR-020 | Keyboard / assistive-technology edge |

Program trace: PRD FR-002/003 → local FR-005–012; PRD FR-014 → local FR-013–015;
PRD FR-017 → local FR-018; PRD FR-020 → local FR-017/022;
PRD FR-022 → local FR-010–012. PRD NFR-1–12 apply only to the parts delivered by
S3; transfer-decision performance, pending queues, and posting are not delivered.

### Key Entities *(include if feature involves data)*

- **Institution Bounds**: institution identity, USD minimum/maximum, disabling
  permission, and version. Initial demo values come from PRD DD-03, not defaults.
- **Company**: institutional scope and STAGED enrollment. No activation is delivered.
- **Membership / Responsibility**: registered user, one company, current status,
  company-administrator or read-only auditor permission. Bank/institution read
  scopes are explicit exceptions; no implicit company writing rights.
- **Linked Account**: existing account identity/ownership plus explicit company
  linkage, currency, status, and type. Existing ownership is preserved.
- **Account Policy**: account identity, explicit Unconfigured/Disabled/Enabled
  state, nullable effective threshold, and saved version/last change metadata.
  Unconfigured is absence of a saved choice, not Disabled.
- **Policy Change Evidence**: immutable application-visible evidence tying actor,
  company/account, before/after state, versions, time, and correlation ID together.

These are business concepts, not a prescribed storage schema or contract design.

## Success Criteria *(mandatory)*

### Measurable Outcomes

All criteria are proposed demo targets. Preparing this spec measures none of them.

- **SC-001**: From a baseline of 0 implemented company threshold editors in the
  inspected platform, deliver 1 demonstrable editor with independently saved
  settings for 2 linked accounts and 17/17 source acceptance scenarios passing.
- **SC-002**: In all executed denied-access, revoked-membership, malformed-input,
  and stale-save scenarios, produce 0 unauthorized mutations and 0 disclosures
  of another company's records.
- **SC-003**: In each executed policy-save/audit-failure and concurrent-save
  scenario, produce 0 partial commits and exactly 1 change record per actual
  committed mutation; evidence is reviewable within 5 seconds.
- **SC-004**: Across configuration scenarios, produce 0 balance, ownership,
  ledger-entry, or transfer-contract changes attributable to this feature.
  Show Draft — not enforced on every policy view, including after reload.
- **SC-005**: With 10 concurrent authenticated users, 20 company accounts, and
  the PRD's 1,000-record history workload, at least 95% of measured policy
  reads/account-list loads/saves respond within 2 seconds, over at least 100
  operations per operation type after warm-up on a documented environment.
  Service response is measured separately from display rendering.
- **SC-006**: All 3 first-time pilot participants can save a valid threshold and
  correctly explain that it is not enforced within 2 minutes each, without coaching.
- **SC-007**: Every primary configuration path, including errors/conflicts,
  is operable using only a keyboard and has recorded label, focus, contrast,
  and screen-reader status-announcement checks.
- **SC-008**: A 30-minute demo requires 0 planned restarts for policy changes.
  An unavailable dependency produces explicit unavailability, never an invented
  threshold or successful save. Failed writes remain safely retryable after reload.

## Assumptions

- PRD version 2 DD-* remain proposed demo assumptions, not approved bank policy.
  The user requested SDD on this story; this does not ratify the PRD or constitution.
- S3 fixtures and authoritative bounds are provisioned through documented trusted
  local setup. No company registration, membership-management, or bank-bounds
  editor is required. Controlled fixture changes support revocation/conflict tests.
- One simulated institution and at most one company per member/account suffice.
  Existing sign-in is reused; this spec selects no new authentication technology.
- Dedicated active USD accounts exist; their ownership is not converted.
  The initial unconfigured policy is not silently set to disabled.
- USD 0.01 and 100,000.00 are proposed bank-bound fixture values. Entered thresholds
  of 10,000.00 and 25,000.00 are walkthrough examples, never prefilled defaults.
- The complete S3 increment includes both user journeys, authorization,
  versioning, durable evidence, privacy, and accessibility. P2 does not waive them.
- Existing historical volume for the performance test must be provisioned
  without implementing a pending-transfer workflow. The plan must document
  how the PRD workload is represented and any resulting measurement limitation.
- Performance/usability baselines are unmeasured. The inspected capability count
  is not market evidence, and no production availability or regulatory claim is made.

### Scope Boundaries and Dependencies

Dependencies: persisted company links/memberships, current scoped authorization,
authoritative bank bounds, durable audit and scoped evidence review, existing
account identity/sign-in, and isolated synthetic fixtures.

Excluded: transfer routing/evaluation, requests, approvals/rejections, reservations,
posting changes, activation/deactivation, machine integrations, notifications,
company onboarding UI, bank-bounds editor, self-approval editor, account migration,
production purge/retention tooling, production release, and external publication.
No plan, tasks, implementation, or acceptance-test result is implied by this spec.

### Architecture Alignment

**NOT CONSULTED.** Applicable enterprise/portfolio owners and governing sources
were not identified. On 2026-11-09 the user explicitly chose to proceed with demo
requirements and record this limitation. This is not corpus silence, approved
alignment, or a waiver of binding requirements.

The [draft constitution](../../.specify/memory/constitution.md) supplies local
guardrails only: source authority, compatibility, scoped authorization, durable
configuration, and honest evidence. Its ratification and the external governance
checkpoint remain open. Revisit applicable principles and plan-stage
standards/ADRs with the responsible Architect before asserting approval.
