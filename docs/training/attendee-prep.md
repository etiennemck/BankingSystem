# Attendee Preparation: Spec-Driven Development Demo

This guide covers what to prepare before the hands-on BankingSystem exercise.
The exercise follows one feature through:

**Specify → Clarify → Plan → Tasks → Analyze → Implement → Verify**

## Before the Session

### 1. Get the workshop starter repository

Use the starter repository or workspace provided by the organizer. It should
contain:

- The synthetic transaction-limits and approval-controls PRD.
- The existing BankingSystem backend code that the exercise builds on.
- A short exercise brief for the standalone account approval-threshold evaluator.

If you are using your own repository instead, choose one small feature and bring
its PRD or equivalent product brief. The feature should be narrow enough to
specify, implement, and test in one session.

### 2. Prepare your workspace

- Have Git and a terminal available.
- Use a fresh branch or workshop copy. Do not work over valuable uncommitted
  changes.
- Have Java 21 installed. The project supplies a Maven wrapper, so a separate
  Maven installation is not required.
- Allow network access on the first build so the wrapper and Maven dependencies
  can be downloaded.

For a Windows PowerShell workspace, check Java and compile the existing backend
from the repository root:

```powershell
java -version
cd backend
.\mvnw.cmd -B "-DskipTests" test-compile
```

The exercise uses unit tests only. Docker is not needed.

### 3. Check your Spec Kit and coding-agent setup

- Have access to an AI coding agent that can read and edit the repository and
  run local commands.
- Install or initialize Spec Kit for that agent in the repository.
- Confirm the workflow commands are available in the agent chat:
  `/speckit-specify`, `/speckit-clarify`, `/speckit-plan`, `/speckit-tasks`,
  `/speckit-analyze`, and `/speckit-implement`.
- For this demo, the agent integration is Droid. Equivalent Spec Kit integrations
  can be used if the organizer confirms them in advance.

If Spec Kit is already initialized in the supplied starter, you do not need to
initialize it again.

### 4. Bring the right source material

Read the exercise brief and PRD before the session. Note questions about user
outcomes, boundary cases, and scope. For this exercise, the evaluator is
standalone: it returns a decision but is not connected to transfer submission
or payment enforcement.

Do not bring production credentials or customer data. This is a synthetic
training exercise, not an authorization to change a live banking workflow.

## Not Required

- Docker or a running database.
- Jira or Confluence access.
- A global Maven installation.
- Production credentials or real customer information.

## If Something Is Not Ready

Tell the organizer before the session if Java is not available, the baseline
compile fails, or the Spec Kit commands do not appear in your coding agent. That
leaves time to use the prepared workshop environment or troubleshoot setup.
