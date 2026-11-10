# Facilitator Script: Account Approval-Threshold SDD Exercise

Use this guide with the public
[starter branch](https://github.com/etiennemck/BankingSystem/tree/workshop/sdd-approval-threshold-starter).
This script and the completed reference artifacts belong in the facilitator
workspace, not in the starter branch.

The core exercise follows the slide's suggested 10/10/15-minute timeboxes.
Treat these as workshop pacing, not a guarantee that every group will finish
implementation in 35 minutes.

## Before the session

- Give attendees the starter branch URL and ask them to work in a fresh clone or
  isolated worktree.
- Confirm they have Git, an AI coding agent with the `/speckit-*` commands
  available, and Java 21. The first Maven build may need network access.
- Ask them to run this baseline check from the repository root in PowerShell:

  ```powershell
  java -version
  cd backend
  .\mvnw.cmd -B "-DskipTests" test-compile
  ```

- Keep your completed reference workspace separate from attendee copies. Do
  not open the completed spec or implementation until the hands-on work is
  finished.
- No Docker, database, production credentials, customer data, Jira, or
  Confluence access is needed.

## Opening — 2 minutes

**Say:**

> We are going to take a small product request through specification, planning,
> implementation, and verification. The source material is a synthetic PRD and
> an exercise story, not approved banking policy.
>
> Our scope is only a standalone evaluator that can classify an account-level
> threshold decision. We are not looking up account rules, changing an account,
> connecting to a transfer, or enforcing an approval. Live transfer behavior
> must remain unchanged.

**Set expectations:**

- The starter intentionally has no completed feature spec, evaluator code, or
  evaluator tests.
- Prefer asking the agent to work from the source documents over giving it a
  solution design up front.
- Keep proposed or unresolved policy decisions visible in the artifacts.
- If the group does not finish implementation in the timebox, stop at a useful
  checkpoint and use the reference walkthrough for the remaining stage.

## Exercise 1: Specify — 10 minutes

**Open together:**

- `docs/product/dbb-prd-transaction-limits-approval-controls.md`
- `docs/product/stories/S5-EVAL-account-approval-threshold-evaluation.md`

**Say:**

> First, turn the product intent into observable behavior. Keep the spec focused
> on what the evaluator must do, not on Java classes or implementation choices.
> Use the PRD and story as sources, and flag anything they do not settle.

**Ask the group:**

- What should happen below, at, and above the threshold?
- Is “no applicable rule” the same thing as a present rule with missing or
  invalid configuration?
- Which amount and threshold inputs are valid? Should anything be rounded?
- What does this feature explicitly not do?
- What evidence would show that existing transfers still behave as before?

**Have attendees run:**

1. `/speckit-specify`, using the story and PRD paths above as source material.
2. `/speckit-clarify`, to resolve or record the important ambiguities in the
   generated spec.

**Checkpoint:**

- Read the resulting `spec.md` aloud against the source story.
- Check that outcomes are testable, edge cases are explicit, and the standalone
  boundary is stated.
- Ask: “Could someone write tests from this spec without guessing what success
  means?”

**Expected exercise artifact:** `specs/<feature>/spec.md`. Spec Kit may also
create supporting artifacts as the workflow continues.

## Exercise 2: Plan and create tasks — 10 minutes

**Say:**

> Now inspect the existing code and turn the behavior into the smallest safe
> design and an ordered task list. Do not assume this requires a new endpoint,
> database field, or change to the transfer flow. Let the codebase and the scope
> decide.

**Ask the group:**

- Which existing components are relevant, and which must remain untouched?
- Where should evaluation live if it has no runtime caller in this exercise?
- How will tests cover the specification, including compatibility?
- Which decisions belong to a future integration rather than this slice?

**Have attendees run:**

1. `/speckit-plan`
2. `/speckit-tasks`
3. `/speckit-analyze`, if time permits, to find mismatches before coding.

Invite them to inspect the existing transfer service and related tests. The
goal is to use repository evidence, not to edit those files.

**Checkpoint:**

- Can each requirement be traced to a task or test?
- Do the tasks preserve the standalone boundary?
- Does the analysis identify and resolve any inconsistency before
  implementation?

**Expected exercise artifacts:** `design.md` and `tasks.md`. The Spec Kit
workflow may additionally create `plan.md`, research, or data-model artifacts;
use those where helpful, but keep the exercise checkpoint focused.

## Exercise 3: Implement and verify — 15 minutes

**Say:**

> Implement from the approved task list, not from an improvised code prompt.
> Start with tests for the specified cases. If a test exposes a gap in the
> requirement or plan, update those artifacts explicitly and regenerate the
> affected work. Do not silently change the meaning of the requirement or
> weaken a test to get a green result.

**Have attendees run:**

1. `/speckit-implement`
2. Review the changed-file list and confirm the transfer path remains untouched.
3. From `backend`, run the focused evaluator and compatibility tests:

   ```powershell
   .\mvnw.cmd -B "-Dtest=ApprovalThresholdEvaluatorTest,TransferServiceCompatibilityTest" test
   ```

If Maven is run from the repository root, first use `cd backend`. The focused
command is expected to be run after implementation has created those test
classes.

**Checkpoint:**

- Do the tests cover the scenarios and edge cases in `spec.md`?
- Does the no-rule case preserve the existing behavior without inventing a
  default?
- Are failures investigated against the spec and plan rather than patched over?
- Has the implementation stayed standalone, with no transfer-service,
  persistence, API, or UI integration?

If a group is still implementing at the end of the timebox, have them state
which task is next and what verification is still missing. Do not present an
unfinished or failing run as a successful result.

## Debrief — 5 minutes

Use this section after attendees have had time to form their own solution.
These are the reference decisions in the facilitator copy:

- A valid amount with no applicable rule returns `NOT_REQUIRED`; no default
  threshold is invented.
- Below a valid threshold returns `NOT_REQUIRED`; equal to or above it returns
  `REQUIRED`.
- A present rule with a missing or invalid threshold returns
  `INVALID_CONFIGURATION`.
- Invalid amount or missing rule-selection input is rejected.
- The evaluator is stateless and uses exact decimal comparison.
- `TransferService` remains unchanged; the evaluator is neither registered nor
  invoked by the live application.

The completed focused run recorded **37 passing tests**: 35 evaluator cases and
2 transfer-compatibility checks. The broader recorded non-container selection
passed 42 tests. Container-backed integration tests were not run because
Docker was unavailable; do not imply they passed.

**Ask:**

- Which ambiguity took the most discussion to resolve?
- What would need to be decided before a real caller could use this evaluator?
- What should be carried into a future Jira story or product decision?

No Jira issue was supplied or created for this exercise. If discussing a future
Jira sync, capture the acceptance criteria, unresolved policy questions, and the
no-live-integration boundary. Do not create or update an issue during the
workshop.

**Close with:**

> This exercise demonstrates a development workflow, not approval of a banking
> policy or production architecture. Governance was not consulted, and the
> repository constitution is an unratified draft. A real implementation would
> need authoritative policy owners, governance review, and a separately scoped
> integration and approval lifecycle.

## Recovery notes

- **Spec Kit command unavailable:** Check that the attendee is in the starter
  repository and that their selected agent has the repository's Spec Kit
  commands configured. Do not switch them to the completed workspace.
- **Baseline compile fails:** Confirm Java 21 is active and allow the Maven
  wrapper to download dependencies. Record the actual failure; do not claim the
  baseline passed.
- **Build or test fails during implementation:** Read the first relevant
  compiler or test failure, compare it with the spec and task, and decide whether
  the artifact or implementation needs correction. Keep the failing evidence.
- **Time runs short:** Preserve the current artifacts and move to the next stage
  as a facilitator-led walkthrough. The learning goal is traceability from
  source to spec, tasks, tests, and evidence, not typing speed.
