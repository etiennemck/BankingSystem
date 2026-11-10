# S3 — Configure an account approval threshold in a staged company

> **DRAFT — SDD DEMONSTRATION INPUT.** Derived from PRD version 2, 2026-11-09.
> Demo policies are proposed, not bank-approved. This is a fleshed-out story for
> the spec stage, not a generated Spec Kit spec, implementation plan, or Jira issue.

## 1 Story and value

**As a Company Administrator, I want to configure an account's transfer approval
threshold within bank-defined bounds, so that I can prepare account-specific
payment controls and verify their meaning before activation.**

- **Local ID:** S3; no Jira key assigned.
- **Proposed sequence:** first configuration demonstration slice, after or with
  its minimal staged-access/setup enablers. Not a Jira priority.
- **Source of truth:** [PRD version 2](../dbb-prd-transaction-limits-approval-controls.md),
  especially FR-002, FR-003, FR-014, FR-017, FR-020, FR-022.
- **Acceptance trace:** AC-1 (configuration portion only), AC-4, AC-5, AC-7,
  AC-10, AC-11. S3 does not satisfy transfer enforcement in AC-1.
- **Policy dependencies:** DD-02–DD-06, DD-15–DD-20. Cite those decisions rather
  than treating the illustrative values below as approved policy.
- **Definition of value:** one authenticated administrator can save different
  draft thresholds for two company accounts, reload them, and inspect their
  evidence; users cannot mistake those settings for enforced transfer controls.

## 2 Problem and current baseline

The inspected BankingSystem has individually owned accounts and no company
threshold editor or company-scoped administrator workflow. There are **0 such
implemented editors** in the inspected platform; no baseline task time or user
adoption has been measured.

The first increment must not change money movement. The configuration itself is
the demonstrable value; its lack of enforcement is deliberately visible.

## 3 In scope

- Authenticated, server-authorized company-admin navigation and account policy view.
- List/select only the staged company's linked accounts.
- View bank bounds, account currency, explicit policy state, saved versions,
  last actor/time, and persistent **Draft — not enforced** warning.
- Enable a rule with an explicit threshold, change its value, or explicitly disable it.
- Persist valid settings; reload saved state; produce durable auditable changes.
- Detect stale account-policy and bank-bounds edits; require reload.
- Use real persisted company membership/account links and trusted server-side grants.
- Include only the necessary company/access/audit enablers to demonstrate the slice.
  They remain implementation tasks under this story, not discipline-based stories.

## 4 Explicit exclusions

- No transfer evaluation, pending requests, approve/reject action, funds reservation,
  posting changes, or automated notifications.
- No company activation/deactivation; activation must be unavailable, not a fake
  enabled button. No claim that a saved threshold protects payments.
- No bank-bounds editor, membership-management UI, company-registration UI,
  self-approval editor, account migration, multi-bank support, or production retention tooling.
- No browser-only mock standing in for backend authorization or durable saves.
- No Spec Kit installation, branch creation, code implementation, Jira creation,
  or external publication merely because this story document exists.

## 5 Proposed demo policy and fixtures

These are synthetic logical labels, not credentials or new production defaults.
Provision them through a documented local setup with existing authentication.

| Fixture | Purpose |
|---|---|
| Demo Institution | Bank bounds present: USD 0.01–100,000.00 inclusive; disabling allowed; bank-bounds version initially 1 |
| Company A / Company B | Distinct STAGED companies; neither can be activated in S3 |
| Admin A / Admin B | Registered authenticated company administrators for A/B respectively |
| Member A | Active member of A without company-administration rights |
| Company Auditor A | Read-only auditor of A |
| Bank Administrator | Existing ADMIN, used for setup/read oversight; not an implicit company policy writer |
| A-Operating / A-Reserve | Active USD non-system accounts linked to A, initially unconfigured |
| B-Operating | Active USD non-system account linked to B |
| Legacy Personal | Unenrolled account used to demonstrate compatibility |

Initial ownership fields remain unchanged. Where policy is unconfigured, show an
empty threshold input, never a prefilled “recommended” amount.

For the walkthrough only, Admin A deliberately chooses **10,000.00** for
A-Operating and **25,000.00** for A-Reserve. These are entered examples, not defaults.
No transfer amount cap is introduced.

