#!/usr/bin/env python3
"""Compute the next release tag for a main-branch build.

Versioning rule: every commit that lands on main is a release,
and each one bumps the MINOR component. PATCH stays 0 - a hotfix flow branching
off an existing tag would be the thing to use it, and that is out of scope here.

Output contract: stdout carries the new tag and nothing else, so the caller can
do NEW_TAG="$(bump_version.py --from-git)". Every diagnostic goes to stderr.

Fetching tags is the caller's job. The script only reads what it is given; the
workflow runs `git fetch --tags --force` before calling it.
"""

from __future__ import annotations

import argparse
import re
import subprocess
import sys

import semver

TAG_RE = re.compile(r"^v(\d+)\.(\d+)\.(\d+)$")
INITIAL_VERSION = semver.Version(1, 0, 0)


def parse_tags(raw_tags):
    """Split raw tag names into (sorted versions, ignored names)."""
    versions = []
    ignored = []
    for raw in raw_tags:
        tag = raw.strip()
        if not tag:
            continue
        match = TAG_RE.match(tag)
        if match:
            versions.append(semver.Version(*(int(g) for g in match.groups())))
        else:
            ignored.append(tag)
    return sorted(versions), ignored


def next_version(versions):
    """Highest version bumped by one minor, or the initial version if none."""
    if not versions:
        return INITIAL_VERSION
    return versions[-1].bump_minor()


def format_tag(version):
    return f"v{version}"


def compute_tag(raw_tags):
    """Full decision: filter, bump, guard. Returns the new tag name."""
    versions, ignored = parse_tags(raw_tags)

    if ignored:
        warn(f"ignoring {len(ignored)} tag(s) that are not vX.Y.Z: {', '.join(sorted(ignored))}")

    if versions:
        warn(f"highest existing release tag: {format_tag(versions[-1])}")
    else:
        warn("no release tags found, starting the version series")

    tag = format_tag(next_version(versions))

    if tag in {raw.strip() for raw in raw_tags}:
        die(
            f"{tag} already exists - refusing to reuse a released version. "
            "Fetch tags and re-run; if the tag is an orphan from a failed run, "
            "delete it after confirming no image was pushed for it."
        )

    return tag


def tags_from_git():
    result = subprocess.run(
        ["git", "tag", "--list"],
        capture_output=True,
        text=True,
        check=True,
    )
    return result.stdout.splitlines()


def warn(message):
    print(f"bump_version: {message}", file=sys.stderr)


def die(message):
    print(f"bump_version: error: {message}", file=sys.stderr)
    raise SystemExit(1)


def main(argv=None):
    parser = argparse.ArgumentParser(description=__doc__.splitlines()[0])
    parser.add_argument(
        "--from-git",
        action="store_true",
        help="read tags from `git tag --list` instead of stdin",
    )
    args = parser.parse_args(argv)

    raw_tags = tags_from_git() if args.from_git else sys.stdin.read().splitlines()

    print(compute_tag(raw_tags))
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
