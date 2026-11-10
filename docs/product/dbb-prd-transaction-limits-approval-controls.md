# DBB PRD Transaction Limits & Approval Controls

> **DRAFT — DEMO POLICY EDITION.** Synthetic training artifact. The user requested
> explicit decisions for a demonstration on 2026-11-09. The choices labelled
> `DD-*` are assistant-proposed demo policies, not approved FIS, bank, client, risk,
> compliance, or commercial requirements. This document does not authorize production
> delivery. No real money, customer data, or external integrations are required.

## 1 Page Properties

| Property | Value |
|---|---|
| Document Title | DBB PRD Transaction Limits & Approval Controls |
| Document Status | ☑ DRAFT ☐ IN REVIEW ☐ APPROVED |
| Program Owner | Product Management role. [NEEDS CLARIFICATION: named accountable owner — owner: Product Management] |
| Program Lead & Document Owner | Product Owner role; Droid prepared this revision at the user's request. [NEEDS CLARIFICATION: named document owner — owner: Product Management] |
| Collaborators | Business/Risk, Engineering, Quality Engineering, Product Design, Architecture, Compliance, SRE, API platform, Client Solutions |
| Approvers | [NEEDS CLARIFICATION: named approvers and authority — owner: Product Management] |
| Version / Date | 3 / 2026-11-09 |
| Selected opportunity | CI-01 Proportionate Payment Authorization, as described in the supplied version 1 draft; selection is illustrative, not approved prioritization. |

**Authority order:** current code establishes current behavior only; the supplied
version 1 PRD describes synthetic discovery; `DD-*` decisions establish the proposed
demo behavior only. Production policy requires human review and approval.

## 2 Table of Contents

1. Page Properties
2. Table of Contents
3. Executive Summary
4. Schedule & Milestones
5. Important Resources & Links
6. Introduction & Problem Statement
7. Business Justification & Benefit Statement
8. Scope of Work
9. Acceptance Criteria
10. User Personas
11. Functional Requirements
12. Non-Functional Requirements
13. UI / UX Considerations
14. Dependencies
15. Risks & Assumptions
16. Appendix

## 3 Executive Summary

**Context:** The supplied synthetic discovery describes business customers with
different account values and internal approval policies. BankingSystem currently
posts successful account-to-account transfers immediately.

**Problem:** The platform cannot express account-specific approval thresholds,
shared company responsibilities, or an in-platform decision history. The supplied
prospect scenarios describe manual email approvals and concentrated duties.

**Who:** Payment Initiator, Payment Approver, Company Administrator, Bank
Administrator, and read-only Auditor.

**What:** A company can configure account approval thresholds inside bank bounds.
For an ACTIVE company, a transfer below its enabled threshold, or from an explicitly
disabled-rule account, executes normally. A transfer at or above its threshold
requires one eligible approver; no money is reserved or posted while pending.
Configuration, permissions, and decisions are durably auditable. Unenrolled
personal accounts keep existing behavior.

**Why now:** The user wants a concrete PRD and a bounded story for demonstrating
spec-driven development (SDD). This is a demo sequencing decision, not a claim of
validated demand, funded priority, or delivery commitment.

**Hands-on exercise increment:** Build a standalone account-threshold evaluator
and unit tests, progressing through specification, design/tasks, and implementation/
verification. It determines approval need but is not connected to live submissions
(DD-21, DD-22). S3's staged configuration story is retained as separate future work,
not the selected exercise. Neither increment alone provides live payment control.

## 4 Schedule & Milestones

No supplier delivery dates are established. H1 2027 in the supplied synthetic client
draft is client preference only, not a commitment.

| Milestone | Description | Desired Date | Go/No-Go Criteria |
|---|---|---|---|
| Phase 1 | Scope: Research & Design; review demo decisions, selected story, and solution feasibility | [NEEDS CLARIFICATION: demonstration date — owner: session facilitator] | ☐ Build Readiness Checklist Completed |
| Phase 2 | Build: Dev & Testing; standalone evaluator exercise, followed by separately reviewed configuration/enforcement slices | Not committed; follows review | ☐ Deploy Readiness Checklist Completed |
| Phase 3 | Launch: Commercialize | No commercial launch authorized | ☐ Launch Readiness Checklist Completed |

## 5 Important Resources & Links

| Resource Name | Description | Link |
|---|---|---|
| Aha! program | No live record verified in this revision | [NEEDS CLARIFICATION: program/objective link if commercialized — owner: Product Management] |
| Jira program | No epic provided or created | [NEEDS CLARIFICATION: epic key before Jira publication — owner: Product Owner] |
| UI / UX Design & Research | Requirements in §13; no approved design supplied | [NEEDS CLARIFICATION: design artifact — owner: Product Design] |
| Solution Design | Not authored; follows requirements review | [NEEDS CLARIFICATION: solution artifact — owner: Solution Architect] |
| Planview Bucket | Not needed to execute a local demonstration | [NEEDS CLARIFICATION: required booking reference for funded delivery — owner: Product Management] |
| MS Teams Site | No site supplied | [NEEDS CLARIFICATION: collaboration site if needed — owner: Product Management] |
| Partner Team Docs | Synthetic Northstar integration material not supplied | [NEEDS CLARIFICATION: partner evidence for real delivery — owner: Client Solutions] |
| Selected exercise story | S5-EVAL: standalone account-threshold evaluation | [Story S5-EVAL](stories/S5-EVAL-account-approval-threshold-evaluation.md) |
| Exercise specification | Evaluator decisions, edge cases, and compatibility boundary | [Evaluator spec](../../specs/002-evaluate-approval-threshold/spec.md) |
| Deferred configuration story | S3: staged account threshold configuration, retained unchanged in scope | [Story S3](stories/S3-configure-account-approval-threshold.md) |
| Current platform | Implemented behavior and run instructions | [README](../../README.md) |

**Evidence register**