The first saved policy version is 1. An unconfigured read exposes an explicit
unconfigured version marker, not a fabricated saved version.

## 6 User journey

1. Admin A signs in through the existing login.
2. Open company payment-control configuration and select A-Operating.
3. Read bounds, currency, empty current state, and **Draft — not enforced**.
4. Enable approval, enter 10,000.00, and save.
5. See success and the persisted policy version; reload and verify it remains.
6. Configure A-Reserve at 25,000.00 to demonstrate account-specific policy.
7. Show a validation error and a stale-edit conflict without losing the newer data.
8. Inspect authorized evidence for the successful changes.
9. Demonstrate that Member A cannot change settings and Admin B cannot access A.
10. Show that activation is unavailable and no ledger entries were created.

Do not submit a transfer as “proof of enforcement.” If demonstrating existing
transfer behavior, explicitly explain that staged thresholds have no effect.

## 7 Acceptance scenarios

Every scenario is required for story acceptance. IDs are stable local identifiers,
not test results. Access is enforced server-side even when the UI hides controls.

| ID | Given / When / Then | Trace |
|---|---|---|
| S3-AC01 | **Given** Admin A is authenticated and A is STAGED, **when** they open configuration, **then** only A's linked accounts are available; show USD bounds, bank-bounds version, current policy state/version, last actor/time if configured, and Draft — not enforced. | FR-002, FR-003; AC-7, AC-10 |
| S3-AC02 | **Given** A-Operating is unconfigured, **when** its editor opens, **then** no threshold is prefilled, the state is explicitly Unconfigured, and no saved policy or change-audit entry is created just by viewing. | FR-003; DD-04, DD-19 |
| S3-AC03 | **Given** current expected versions, **when** Admin A enables approval at 10,000.00, **then** persist that USD threshold as policy version 1, show success, and return the same value/version after reload. | FR-002, FR-003, FR-022 |
| S3-AC04 | **Given** A-Operating has a saved rule, **when** A-Reserve is configured at 25,000.00, **then** retain both independent values without company-level inheritance or changes to A-Operating. | FR-003; AC-1 configuration portion |
| S3-AC05 | **Given** bank bounds of 0.01–100,000.00, **when** enabling at either exact boundary, **then** accept it; **when** using 0, a negative, 0.001, 100,000.01, a blank, nonnumeric input, or more than two decimal places, **then** reject without rounding or mutating saved policy/evidence. Client and server both validate. | FR-001, FR-003; DD-03, DD-04 |
| S3-AC06 | **Given** disabling is allowed and the saved rule is enabled, **when** Admin A explicitly disables it with current versions, **then** persist disabled state with a null effective threshold, increment policy version once, and retain the former value in audit. Re-enabling requires an explicit valid amount. | FR-003, FR-014; DD-04 |
| S3-AC07 | **Given** two sessions read the same policy version, **when** the first saves and the second attempts a different save, **then** the second gets a conflict requiring reload; retain the first saved value/version and create no successful-change record for the rejected attempt. | FR-022; AC-11 |
| S3-AC08 | **Given** the editor read bank-bounds version 1, **when** a trusted test setup validly changes it before saving, **then** a save referring to version 1 conflicts even if its value fits the new bounds. Reload is required. | FR-022; DD-05, DD-19 |
| S3-AC09 | **Given** Member A lacks administrator rights, **when** they attempt to read the policy editor data or save settings, **then** deny access and make no policy change. Auditor A may read but cannot save. A global ADMIN without company-admin grant cannot silently write company settings through this action. | NFR-1; DD-16 |
| S3-AC10 | **Given** Admin B supplies A's account/company identifiers directly, **when** requesting reads or writes, **then** disclose no A policy/account data and change nothing. Unknown and unauthorized identifiers do not reveal cross-company existence. | NFR-1; AC-10 |
| S3-AC11 | **Given** a token exists but company-admin membership is revoked by trusted setup after page load, **when** the stale session saves, **then** deny using current server state without policy mutation; do not trust cached token/UI roles. Unauthenticated and expired-token requests also fail. | FR-013 dependency; NFR-1 |
| S3-AC12 | **Given** a valid mutation, **when** it commits, **then** authorized company/bank audit readers can find actor, company/account, before/after state, policy/bounds versions, UTC time, and correlation ID within 5 seconds; there is exactly one change record for that mutation. | FR-014; NFR-2 |
| S3-AC13 | **Given** durable audit recording is unavailable, **when** a valid save is attempted, **then** fail with actionable retry guidance and leave policy/version/evidence unchanged. An asynchronous notification or event alone is not sufficient evidence. | DD-15; NFR-3, NFR-5 |
| S3-AC14 | **Given** a previously saved value, **when** the same enabled/value state is submitted with current versions, **then** return unchanged success with no version increment and no duplicate change evidence. After a lost response, reload before deciding whether another save is needed; a stale replay still conflicts. | DD-19 |
| S3-AC15 | **Given** this staged increment, **when** settings are viewed/saved, **then** the draft warning remains visible after reload, activation is unavailable, and no balances, ledger entries, transfer responses, or ownership fields change as a result of configuration. | FR-017, FR-020; AC-5, AC-7 |
| S3-AC16 | **Given** bank bounds are missing/unreadable, disabling is prohibited, or an account is not an eligible linked active USD non-system account, **when** an invalid save is attempted, **then** refuse it with a safe reason, preserve prior state, and never invent fallback bounds. | FR-001, FR-003; DD-02–DD-04 |
| S3-AC17 | **Given** the editor is keyboard-operated, **when** a user edits, saves, encounters validation, or reloads after conflict, **then** labels, focus, warnings, status and error announcements are usable without relying on color or a pointer. | NFR-7, NFR-10 |

