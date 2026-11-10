<!--
Sync Impact Report (review scratch; remove before committing after human review)
Version: 0.1.0 -> 0.2.0 (expanded demo scope, still not ratified).
Trigger: user selected exercise-aligned standalone evaluator and unit tests.
Principles: I-V unchanged; scope now includes S5-EVAL as the selected exercise.
Added sections: none. Removed sections: none.
Follow-up: human ratification; identify applicable enterprise/portfolio governance;
consult principles before asserting alignment and standards/ADRs before planning.
Dependent templates/commands: unchanged; they read this constitution at runtime.
-->
# BankingSystem Demo Constitution

**Status: Draft local guardrails, not ratified.** This governs the proposed SDD
demonstration only; it is not an approved FIS architecture policy or production
authorization. It captures existing platform constraints and the supplied demo PRD.

## Core Principles

### I. Explicit Scope and Honest Policy Authority

Every feature MUST identify its source requirements and distinguish current
behavior, proposed demo decisions, and approved requirements. Demo numbers MUST
NOT be described as bank policy, platform defaults, or measured business results.
Unresolved substantive choices MUST be surfaced rather than hidden in a plan.

Source: [demo PRD](../../docs/product/dbb-prd-transaction-limits-approval-controls.md),
§§1, 5, 16.4. Rationale: the evidence and new decisions are synthetic.

### II. Preserve Payment Integrity and Existing Contracts

Changes MUST preserve balanced posting, prevent unauthorized or duplicate money
movement within their specified guarantees, and maintain existing contracts for
unenrolled accounts. Configuration-only features MUST NOT mutate balances,
ledger entries, account ownership, or transfer behavior.

Source: [README](../../README.md), account/ledger/transfer descriptions; demo PRD
FR-017 and NFR-3–4. These are requirements to verify, not claims that every
existing path has already been proven correct.

### III. Enforce Current, Scoped Authorization

New company actions MUST authenticate the user and enforce current membership,
responsibilities, and company/account scope on the server. UI visibility, stale
session claims, account ownership, and global administration MUST NOT implicitly
grant company policy-writing or payment-decision rights. Explicit bank oversight
and read-only auditor scopes MUST be distinguished from mutation rights.

Source: demo PRD NFR-1, DD-02, DD-07, DD-16.

### IV. Durable, Explainable Configuration

Committed policy mutations MUST be accompanied by atomically durable audit
evidence. Version conflicts MUST NOT overwrite newer policy or bounds.
STAGED settings MUST be labelled **Draft — not enforced**, and activation MUST
remain unavailable until the complete control dependencies are delivered.

Source: demo PRD FR-014, FR-020, FR-022, DD-06, DD-15, DD-19.
Rationale: a saved configuration is not proof of enforced payment protection.

### V. Traceable Verification Before Completion Claims

Specs MUST describe testable user outcomes; plans and tasks MUST trace to them.
Acceptance MUST include relevant success, denial, conflict, failure, and
compatibility evidence. Checks MUST be reported as passed, failed, or not run;
document quality reviews MUST NOT be presented as application test results.
Test data MUST be synthetic, and evidence MUST exclude secrets and full account numbers.

Source: demo PRD NFR-9–12. Existing repository conventions determine concrete
commands at plan time.

## Demo Constraints and Authority

- The selected exercise is S5-EVAL standalone account-threshold evaluation and
  unit tests. S3 staged configuration remains separate future work. Neither
  connects to live transfer submission or provides the complete approval workflow.
- Use the existing platform as implementation context. The spec MUST NOT choose
  a replacement stack or redesign unrelated behavior.
- No real-money activity, production deployment, customer data, or external
  platform writes are authorized by the documentation workflow.
- **Enterprise/portfolio governance: NOT CONSULTED.** Applicable owners and
  sources have not been identified. The user explicitly chose to continue with
  demo requirements and this limitation on 2026-11-09.
- No conclusion of corpus silence, compliance, exemption, or approved alignment
  may be inferred. The architecture checkpoint remains open.

## Delivery Workflow and Evidence

1. Assess intake; identify baseline, intended outcomes, exclusions, and dependencies.
2. Consult applicable principles when available. If unavailable, record why and
   continue only work not claiming governance approval.
3. Specify what and why; clarify material ambiguity before planning.
4. Before technical planning, revisit governance and consult applicable
   standards, ADRs, and reference architectures through architecture-north-stars.
5. Plan, derive tasks, and analyze cross-artifact consistency before implementation.
6. Implement only with user instruction; validate against acceptance and relevant
   existing checks. Preserve uncommitted user work.

No planning, implementation, or test execution is implied by creating this file.

## Governance

This document is a draft working constraint for the demo. It does not supersede
repository instructions, enterprise requirements, or authorized risk decisions.
The Product Owner and Tech Lead must review it before treating it as adopted;
the responsible Architect must identify applicable external governance.

Amendments MUST name their trigger and affected principles, update this version,
and identify dependent artifacts requiring review. Use a major bump for an
incompatible principle change, minor for a new/expanded principle, and patch for
wording-only clarification. While unratified, retain the 0.x draft status.

Spec/plan reviews MUST record adherence or an explicit unresolved divergence.
Human approval and unavailable governance MUST never be marked complete by the
assistant. Initial ratification date is intentionally absent because no
ratification has occurred; this is not a template placeholder.

**Version**: 0.2.0 | **Ratified**: Not ratified (draft) | **Last Amended**: 2026-11-09