| Source | Use and verification status |
|---|---|
| `SUPPLIED-V1` | Full version 1 PRD pasted by the user in this session. Source for existing IDs, scope, and synthetic discovery citations. The original was not present as a workspace file. |
| `SYNTHETIC-DISCOVERY` | SEL-001, STR-001, PSF-001, CSR-001, MRD-001, CI-01 to CI-05, OBS-01 to OBS-51, and original decisions D1–D4 are reported by SUPPLIED-V1. The cited `product/discovery/...` and `product/viability/...` files are absent from this checkout; their content was not independently verified. |
| `CODE-TRANSFER` | `backend/src/main/java/com/bank/transfer/service/TransferService.java` and `transfer/web/TransferController.java`: owner-based access, immediate posting, successful response `COMPLETED`, optional idempotency header, no channel discriminator. |
| `CODE-ACCOUNT` | `backend/src/main/java/com/bank/account/domain/Account.java` and `account/service/AccountService.java`: single owning user, USD-only account creation, current balance and account-status rules. |
| `CODE-LEDGER` | `backend/src/main/java/com/bank/ledger/service/LedgerService.java`: balanced posting, account locking, insufficient-funds checks, replay lookup when an idempotency key is supplied. Not proof of measured exactly-once behavior for every intent. |
| `CODE-AUDIT` | `backend/src/main/java/com/bank/audit/domain/AuditEntry.java` and README: append-only audit design and event consumers. Not proof of tamper resistance against privileged database access or guaranteed delivery across failures. |
| `DEMO-MANDATE` | User request on 2026-11-09 to flesh out decisions for demo purposes and prepare a story for SDD. It authorizes document preparation, not production policy approval. |
| `DD-*` | Explicit proposed demo policies in §16.4. Numeric limits and targets are illustrative choices, not discovery findings or existing defaults. |

## 6 Introduction & Problem Statement

SUPPLIED-V1 describes three synthetic prospects needing proportional authorization:
routine low-value payments should remain simple, while larger transfers receive
review. One prospect's $10,000 policy is an example, not evidence for a universal
threshold. The reported client draft asks for company administration with bank
oversight and auditability.

The inspected code has no company account access or pending transfer lifecycle.
It posts successful transfers through the ledger and returns `COMPLETED`.
Adding approval therefore requires company-scoped access and a request lifecycle,
not just a new button.

Here, **transaction limits means approval-trigger thresholds and bank bounds on
those thresholds**. This program does not introduce hard maximum transfer amounts,
daily/monthly aggregate limits, or a general account spending-control system.
Cards, withdrawals, top-ups, and loans remain outside this transfer-specific policy.

## 7 Business Justification & Benefit Statement

- **Business customers:** configurable account controls and durable decisions
  replace the synthetic scenarios' email coordination.
- **Finance staff:** one place to submit, review, and track governed requests.
- **Banks:** scoped oversight and auditable configuration within bank bounds.
- **FIS:** a reusable configuration model rather than client-specific rules.
- **Demo audience:** a traceable progression from PRD decisions to one testable
  story, specification, plan, tasks, and evidence.

Commercial benefits remain qualitative. No market size, revenue, measured time
saving, loss reduction, or production baseline is asserted. §16.3 separates demo
verification targets from commercial success measures.

## 8 Scope of Work

### 8.1 In Scope

| Scope Area | Description |
|---|---|
| Demo company setup | One simulated institution; bank admin creates STAGED companies, links dedicated demo accounts and registered users, and assigns the initial company admin. |
| Scoped responsibilities | Company membership plus account-scoped initiator/approver grants; company-wide administrator; explicit read-only auditor grants. |
| Rule configuration | Bank bounds; per-account enabled/disabled threshold configuration; company self-approval setting. No company-wide inherited threshold. |
| Governed transfers | For ACTIVE companies, evaluate source-account policy server-side on every transfer submission regardless of caller-supplied channel labels. |
| Decisions | One eligible approver may approve or reject a pending request; posting at most once; explicit failure outcomes. |
| Visibility | Approver queue, initiator history, company rejected history, and bank oversight. |
| Audit | Durable decision, configuration, permission, enrollment, and activation evidence; security refusals are recorded separately from successful decisions. |
| Safe staged delivery | Configuration can be demonstrated before enforcement exists; activation remains unavailable until all control dependencies pass. |

### 8.2 Out of Scope

| Scope Area | Description |
|---|---|
| Machine-consumer product | No ERP integration, machine credentials, channel exemption, or external payment-lifecycle contract. Existing transfer calls against ACTIVE company accounts are still governed. |
| Other limits | No hard payment caps, cumulative limits, per-user amounts, payment-type rules, or non-transfer spending control. |
| Other payment capabilities | No scheduled, recurring, batch, cross-currency, clearing, settlement, or external-core changes. |
| Notifications and broad treasury views | CI-02/CI-04 remain deferred. No new approval notification service level or consolidated cash dashboard. |
| Reconciliation | CI-05 remains deferred; internal ledger integrity checks are still required. |
| Advanced lifecycle | No pending-request edits, initiator cancellation, automatic expiry, appeal, or automatic resubmission. |
| Advanced administration | No multi-bank tenancy, self-service company registration, invitations, membership in multiple companies, account reassignment after activation, or company deactivation in the demo. |
| Production release | No legal retention claim, compliance certification, external publication, client onboarding, or real-money authorization. |

### 8.3 Deferred Items and Why

CI-03 machine integrations need their own supported authentication and lifecycle
contract; they are not a bypass around source-account controls. CI-02 notifications,
CI-04 cash visibility, and CI-05 reconciliation remain separate opportunities.
Pending requests do not expire in this demo; the operator guide must explain
manual approve/reject resolution and queue monitoring.

### 8.4 Platform Integration Scope

