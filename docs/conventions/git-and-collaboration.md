# Git & Collaboration Conventions

> [!IMPORTANT]
> **Canonical source of truth** (shared, project-agnostic): [`collaboration/git-and-collaboration.md`](https://github.com/cyrillrx/coding-conventions/blob/main/collaboration/git-and-collaboration.md) — do not duplicate here.

Conventional Commits, trunk-based branching, atomic commits, PR etiquette, the authorship rule, ADR guidance, and the [code review emoji legend](https://github.com/cyrillrx/coding-conventions/blob/main/collaboration/code-review-emojis.md) all live in the canonical document. Only the project-specific bindings below differ.

## Project-specific additions

### Commit scopes

| Scope     | Covers                                                     |
|-----------|------------------------------------------------------------|
| `project` | Root-level tooling, README, repository-wide changes        |
| `agents`  | AI agent configuration and rules (`AGENTS.md`, `.claude/`) |
| `logger`  | The logger modules (`logger/*`)                            |
| `tracker` | The tracker modules (`tracker/*`)                          |
| `sample`  | The sample application                                     |
| `release` | Publication, versioning, changelog                         |
| `adr`     | Architecture Decision Records under `docs/adr/`            |
| `docs`    | Documentation that is not tied to a single component       |

### Releases

Every module ships the same version. A `vX.Y.Z` tag on `main` publishes all of them to Maven Central, and `CHANGELOG.md` follows [Keep a Changelog](https://keepachangelog.com/). The rationale is in [ADR-001](../adr/adr-001-distribution-via-maven-central.md).

### ADRs

ADRs live under `docs/adr/`, numbered in order, and are written from the [template](https://github.com/cyrillrx/coding-conventions/blob/main/templates/adr-template.md) in the canonical repository. Any change to the public API of a published module requires one.
