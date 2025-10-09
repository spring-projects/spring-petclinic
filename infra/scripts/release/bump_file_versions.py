# This script will bump the versions found in files (charts, pom.xml) during the Feast release process.

import pathlib
import sys

USAGE = f"Usage: python {sys.argv[0]} [--help] | current_semver_version new_semver_version]"
VERSIONS_TO_BUMP = 27


def main() -> None:
    args = sys.argv[1:]
    if not args or len(args) != 2:
        raise SystemExit(USAGE)

    current_version = args[0].strip()
    new_version = args[1].strip()

    if current_version == new_version:
        raise SystemExit(f"Current and new versions are the same: {current_version} == {new_version}")

    # Validate that the input arguments are semver versions
    if not is_semantic_version(current_version):
        raise SystemExit(f"Current version is not a valid semantic version: {current_version}")

    if not is_semantic_version(new_version):
        raise SystemExit(f"New version is not a valid semantic version: {new_version}")

    # Get git repo root directory
    repo_root = pathlib.Path(__file__).resolve().parent.parent.parent.parent
    path_to_file_list = repo_root.joinpath("infra", "scripts", "release", "files_to_bump.txt")

    # Get files to bump versions within
    with open(path_to_file_list, "r") as f:
        files_to_bump = f.read().splitlines()

    # The current version should be 0.18.0 or 0.19.0 or 0.20.0 etc
    validate_files_to_bump(current_version, files_to_bump, repo_root)

    # Bump the version in the files
    updated_count = 0
    for file in files_to_bump:
        components = file.split(" ")
        file_path = components[0]
        lines = components[1:]
        with open(repo_root.joinpath(file_path), "r") as f:
            file_contents = f.readlines()
            for line in lines:
                file_contents[int(line) - 1] = file_contents[int(line) - 1].replace(current_version, new_version)

        with open(repo_root.joinpath(file_path), "w") as f:
            f.write(''.join(file_contents))
            updated_count += 1

    print(f"Updated {updated_count} files with new version {new_version}")


def is_semantic_version(version: str) -> bool:
    components = version.split(".")
    if len(components) != 3:
        return False
    for component in components:
        if not component.isdigit():
            return False
    return True


def validate_files_to_bump(current_version, files_to_bump, repo_root):
    for file in files_to_bump:
        components = file.split(" ")
        assert len(components) > 1, f"Entry {file} should have a file name, and a list of line numbers with versions"
        file_path = components[0]
        lines = components[1:]
        with open(repo_root.joinpath(file_path), "r") as f:
            file_contents = f.readlines()
            for line in lines:
                assert current_version in file_contents[int(line) - 1], (
                    f"File `{file_path}` line `{line}` didn't contain version {current_version}. "
                    f"Contents: {file_contents[int(line) - 1]}"
                )


if __name__ == "__main__":
    main()