| Category | Description | In Scope | Out of Scope | Reason / Notes |
|---|---|---|---|---|
| **Digital Channels & Platforms** | | | | |
| D1B Online / Desktop | Legacy web | ☐ | ☑ | Not present |
| D1B Mobile (legacy) | Legacy mobile | ☐ | ☑ | Not present |
| D1B BST | Legacy bank admin | ☐ | ☑ | Use platform administration |
| D1B Legacy BeB / BST | Legacy channel/admin | ☐ | ☑ | Not present |
| D1C Online / Desktop | Legacy web | ☐ | ☑ | Not present |
| D1C Mobile (legacy) | Legacy mobile | ☐ | ☑ | Not present |
| D1C SAM | Legacy admin | ☐ | ☑ | Not present |
| DBB Online | BankingSystem React web | ☑ | ☐ | Configuration, requests, approvals, oversight |
| D1C Mobile (native) | Native mobile | ☐ | ☑ | Not present |
| **Core Processing Platforms** | | | | |
| IBS | External core | ☐ | ☑ | Not present |
| HORIZON | External core | ☐ | ☑ | Not present |
| Affinity Edge | External core | ☐ | ☑ | Not present |
| BancPac | External core | ☐ | ☑ | Not present |
| Bankway | External core | ☐ | ☑ | Not present |
| Other: platform ledger / REST API | Internal posting and interactive API | ☑ | ☐ | Extend company behavior; preserve unenrolled contract |
| **Productized API Channels** | | | | |
| Code Connect | External API product | ☐ | ☑ | No machine-consumer product in demo |

## 9 Acceptance Criteria

Original IDs are preserved; additions begin at AC-7.

| AC-# | Acceptance Criteria |
|---|---|
| AC-1 | ACTIVE companies can configure different per-account thresholds within bank bounds; amounts below an enabled threshold execute without approval. |
| AC-2 | A governed request at or above its threshold creates no posting or reservation before an eligible approval. |
| AC-3 | Eligible approvers can find, approve, and reject pending requests; rejected history remains visible to entitled company users. |
| AC-4 | Every committed rule/permission change and approval/rejection decision has durable actor, action, entity, time, and relevant before/after or decision evidence reviewable by the bank. |
| AC-5 | Unenrolled accounts preserve existing transfer workflows and successful API response shape. ACTIVE-company accounts with disabled rules execute without an approval step, but still require company initiation rights. |
| AC-6 | Self-approval requires both current approver permission and company permission within current bank bounds; otherwise no decision or posting occurs. |
| AC-7 | STAGED configuration is persistently labelled Draft — not enforced; activation cannot occur before enforcement, visibility, and audit dependencies pass. |
| AC-8 | Conflicting decisions or duplicate retries produce at most one final decision and one posting; no partial posting occurs. |
| AC-9 | Insufficient funds or inactive accounts at approval yield FAILED without posting, with a recorded failure and clear next action. |
| AC-10 | Cross-company access is refused without disclosing another company's records; bank oversight and auditor access are explicit read-only exceptions. |
| AC-11 | Concurrent configuration saves cannot silently overwrite a newer version or newer bank bounds. |

## 10 User Personas

Role-based demo personas are not formal approved research personas.

| User Persona | Summary & Function | Problems Being Solved | Objectives |
|---|---|---|---|
| Payment Initiator | Active member with source-account initiation permission | Manual approval coordination; unclear request state | Submit transfers and see own company requests |
| Payment Approver | Active member with source-account approval permission | No decision queue or durable evidence | Review detail and make one authorized decision |
| Company Administrator | Company-wide configuration and responsibility administrator | Cannot express account-specific policy | Configure rules and grants without acquiring payment authority automatically |
| Bank Administrator | Existing ADMIN mapped to institution-wide demo setup and oversight | No company policy bounds or oversight | Enroll companies, set bounds, activate complete controls, review evidence |
| Auditor | Explicitly assigned company auditor or institution auditor | Cannot reconstruct decisions | Read scoped configuration, request history, and audit evidence; never mutate or approve |

SUPPORT gains no new company permissions. Existing CUSTOMER/ADMIN/SUPPORT behavior
outside enrolled company transfers remains unchanged. A company admin, bank admin,
account owner, or auditor is not automatically an initiator or approver.

## 11 Functional Requirements

All rows are Must for the complete demo capability, not all for the first story.
Original FR-001–019 IDs remain stable.

