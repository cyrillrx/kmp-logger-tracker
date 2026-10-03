# KMP Logger & Tracker — Contributor Guide

Central reference for all contributors (human or AI). For the project pitch and usage, see [`README.md`](README.md).

## 1. Product Context

This repository publishes two Kotlin Multiplatform libraries meant to be shared by several personal projects, each in its own repository:

- **Logger** — severity-based logging, dispatched to pluggable children (console, Logcat, `os_log`, Sentry).
- **Tracker** — analytics event tracking, dispatched to pluggable children (Firebase Analytics), with an enriched context.

Rules that follow from being a library:

- **The public API is a contract.** Any change to it goes through an ADR ([`git-and-collaboration.md` §9](https://github.com/cyrillrx/coding-conventions/blob/main/collaboration/git-and-collaboration.md)) and is caught by the ABI validation.
- **Privacy is a library guarantee, not a consumer's chore.** Third-party children send nothing without consent, and data goes through redaction before leaving the device ([ADR-003](docs/adr/adr-003-consent-and-redaction.md)).
- **A misbehaving child never crashes the host app.**

> [!IMPORTANT]
> The repository is being realigned. [`docs/audit-2026-10.md`](docs/audit-2026-10.md) describes the state it was found in, and [`docs/roadmap.md`](docs/roadmap.md) the phases that bring it in line. Where the code and the roadmap disagree, the code describes today and the roadmap describes the target.

## 2. Project Guidelines and Conventions

The conventions live in the shared [`cyrillrx/coding-conventions`](https://github.com/cyrillrx/coding-conventions) repository — the single source of truth. The documents below are thin pointers to it; some add project-specific bindings (marked _+ project_). Do not duplicate the shared rules here.

### Collaboration and Communication

- **Collaboration, Git & CI Conventions**: [`git-and-collaboration.md`](docs/conventions/git-and-collaboration.md) _(+ project)_
- **Documentation Conventions**: [`docs-conventions.md`](docs/conventions/docs-conventions.md) _(pointer)_
- **Documentation language**: All documentation, comments, commit messages, and PR descriptions must be written in English.
- **AI Co-authorship**: Do not add AI co-author tags (e.g. `Co-Authored-By: Claude`) or generated-by footers to commits or pull requests.
- **AI/Agent rules**: All rules applying to AI agents must be written in this file (`AGENTS.md`). Agent-specific config files (e.g. `.claude/CLAUDE.md`) must only point to this file — never duplicate or extend rules there.
- **AI/Agent naming convention**: Commits and PRs related to AI agent configuration or rules must use the `docs(agents)` conventional-commit prefix (e.g. `docs(agents): add co-authorship rule`).

### Code Quality and Maintainability

- **General Coding Conventions**: [`coding-conventions.md`](docs/conventions/coding-conventions.md) _(pointer)_

### Technology-Specific Guidelines

- **Kotlin Multiplatform Conventions**: [`kmp-conventions.md`](docs/conventions/kmp-conventions.md) _(+ project)_

## 3. Repository Structure

Target layout, reached during phase 1 of the [roadmap](docs/roadmap.md). Modules are nested rather than given compound names ([ADR-004](docs/adr/adr-004-supported-backends-and-module-layout.md)).

| Module              | Directory           | Published artifact                    |
|---------------------|---------------------|---------------------------------------|
| `:logger:core`      | `logger/core/`      | `io.github.cyrillrx:logger`           |
| `:logger:sentry`    | `logger/sentry/`    | `io.github.cyrillrx:logger-sentry`    |
| `:tracker:core`     | `tracker/core/`     | `io.github.cyrillrx:tracker`          |
| `:tracker:firebase` | `tracker/firebase/` | `io.github.cyrillrx:tracker-firebase` |
| `:sample`           | `sample/`           | Not published                         |

Until phase 1 lands, the libraries still live in `shared/logger` and `shared/tracker`, next to legacy Android-only modules scheduled for removal.

Every module ships the same version, published to Maven Central from a `vX.Y.Z` tag ([ADR-001](docs/adr/adr-001-distribution-via-maven-central.md)).

## 4. Commands

All commands run from the repository root:

```bash
./gradlew build          # Build every target
./gradlew jvmTest        # Run the common tests on the JVM
./gradlew ktlintCheck    # Check formatting
./gradlew ktlintFormat   # Auto-fix formatting
```

## 5. Shared Tooling

The Claude Code plugins declared in [`.claude/settings.json`](.claude/settings.json) come from the `cyrillrx-conventions` marketplace and install on folder trust:

| Plugin            | Provides                                         |
|-------------------|--------------------------------------------------|
| `git-workflow`    | `/commit`, `/triage-findings`, `/address-review` |
| `kmp-conventions` | `kmp-style` (auto-invoked)                       |

The plugin skills are derived from the convention documents. When a rule and a skill disagree, the document in `cyrillrx/coding-conventions` wins — report the drift there rather than working around it here.
