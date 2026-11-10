# Organizer Preparation: Spec-Driven Development Demo

Use this checklist to prepare a reproducible session for the BankingSystem
approval-threshold evaluator exercise.

## 1. Choose the Session Mode

Decide whether attendees will:

- **Walk through the completed cycle:** provide the finished workspace and use
  it to explain each artifact and the test evidence; or
- **Do the cycle hands-on:** provide a starter state with the PRD, baseline
  application, and exercise brief, but without the completed S5-EVAL spec,
  evaluator implementation, or evaluator tests.

Do not use the completed feature as the hands-on starter if attendees are
expected to specify and implement it themselves.

## 2. Prepare a Reproducible Starter

- Provide a repository branch, archive, or managed workspace attendees can
  access before the session.
- Include the synthetic PRD at
  `docs/product/dbb-prd-transaction-limits-approval-controls.md` and the
  standalone evaluator exercise brief/story.
- Include the existing backend baseline and Spec Kit configuration needed by
  the chosen coding-agent integration.
- For the hands-on mode, keep the completed evaluator and its final artifacts
  out of the starter. Retain a separate facilitator copy for reference.
- Confirm attendee copies do not contain personal data, credentials, or
  unrelated local changes.

**Workspace note:** The exercise artifacts in the current working directory
have not been committed. Attendees should not be told that a fresh clone of the
repository will contain these local changes. Prepare a reviewed starter branch
or export the required files before distributing access.

## 3. Validate Tools and Baseline

For this exercise, verify:

- Java 21 is available.
- The repository Maven wrapper can run and download dependencies.
- Spec Kit is initialized for the selected agent and its workflow commands are
  available.
- The agent can inspect files, make local edits, and run tests.
- Attendees have a clean, isolated working copy or branch.

From the repository root on Windows PowerShell, check the Java version and
baseline compile:

```powershell
java -version
cd backend
.\mvnw.cmd -B "-DskipTests" test-compile
```

No Docker runtime, database, Jira, or Confluence access is needed for this
standalone unit-test exercise. Allow time or provide a prewarmed environment for
first-time Maven dependency downloads.

## 4. Set the Scope and Guardrails

State these boundaries at the beginning:

- The exercise implements an account-level threshold evaluator only.
- It does not look up or persist account rules.
- It does not connect to `TransferService`, submit payments, or enforce
  approvals.
- The PRD is synthetic demo material. Its policy assumptions are not approved
  production requirements.
- Governance was not consulted for the exercise, and the local constitution is
  an unratified draft.
- S3 configuration editing is separate, deferred work.

For a real product exercise, replace synthetic assumptions with authoritative
policy sources and named decision-makers. Mark unanswered governance questions
as unresolved rather than presenting proposed demo policy as approved.

## 5. Prepare the Walkthrough

Have these artifacts ready in sequence:

1. PRD and exercise brief/story.
2. `spec.md` after specification and clarification.
3. `plan.md` and `design.md`.
4. `tasks.md` and the analyze findings.
5. The implementation and unit tests.
6. `verification.md` with commands, actual counts, skipped checks, and scope
   limitations.

For a completed-demo walkthrough, the focused test command is:

```powershell
cd backend
.\mvnw.cmd -B "-Dtest=ApprovalThresholdEvaluatorTest,TransferServiceCompatibilityTest" test
```

The recorded focused run passed 37 tests. The broader recorded non-container
unit selection passed 42 tests. Container-backed integration tests were not run
because Docker was unavailable; do not imply otherwise.

## 6. Keep the Session Safe and Honest

- Do not use production accounts, data, or credentials.
- Do not connect the evaluator to live transfer code as an unplanned demo
  shortcut.
- Do not create Jira/Confluence records or commit/push changes unless that work
  is explicitly authorized and prepared.
- If a check fails in the session, show the failure and investigate it. Do not
  change the spec or weaken a test merely to make the demo appear green.
- Distinguish document-quality checks from business, governance, or production
  approval.