| FR-# | FR Name | Priority (MoSCoW) | Description |
|---|---|---|---|
| FR-001 | Bank-level authorization bounds | Must | As a Bank Administrator, I want to define allowable configuration, so company policies remain within bounds. Given a proposed bounds change, when any saved enabled threshold or disabled-rule setting would become invalid, reject the entire change and identify affected accounts only to the authorized administrator. Bounds are never silently clamped. Source: SUPPLIED-V1 FR-001; DD-03, DD-05. |
| FR-002 | Company-level rule configuration | Must | As a Company Administrator, I want configuration without deployment. Given current membership and account scope, when a valid versioned change is saved, persist it atomically with audit evidence. In STAGED mode it is a draft; in ACTIVE mode it applies to new submissions after commit. Source: SUPPLIED-V1 FR-002; DD-02, DD-06, DD-15. |
| FR-003 | Account-level approval thresholds | Must | As a Company Administrator, I want separate account thresholds. Given an enabled rule, require a USD amount with at most two decimal places inside inclusive bank bounds; approval applies when transfer amount is greater than or equal to the threshold. No automatic threshold, rounding, or company-threshold inheritance exists. Source: SUPPLIED-V1 FR-003; DD-03, DD-04. |
| FR-004 | Straight-through processing below threshold | Must | As an Initiator, I want routine transfers to complete normally. Given current initiation permission and an ACTIVE source account with disabled rules or an amount below threshold, execute immediately under existing posting checks. Source: SUPPLIED-V1 FR-004; DD-01, DD-06. |
| FR-005 | Threshold-triggered approval routing | Must | As an Initiator, I want larger requests reviewed. Given a qualifying submission and at least one eligible approver, create one PENDING_APPROVAL request with frozen transfer details and policy evidence; show it only to eligible approvers. If none exists, reject submission without creating a pending request. Source: SUPPLIED-V1 FR-005; DD-07, DD-08. |
| FR-006 | No execution while approval is pending | Must | As an Approver, I want pending requests to move no funds. Given PENDING_APPROVAL, balances remain unchanged and no reservation or ledger entry exists; show that available funds are not guaranteed. Source: SUPPLIED-V1 FR-006; DD-09. |
| FR-007 | Approver visibility of pending requests | Must | As an Approver, I want deciding detail. Given current approval eligibility, display source account, masked destination with permitted detail access, amount, currency, initiator, submission time, note, applied threshold/version, and reservation warning. Source: SUPPLIED-V1 FR-007; DD-07, DD-18. |
| FR-008 | Approve action | Must | As an Approver, I want approval to attempt execution safely. Given PENDING_APPROVAL and current eligibility, revalidate both accounts and funds. Successful posting yields COMPLETED with ledger reference; insufficient funds or inactive accounts yield FAILED with no posting. A transient infrastructure failure leaves the request pending and records no successful approval. Source: SUPPLIED-V1 FR-008; DD-09–DD-12. |
| FR-009 | Reject action | Must | As an Approver, I want to decline a request. Given PENDING_APPROVAL and current eligibility, rejection records a mandatory 1–500 character non-whitespace reason, yields REJECTED, and moves no funds. Source: SUPPLIED-V1 FR-009; DD-10, DD-12. |
| FR-010 | Retention and visibility of rejected requests | Must | As a Company Administrator, I want reviewable history. Given a final request, entitled company admins/auditors can inspect it; initiators see their own and current account approvers see their account's history. Retain decisions under the demo retention policy. Source: SUPPLIED-V1 FR-010; DD-16, DD-17. |
| FR-011 | Initiator visibility of request state | Must | As an Initiator, I want the actual outcome. Given continued company membership, show my PENDING_APPROVAL, COMPLETED, REJECTED, and FAILED requests with timestamps and safe failure/rejection detail. Removing initiation permission does not erase own history; removing membership removes company access. Source: SUPPLIED-V1 FR-011; DD-10, DD-16. |
| FR-012 | Self-approval configuration | Must | As a Company Administrator, I want configurable segregation of duties. Self-approval starts false and may become true only when bank bounds allow it. At decision time require current approver permission and both bank/company self-approval permission; record successful self-approval explicitly. The same prohibition applies to self-rejection. Source: SUPPLIED-V1 FR-012; DD-03, DD-07. |
| FR-013 | Assignment of payment responsibilities | Must | As a Company Administrator, I want account-scoped initiator/approver grants for active members. Given a valid grant/revocation, commit it with audit evidence; subsequent actions immediately use current grants rather than stale token claims. In ACTIVE mode reject changes that would strand pending requests without any eligible approver. Source: SUPPLIED-V1 FR-013; DD-02, DD-07, DD-13. |
| FR-014 | Audit of configuration and permission changes | Must | As a Bank Administrator, I want reconstructable changes. Each committed change records actor, scope, entity, action, before/after values, versions, UTC timestamp, and correlation ID. Failed validation does not produce a successful-change record; denied attempts generate security evidence. Source: SUPPLIED-V1 FR-014; DD-15, DD-17. |
| FR-015 | Audit of approval decisions | Must | As a Bank Administrator, I want provable decisions. A decision records request ID, actor, time, policy evidence, decision/reason, outcome, and ledger reference when posted. A rollback cannot leave a successful-decision audit entry; retries cannot duplicate decisions. Source: SUPPLIED-V1 FR-015; DD-12, DD-15. |
| FR-016 | Bank oversight of company rules and activity | Must | As a Bank Administrator, I want institution-scoped visibility of company settings and activity. Bank oversight does not confer payment decision authority. Explicit institution auditors have equivalent read scope without administration. Source: SUPPLIED-V1 FR-016; DD-16. |
| FR-017 | Preserved behavior outside approval rules | Must | As an existing user, I want unenrolled accounts unchanged. No automatic account conversion, new mandatory header, or response-field change applies to unenrolled transfers. Company enrollment is explicit; ownership never bypasses ACTIVE company grants. Source: SUPPLIED-V1 FR-017; DD-01, DD-02, DD-14. |
| FR-018 | Rule changes and in-flight requests | Must | As a Company Administrator, I want predictable in-flight behavior. Threshold/enablement changes apply only to later submissions; pending requests retain submission-time details and threshold evidence, never auto-execute. Current membership, grants, bank/company self-approval restrictions, and account status still govern decisions. Source: SUPPLIED-V1 FR-018; DD-11, DD-13. |
| FR-019 | Funds treatment while approval is pending | Must | As an Approver, I want correct funds treatment. No reservation occurs; approval checks current balance during the posting attempt. If funds are insufficient, finalize FAILED, do not automatically retry, and require a new explicitly submitted intent to try again. Source: SUPPLIED-V1 FR-019; DD-09, DD-10. |
| FR-020 | Company onboarding and activation | Must | As a Bank Administrator, I want explicit enrollment. Create STAGED companies and link dedicated active demo accounts and existing users without rewriting ownership; allow exactly one company per member/account. Activation requires complete configuration, current bounds, initiator and approver coverage, and working enforcement, decision, visibility, and audit capabilities. Staged enrollment does not change transfers. Source: DEMO-MANDATE; DD-02, DD-06. |
| FR-021 | Server-side governance boundary | Must | As a Bank Administrator, I want no alternate transfer-route bypass. All transfer entry points use source-account enrollment and current company permissions; caller headers or channel fields cannot exempt ACTIVE accounts. No supported machine-only route or credentials are introduced. Source: SUPPLIED-V1 §8/DR-4; DD-01. |
| FR-022 | Safe versioned configuration edits | Must | As a Company Administrator, I want newer policy preserved. A save must refer to the account-policy and bank-bounds versions read; if either changed, refuse the stale save, retain current state, and require reload before resubmission. Source: DEMO-MANDATE; DD-05, DD-19. |
| FR-023 | Explicit finality and no pending edits | Must | As an Initiator, I want unambiguous final outcomes. Pending request amount, destination, source, and note cannot be edited. No cancellation, expiry, or terminal-state reopening is supported. A new submission uses a new intent identifier and current policy. Source: DEMO-MANDATE; DD-10, DD-14. |