## 8 Quality and verification expectations

- **Success target:** all 17 scenarios pass, 0 unauthorized policy mutations,
  0 ledger entries from configuration, and 1 independently demonstrable editor.
- **Latency:** measure policy reads/account lists/saves under PRD NFR-6's workload;
  P95 ≤2 seconds for at least 100 operations of each measured type after warm-up.
  Story completion does not claim decision-action performance that it does not implement.
- **Usability:** PRD NFR-7's proposed 3-participant check; no result is asserted yet.
- **Evidence:** retain synthetic HTTP outcomes, browser screenshots, persisted
  policy/version checks, audit checks, and ledger-before/after counts. Exclude tokens
  and full account numbers from captured logs.
- **Testing layers:** focused validation/authorization/versioning unit tests;
  real HTTP/persistence tests including audit fault injection and two-session races;
  browser journey with actual login; keyboard and accessibility checks.
- **Compatibility:** run the existing relevant account/transfer tests unchanged.
  A passing editor test is not evidence of a passing payment workflow.
- **Setup/reset:** document isolated fixture setup and synthetic test cleanup.
  Do not delete existing user data or audit records to make the demo repeatable.

No tests were run and no acceptance result is claimed by authoring this story.

## 9 Dependencies and sequencing

| Dependency | Needed for this story | Boundary |
|---|---|---|
| Company/account links and membership | Persisted STAGED company membership, initial admin, account links, isolated fixture setup | No full onboarding UI; one company per user/account |
| Bank bounds | Persisted authoritative values/version and controlled test variation | No bounds editor; missing values block save |
| Scoped authorization | Current server-side admin checks and read-only auditor/oversight scopes | No global auth replacement or caller-provided trusted role headers |
| Durable audit | Atomic policy/evidence behavior plus an authorized way to inspect evidence | Existing async pipeline must not weaken the requirement |
| Frontend | Real editor backed by authenticated reads/writes | No mock backend sold as completed functionality |
| Governance | Identify governing sources and consult applicable principles before spec checkpoint; standards/ADRs before plan | Currently not consulted; do not claim corpus silence |

The plan stage chooses how to implement these dependencies. Do not decide database
schemas, endpoints, component names, or locking mechanisms in this input story.
If the necessary enablers make the story too large, record and review the scope
before planning; do not quietly omit authorization or audit to keep it small.

## 10 SDD handoff

### Current progress — 2026-11-09

Spec Kit 1.3.2 is now initialized for Droid with PowerShell scripts. The generated
[feature specification](../../../specs/001-account-approval-threshold/spec.md)
and [workflow record](../../../specs/001-account-approval-threshold/workflow-notes.md)
capture the specification and clarification review. The source story remains
intake; subsequent spec changes belong in the feature artifact, with source
policy changes synchronized to the PRD when required.

