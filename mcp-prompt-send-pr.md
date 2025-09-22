# MCP Prompt: Send Pull Request to Repository

## Instructions
Create and send a pull request to the specified repository URL. Use the provided custom git commit message for the PR.

## Arguments
- `repourl`: The URL of the target repository.
- `git_message`: The custom commit message to use for the PR.

## Steps
1. Clone the repository from `repourl`.
2. Create a new branch for your changes.
3. Commit your changes with `git_message`.
4. Push the branch and open a pull request to the main branch.

## Output
- Confirmation of PR creation.
- PR URL or identifier.