## 12 Non-Functional Requirements

No approved global NFR baseline was supplied or consulted. Targets below are
proposed local-demo criteria, not production SLAs. Named owners and applicable
enterprise standards remain production gates.

| NFR-# | NFR Name | Priority (MoSCoW) | Description |
|---|---|---|---|
| NFR-1 | Security & Authorization | Must | Authenticate all new actions; enforce company/account grants server-side and check current state on writes. Bank/institution auditors have explicit institution-scoped read exceptions. Unauthenticated requests fail; unauthorized identifiers disclose no cross-company data. Source: SUPPLIED-V1 NFR-1; DD-02, DD-16. |
| NFR-2 | Auditability & Compliance | Must | No application API may alter/delete audit evidence. Committed mutations require atomically durable audit evidence, queryable within 5 seconds in the demo. Retain request/decision/configuration evidence for 90 days; no expiry job is required for this demo, and records must not be purged during it. This is a minimum demo retention period, not a legal retention assertion. Source: SUPPLIED-V1 NFR-2; DD-15, DD-17, DD-20. |
| NFR-3 | Data Integrity & Reliability | Must | One final decision and at most one balanced posting per company intent; enforce safe same-key replay and reject changed-payload reuse. Conflicting decisions cannot both succeed. Mutation and evidence roll back together on failure. Source: SUPPLIED-V1 NFR-3; DD-12, DD-14, DD-15. |
| NFR-4 | Backward Compatibility | Must | Unenrolled transfer request/response contracts and account ownership remain unchanged. STAGED companies do not enforce thresholds. ACTIVE companies opt into new grants, states, and required intent identifiers. Source: SUPPLIED-V1 NFR-4; DD-01, DD-06, DD-14. |
| NFR-5 | Availability | Should | During a 30-minute local demonstration, no planned restart is required for policy edits. If policy, membership, or durable audit storage is unavailable, governed mutations fail closed with retry guidance; no fallback posting. Read views show unavailability, not invented zero values. Source: SUPPLIED-V1 NFR-5; DD-20. |
| NFR-6 | Performance | Should | At 10 concurrent authenticated users, 20 company accounts, and 1,000 request-history records, policy reads/list loads and decision/configuration submissions each meet P95 ≤2 seconds over at least 100 operations per operation type after warm-up on the documented test environment. Measure server response latency separately from render/usability. Source: SUPPLIED-V1 NFR-6; DD-20. |
| NFR-7 | Usability | Should | In a scripted pilot, all 3 first-time test participants save a valid threshold and understand Draft — not enforced within 2 minutes each without coaching. Save/error/conflict feedback is visible and actionable; preserve unsaved input after validation errors. Source: SUPPLIED-V1 NFR-7; DD-20. |
| NFR-8 | Configurability & Maintainability | Must | Valid configuration changes need no deployment/restart. Version policy decisions and preserve traceability; do not require production-wide auth replacement. Architecture/component design belongs in the solution plan. Source: SUPPLIED-V1 NFR-8; DD-19. |
| NFR-9 | Documentation & Enablement | Must | Deliver setup/reset guidance, role matrix, draft-versus-active warning, failure/retry guide, demo limitations, and relevant API/user-guide updates with each increment. Source: SUPPLIED-V1 NFR-9; DD-20. |
| NFR-10 | Accessibility | Must | New surfaces target WCAG 2.2 AA: keyboard operation, labelled inputs, announced validation/save results, visible focus, non-color-only status, and contrast checks. Record automated results and manual keyboard/screen-reader checks; do not assert certification. Source: DEMO-MANDATE; DD-20. |
| NFR-11 | Operability & Privacy | Must | Correlate policy saves and decisions without logging credentials, full account numbers, or free-text notes/reasons in operational logs. Surface failure counts and pending queue age to authorized operators. Demo data is synthetic; display account last four digits in lists. Source: DEMO-MANDATE; DD-18, DD-20. |
| NFR-12 | Deployability & Recovery | Must | Roll out STAGED configuration first; do not activate when dependencies are absent. Never roll back to an unenforcing build while ACTIVE accounts or pending requests exist; block governed writes and restore an enforcement-capable build. Recovery walkthrough must preserve committed policy/request/audit versions and show no duplicate posting after retry. No production RTO/RPO is claimed. Source: DEMO-MANDATE; DD-06, DD-20. |

## 13 UI / UX Considerations

### 13.1 User Flow Overview

1. Bank admin configures bounds and explicitly enrolls a STAGED demo company.
2. Company admin assigns responsibilities and configures each account.
3. In the first increment, show a persistent **Draft — not enforced** label,
   saved threshold/version, and no activation control. Existing transfers remain unchanged.
4. In the complete capability, bank activation validates all dependencies and
   account coverage before any transfer enforcement begins.
5. An ACTIVE-company initiator submits; the source policy either posts immediately
   or creates PENDING_APPROVAL. The confirmation must not call a pending transfer paid.
6. An eligible approver approves or rejects. Approval may produce COMPLETED or FAILED;
   an infrastructure failure leaves pending with retry guidance.

### 13.2 End-User Actions

View/save account settings; enable/disable rules where bank bounds permit;
configure self-approval; assign/revoke scoped grants; submit/view own requests;
review/approve/reject eligible requests; view authorized history and audit evidence.
Only bank administrators enroll/activate companies and edit bounds.
Auditors have read-only actions.

### 13.3 Layout & Display Considerations

- Policy editor: company/account identity, masked account number, USD, bank minimum/
  maximum, enabled state, saved threshold, policy/bounds versions, last actor/time,
  and draft/active status. No threshold value is prefilled for an unconfigured account.
- Explain “at or above requires approval,” not “maximum transfer amount.”
- Disabling a rule clearly says no transfer approval will be required when active;
  disabled rules have no effective threshold. Their former value remains in audit.
- Conflict messages require reload; never silently retry a save with newer versions.
- Decision views show amount/currency, identity, applied policy, current state,
  no-reservation warning, and safe action/error feedback.