The [constitution](../../../.specify/memory/constitution.md) is draft and unratified;
architecture governance is explicitly not consulted. No plan, implementation,
or application acceptance tests have run. The historical tooling observations
below describe the state when this story was first authored.

### Intake assessment

**Ready to draft a demonstration spec with explicit proposed policies; not ready
to implement or release.** Problem, outcomes, exclusions, dependencies, scenarios,
and numeric demo targets are present. Policy acceptance and governance checkpoints
remain visible, not silently marked complete.

### Preconditions and workflow

1. **Facilitator review:** accept or amend PRD DD-* and this story's staged scope.
   Record acceptance as demo-assumption acceptance, never bank approval.
2. **Tooling check:** verify Spec Kit is installed/configured in this project.
   No `.specify/` or `specs/` files were found during document preparation; command
   availability has not been verified. Installing tooling requires a separate step.
3. **Governance / constitution:** identify applicable enterprise/portfolio sources;
   use architecture-north-stars before the first checkpoint. Record inaccessible
   sources as **not consulted**, never as “no standards apply.”
4. **Specify:** use this story and PRD version 2 as input. Produce the what/why and
   prioritized journeys without choosing implementation design.
5. **Clarify:** reconcile any remaining ambiguities with the facilitator and update
   both source documents if the chosen behavior changes.
6. **Plan:** consult applicable standards/ADRs, inspect affected code, choose API,
   persistence, authorization, audit, migrations, and test approach.
7. **Tasks:** produce implementation tasks traceable to S3-AC01–17. Foundational
   changes and test work stay as tasks, not separate discipline stories.
8. **Analyze:** check spec, plan, and tasks agree; no activation/enforcement work
   leaks into this slice and no mandatory security/audit scenario is omitted.
9. **Implement:** only after review and explicit instruction; run checks and
   demonstrate the evidence rather than claiming success from document completeness.

In Droid, the supplied SDD guidance names the stages `/speckit-specify`,
`/speckit-clarify`, `/speckit-plan`, `/speckit-tasks`, `/speckit-analyze`, and
`/speckit-implement`; verify installed stage names before using them.
The eventual feature folder belongs under `specs/`; this document remains intake,
not a second editable copy of the generated spec.

### Suggested spec-stage input

> Specify S3 from docs/product/stories/S3-configure-account-approval-threshold.md,
> using docs/product/dbb-prd-transaction-limits-approval-controls.md version 2
> as its policy source. Build only staged account-threshold configuration:
> authenticated scoped access, authoritative bank bounds, explicit account settings,
> version-conflict handling, durable audit, and a persistent Draft — not enforced
> warning. No transfer enforcement or activation. Preserve DD-* as proposed demo
> assumptions, trace requirements to S3-AC01–17, and flag any new decision rather
> than inventing approved policy or selecting implementation technology.

## 11 Completion and review checklist

- [ ] Facilitator has reviewed proposed demo policies and scope.
- [ ] Applicable governance checkpoint completed or explicitly recorded as blocked.
- [ ] Spec, clarified decisions, plan, tasks, and analysis are reviewable and consistent.
- [ ] All 17 acceptance scenarios have executable evidence.
- [ ] Relevant existing checks pass; unrun/failing checks are reported accurately.
- [ ] Editor/reload, isolation, version conflict, audit failure, and no-posting behavior demonstrated.
- [ ] Setup, limitations, and draft-versus-active wording documented.
- [ ] Product Owner accepts the story for the demonstration.

These are future verification/sign-off items, not completed by this document.

## 12 Open ownership and production questions

- **Product Manager / Product Owner:** name the accountable reviewer and confirm
  the chosen demo policy; provide a Jira parent only if publication is requested.
- **Tech Lead / Architect:** identify governing sources and choose the minimal
  persisted access/audit implementation through the plan stage.
- **Risk / Compliance / SRE:** approve real bounds, retention, privacy, availability,
  and recovery expectations before any production use.

None requires a fabricated answer to draft the demo spec; none is waived for
production or for implementing a materially different policy.
