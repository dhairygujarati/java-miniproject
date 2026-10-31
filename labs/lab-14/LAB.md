# Lab 14 — MiniBank Capstone (Part B)

This lab assembles the semester project: model + service + console UI, saved state, transaction logs, reports, concurrent processing, and tests.

## Run the integrated application (JDK 17+)

On Windows, open this folder in Command Prompt and run `run.bat`.

## Concepts demonstrated
- Layered design: model, service, utilities, and menu UI.
- Repository CRUD with reflection-based `@Id` lookup.
- Validation and checked banking exceptions.
- File persistence, transaction log, and report generation.
- Thread-safe account storage and a four-thread transaction processor.

The integrated app is in `src/main/java/minibank`. The small example below illustrates the layered pattern separately.