- Notes and rejection reasons render as plain text. Sensitive detail is visible
  only to the entitled viewer. No new broad notifications are promised.

## 14 Dependencies

| DR-# | DR Name | Application/Area | Owner Name | Description |
|---|---|---|---|---|
| DR-1 | Ledger posting engine | Transfers / ledger | Tech Lead; individual not assigned | Validate current funds/status, concurrency, balanced at-most-once posting, and failure recovery. Not required to change for staged S3. |
| DR-2 | Authentication & authorization | JWT / current grants | Tech Lead; individual not assigned | Extend scoped permissions, do not trust UI-only gates or stale role claims. S3 needs real server-side company-admin checks. |
| DR-3 | Account & company model | Company membership / account links | Product Owner + Engineering; individuals not assigned | Explicit staged setup, ownership preservation, one company per member/account. Thin trusted fixture setup is permitted for S3, not fabricated client headers. |
| DR-4 | Transfer API contract | Interactive REST API | API platform owner; individual not assigned | Source-account governance, versioned policy evidence, company intent identifiers, unchanged unenrolled contract. |
| DR-5 | Event, audit & notification pipeline | Durable audit / existing consumers | Tech Lead; individual not assigned | Determine how atomic durable evidence coexists with current event consumers; do not equate queued delivery with recorded evidence. Notifications remain deferred. |
| DR-6 | Business & risk-owner decisions | Authorization policy | Business/Risk roles; individuals not assigned | DD-* closes demo behavior only. Production bank bounds, retention, self-approval, and lifecycle need human approval. |
| DR-7 | Client inputs | Synthetic Northstar engagement | Client Solutions; individual not assigned | No external integration needed for demo; original evidence and signed commercial scope required for real delivery. |
| DR-8 | Web channel | React application | Engineering + Design; individuals not assigned | New policy editor, queues, history, and oversight; no existing design artifact verified. |
| DR-9 | Applicable governance | Architecture principles/standards | Enterprise Architect; individual not assigned | Not consulted in this document revision. Identify approved sources and consult architecture-north-stars before SDD constitution/spec checkpoint; standards/ADRs before plan. |

## 15 Risks & Assumptions

Original IDs are retained; descriptions are updated for this edition.

| RA-# | Type | Description | Impact | Mitigation/Notes |
|---|---|---|---|---|
| R-1 | Risk | Synthetic evidence, no validated demand | Commercial value unproven | Do not present demo targets as business outcomes |
| R-2 | Risk | Configuration complexity | User error/support cost | Minimal hierarchy; no inherited company threshold; usability check |
| R-3 | Risk | Authorization changes disturb existing behavior | Transfer regressions | Explicit enrollment; compatibility tests; source-account enforcement |
| R-4 | Risk | No implemented company structure today | Foundational scope underestimated | S3 includes only necessary staged identity/access enablers; full feasibility still pending |
| R-5 | Risk | No reservation while pending | Approval can fail for lack of funds | Warn both parties; current-balance check; explicit FAILED state |
| R-6 | Risk | Self-approval weakens segregation | Governance exposure | Initially false; bank bounds; current entitlement checks; audit |
| R-7 | Risk | Saved draft mistaken for enforcement | False assurance | Persistent draft label; activation unavailable in S3 |
| R-8 | Risk | Existing async audit is assumed sufficient | Mutation may lack durable evidence | Atomic durable evidence requirement and fault tests |
| R-9 | Risk | Active governance bypass through legacy ownership path | Unauthorized posting | Enforce enrollment on every transfer entry point; negative regression tests |
| R-10 | Risk | Pending requests stranded by changed permissions | Operational deadlock | Reject changes removing all eligible decision makers; bank restrictions that strand pending requests also rejected |
| R-11 | Risk | Immutable history contains sensitive free text | Excess data retention | Synthetic data only; bounded text; masking; no free text in operational logs; production privacy review |
| A-1 | Assumption | No supported machine-consumer product is introduced | — | No exemption from controls for direct API calls |
| A-2 | Assumption | Pending requests never post or reserve funds | — | DD-09 |
| A-3 | Assumption | No current scheduled/recurring transfer capability is relied upon | — | Future scheduling needs its own requirements |
| A-4 | Assumption | Sources and DD-* values are illustrative, not approved commitments | — | Production sign-offs remain open |
| A-5 | Assumption | Code establishes current behavior, not approved business policy | — | No unconditional exactly-once baseline claim |
| A-6 | Assumption | Existing roles are extended, not replaced | — | Scoped grants only for enrolled accounts |
| A-7 | Assumption | Dedicated synthetic accounts/users can be provisioned in a local demo | — | No real account migration or PII required |

## 16 Appendix

### 16.1 Change History

| Version | Published | Changed By | Comment |
|---|---|---|---|
| 1 | 2026-10-30, as reported in supplied draft | Droid, per supplied version | Original synthetic CI-01 PRD. Not present as a local file; no approval asserted. |
| 2 | 2026-11-09 | Droid, at user's request | Rewrote §§1–16 to close demo policy gaps; preserved existing AC/FR/NFR/DR/RA IDs; added DD-* register, staged rollout, lifecycle, authorization boundary, integrity expectations, measurable demo targets, and S3 handoff. DRAFT, not approved. |
| 3 | 2026-11-09 | Droid, at user's request | Aligned §§1, 3–5, 16.1, 16.3–5 to the supplied hands-on exercise; selected standalone S5-EVAL with user-confirmed no-live-integration scope; added DD-21/22 and evaluator verification reference; retained S3 and all existing IDs. DRAFT, not approved. |

**Downstream re-sync:** S3 and `specs/001-account-approval-threshold/` retain their
version 2 configuration scope. The selected exercise now uses S5-EVAL and
`specs/002-evaluate-approval-threshold/`, citing version 3. No Jira epic, approved
solution design, or prototype is provided. Future Jira synchronization must carry
the evaluator-only boundary and distinguish missing configuration from no rule.

### 16.2 Stakeholder Matrix

No individual appointments or contact details are invented. All rows below require
named ownership before real delivery; role ownership is sufficient for the draft exercise.

| Area | Name | Role | Responsibility | Email | Group Email |
|---|---|---|---|---|---|
| Technical Product Management | Not assigned | Product Manager / Product Owner | Scope, outcomes, policy review, story acceptance | Not supplied | Not supplied |
| Engineering | Not assigned | Technical Lead | Feasibility, implementation plan, integrity | Not supplied | Not supplied |
| Quality Engineering | Not assigned | QE Lead | Scenario matrix, fault/concurrency evidence | Not supplied | Not supplied |
| Product Design | Not assigned | Designer | Policy editor and decision usability | Not supplied | Not supplied |
| Enterprise Architecture | Not assigned | Architect | Identify governing sources and reviews | Not supplied | Not supplied |
| Technical Writing (TKS) | Not assigned | Technical Writer | Guides and demo limitations | Not supplied | Not supplied |
| Implementations & PS | Not assigned | Implementation Manager | Enrollment/setup | Not supplied | Not supplied |
| Customer Support | Not assigned | Support Manager | Failure and pending-request playbook | Not supplied | Not supplied |
| Service Delivery & SRE | Not assigned | SRE Lead | Degraded behavior, recovery evidence | Not supplied | Not supplied |
| Legal | Not assigned | Legal Counsel | Commercial authorization, if pursued | Not supplied | Not supplied |
| Compliance & Regulatory | Not assigned | Compliance Analyst | Real retention/privacy review | Not supplied | Not supplied |
| Product Marketing | Not assigned | Product Marketer | No unsupported commercial claims | Not supplied | Not supplied |
| Business & Risk | Not assigned | Policy owner | Replace demo bounds with approved policy | Not supplied | Not supplied |
| Client Engagement | Not assigned | Client Solutions | Verify original client evidence | Not supplied | Not supplied |

### 16.3 Program Validation & Metrics

All numeric targets below are proposed demo criteria. Actual results must be recorded
after execution; none has been measured by preparing this document.

| Aha! OKR | What is Measured? | How is it Measured? | What does "Good" look like? |
|---|---|---|---|
| No verified objective; selected local exercise only | S5-EVAL capability: baseline 0 standalone threshold evaluators in the inspected transfer module | Exercise all nine source criteria through the unit matrix; see [verification](../../specs/002-evaluate-approval-threshold/verification.md) for actual results | 1 component, all nine criteria covered, zero changes to live transfer execution |
| No verified objective; local demo only | S3 capability: baseline 0 implemented company threshold editors in inspected platform | Execute S3 scenarios with authorized/unauthorized personas and persisted reloads | 1 demonstrable editor; all acceptance scenarios pass; 0 ledger mutations |
| Same gap | Governed workflow coverage: baseline 0 implemented pending approval workflows in inspected transfer service | Full-program scripted submissions, decisions, failures, and history checks | 100% expected outcomes across the published scenario set |
| Same gap | Integrity defects; no measured production baseline | Parallel decisions, retries, changed-payload replays, simulated failures; inspect postings and evidence | 0 unauthorized/duplicate/partial postings in executed cases |
| Same gap | Configuration response latency; baseline not measured | NFR-6 workload and documented hardware/environment | P95 ≤2 seconds per measured operation type |
| Same gap | Draft-label comprehension; baseline not measured | NFR-7 three-participant pilot | 3/3 complete and understand non-enforcement within 2 minutes each |

Real business measures (approval adoption, decision time, support effort) need
baselines, valid denominators, owners, and current Aha! alignment before
commercialization. The template's 2026 DBB-G-25–28 list is historical reference,
not a selected or verified objective. No live Aha! authentication claim is made.

### 16.4 Proposed Demo Decision Register

Each decision has status **PROPOSED FOR DEMO**. User authorization to flesh out a
demo is not equivalent to approval of these particular policies. Review them
before planning implementation; retain this distinction in later artifacts.

| ID | Decision | Rationale / authority boundary |
|---|---|---|
| DD-01 | Govern by source-account enrollment on every server-side transfer entry point. No client-controlled channel bypass. Unenrolled accounts remain legacy. | Resolves interactive/API ambiguity without building a machine-integration product. |
| DD-02 | One institution; one company per member/account. Bank admin enrolls only dedicated demo accounts, preserving owner fields. Company-admin membership and account-scoped payment grants are separate. Membership removal also removes grants and must obey DD-13. | Minimal demo identity model, not full corporate onboarding. |
| DD-03 | Illustrative initial bank bounds: USD minimum threshold **0.01**, maximum **100,000.00**, inclusive; rule disabling allowed; self-approval not allowed. Bank admin may change bounds through validated configuration. | These numbers/booleans are demo choices, not platform defaults or sourced bank policy. Missing bank bounds block company saves and activation. ACTIVE governed writes fail closed if required bounds are unreadable/missing. |
| DD-04 | No inherited company threshold and no automatic per-account threshold. Account policy is unconfigured, explicitly disabled (effective threshold null), or enabled with a valid threshold. USD only, at most two decimal places, no rounding. Disabling clears the current threshold; audit preserves the old value; re-enabling requires an explicit value. | Keeps precedence unambiguous and avoids silently inventing customer policy. |
| DD-05 | Reject an entire bank-bounds update if any saved policy, company self-approval setting, or pending request's eligible-approver coverage would become invalid. Do not clamp or partially apply it. Validate against current versions at commit. | Prevents surprise weakening, invalid staged settings, and stranded pending work. |
| DD-06 | Company lifecycle: STAGED → ACTIVE only. STAGED saves persist but do not govern transfers; show Draft — not enforced. Activation is bank-admin-only, unavailable until complete enforcement ships, and requires all linked accounts configured and each account to have an initiator plus a distinct eligible approver. No deactivation or account reassignment in demo. | Allows honest configuration-only delivery; production rollback must not remove enforcement. |
| DD-07 | Any one current account approver may decide. Current membership/grants are rechecked server-side. Self-approval and self-rejection require bank and company permission plus approver grant; company setting initially false. Admin status never confers payment rights. | Single decision model with explicit segregation of duties. |
| DD-08 | At submission, require at least one eligible approver for that initiator when approval is needed. Otherwise reject without creating a request or posting. Frozen source/destination/amount/currency/note cannot be edited. | Avoids knowingly creating an unresolvable request. |
| DD-09 | Do not reserve funds. At approval recheck source/destination existence, active status, matching currency, and current balance under posting integrity controls. No negative balance permitted. | No new reservation engine in demo. |
| DD-10 | States: PENDING_APPROVAL → COMPLETED, REJECTED, or FAILED. Straight-through success is COMPLETED. Insufficient funds/inactive accounts at approval yield final FAILED with failure evidence and no posting. Unexpected infrastructure failure leaves PENDING_APPROVAL and no recorded successful decision. No automatic expiry/cancel/reopen/retry. | Separates authorization from successful execution. |
| DD-11 | Snapshot threshold, account-policy version, bank-bounds version, and company-policy version at submission. Changes never auto-release pending requests. Current decision permissions/self-approval restrictions still apply. | Historical explanation without honoring revoked authorization. |
| DD-12 | Serialize final decisions. Same action by the same actor replay returns the recorded outcome without new side effects; a conflicting action or another actor's late decision returns already-decided conflict with authorized state. A final FAILED approval is also replayable. Rejection requires a 1–500 character reason after trimming. | Exactly one final business decision; no duplicate posting or audit. |
| DD-13 | Permit grant/membership/self-approval changes only when each pending request retains at least one currently eligible approver. Bank-bound changes obey the same guard. Pending requests may still fail financially at decision time. | Covers revocation without freezing old grants. |
| DD-14 | Require an opaque client-generated intent identifier for ACTIVE-company submissions, scoped to institution/company/initiator and retained with the request for at least 90 days. Same key/same payload returns original outcome; same key/changed payload is rejected. No semantic dedupe across different keys. Legacy optional idempotency behavior stays unchanged for unenrolled and STAGED transfers. | Bounded duplicate-intent guarantee, not a claim that identical payments are always duplicates. |
| DD-15 | Persist mutation and durable audit evidence atomically; asynchronous display/event delivery may follow only if durable evidence already exists. Audit unavailability blocks the mutation. Successful no-op replays create no extra change/decision record; refusals go to security evidence. | Reliability requirement, not a prescribed storage architecture. |
| DD-16 | Company admins/auditors read company settings and requests; initiators read own requests; current account approvers read account requests. Bank admins/institution auditors read institution-wide oversight. Only bank/company admins perform their assigned administration; no auditor mutates. | Explicit exceptions to company isolation, with least privilege. |
| DD-17 | Minimum demo evidence retention: 90 days from event/final decision; pending records stay until final then retention applies. No purge workflow needed for the exercise and no demo reset may silently delete evidence. Production period/deletion/legal hold remain unapproved. | Not a regulatory retention assertion. |
| DD-18 | Synthetic data only. Account last four digits in lists; full deciding account detail only for entitled viewers. Plain-text notes/reasons with bounded length; no credentials/account numbers/free-text payment detail in operational logs. | Basic demo data minimization. |
| DD-19 | Saves include expected account-policy and bank-bounds versions; first policy creation uses an explicit unconfigured version. Competing changes yield a conflict and require reload. No-op saves with current versions succeed without incrementing versions or generating change evidence. | Prevents lost updates and misleading audit noise. |
| DD-20 | Adopt NFR-5–12 numeric/local-demo targets as proposed verification criteria. All require execution evidence; none is a measured result or production commitment. | Makes demonstration testable without inventing commercial facts. |
| DD-21 | For the supplied hands-on exercise, implement a standalone evaluator and unit tests only, as explicitly selected by the user. It consumes an authoritative applicable-rule snapshot, never reads/writes live accounts, and is not invoked by TransferService. An absent applicable rule includes an explicitly disabled rule resolved by a future adapter. A present rule with a missing threshold is not absence. | Bounded exercise; configuration UI, rule lookup/storage, bank-bound administration, live transfer integration, pending states, decisions, posting changes, and audit mutations remain future work. No claim of enforced approval is permitted. |
| DD-22 | Evaluator outcomes are Approval required, Approval not required, or Invalid configuration. For a present rule, a missing/nonpositive/over-precision/out-of-representable-range threshold yields Invalid configuration, never straight-through permission. Valid transfer amounts must be positive with at most 15 integer and 2 fractional decimal digits, matching the existing request contract. Invalid amounts or missing rule-selection input are rejected. Equality is numeric (not decimal representation), no rounding/default threshold is allowed, and no bank-bound fixture value is hard-coded into the evaluator. | Explicit demo behavior for the exercise's missing/invalid-threshold edge cases. Caller must supply amounts/thresholds in the same account currency; no conversion or production-policy approval is implied. |

### 16.5 Readiness and Open Questions

**Selected exercise:** S5-EVAL is specified, designed, implemented, and unit-verified
as a standalone component; see its linked verification record. This does not
deliver live enforcement. S3 configuration remains separate deferred work.
**Full program is not production/build ready:**
no approval, named delivery owners, applicable governance review, architecture
feasibility confirmation, complete epic decomposition, or measured evidence exists.

- **Product Manager:** confirm demo choices; appoint owners; validate demand,
  current objectives, commercial scope, and real policy if pursued.
- **Product Owner:** accept S3 scope and staged wording; review DD-* with Risk;
  prioritize later activation/transfer stories; supply Jira parent only if wanted.
- **Tech Lead / Architect:** identify applicable governance, run the spec/plan
  checkpoints, choose minimal identity/audit enablers, validate current contract
  and concurrency/recovery approach. No new implementation design is approved here.
- **Risk / Compliance / SRE:** replace demo bounds/retention/NFRs with approved
  values before production; define legal hold, deletion, RTO/RPO, and support model.
