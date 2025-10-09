#!/usr/bin/env bash
set -eo pipefail


usage()
{
    echo "usage: release-patch.sh

    This script is used to release a patch release. It is untested on major/minor releases and for those, some modification may be necessary.

    -v, --version           version to release, example: 0.10.6
    -t, --github-token      personal GitHub token
    -r, --remote            git remote server name for the feast-dev/feast repo (e.g. origin, upstream, etc.)
"
}

while [ "$1" != "" ]; do
  case "$1" in
      -v | --version )           VERSION="$2";            shift;;
      -t | --github-token )      GH_TOKEN="$2";           shift;;
      -r | --remote )            REMOTE="$2";             shift;;
      -h | --help )              usage;                   exit;;
      * )                        usage;                   exit 1
  esac
  shift
done

if [ -z $VERSION ]; then usage; exit 1; fi
if [ -z $GH_TOKEN ]; then usage; exit 1; fi
if [ -z $REMOTE ]; then usage; exit 1; fi
regex="([0-9]+)\.([0-9]+)\.([0-9]+)"
if [[ $VERSION =~ $regex ]]
then
    MAJOR="${BASH_REMATCH[1]}"
    MINOR="${BASH_REMATCH[2]}"
    PATCH="${BASH_REMATCH[3]}"
else
    usage
    exit 1
fi
if ! which gh ; then echo "Please install the GitHub CLI to use this script"; exit 1; fi

echo "This script is mostly idempotent; check git status for temp files before restarting. It will always prompt you before making any non-local change."

# Go to infra/scripts directory
cd $(dirname "$0")
# Login to GitHub CLI
echo $GH_TOKEN | gh auth login --with-token

echo "Step 1: rebase new commits onto release branch"
git fetch $REMOTE
git checkout $REMOTE/master
STARTING_COMMIT=$(git merge-base $REMOTE/master v$MAJOR.$MINOR-branch)
git checkout v$MAJOR.$MINOR-branch

push_rebased_commits()
{
    echo "Pushing commits"
    git push $REMOTE v$MAJOR.$MINOR-branch
    echo "Commits pushed"
}
rebase_from_master()
{
    echo "Rebasing commits"
    git checkout $REMOTE/master
    git rebase --interactive --onto v$MAJOR.$MINOR-branch $STARTING_COMMIT HEAD
    git branch -f v$MAJOR.$MINOR-branch HEAD
    git checkout v$MAJOR.$MINOR-branch
    echo "Commits rebased"
    echo "Step 1b: Push commits"
    read -p "Commits are not pushed. Continue (y) or skip this sub-step (n)? " choice
    case "$choice" in
        y|Y ) push_rebased_commits ;;
        * ) echo "Skipping this sub-step" ;;
    esac ;
}
echo "Step 1a: rebase commits"
if git status | grep -q "is ahead of" ; then
    read -p "Your local branch is ahead of its remote counterpart, indicating you may have already rebased. Skip this step (y) or run the rebase starting from commit $STARTING_COMMIT (n)? " choice
    case "$choice" in
        y|Y ) echo "Skipping this step" ;;
        * ) rebase_from_master ;;
    esac ;
else
    read -p "Will rebase starting from commit $STARTING_COMMIT. Continue (y) or skip this step (n)? " choice
    case "$choice" in
        y|Y ) rebase_from_master ;;
        * ) echo "Skipping this step" ;;
    esac ;
fi

CHANGELOG=$(git rev-parse --show-toplevel)/CHANGELOG.md

commit_changelog()
{
    echo "Committing CHANGELOG.md"
    git add $CHANGELOG
    git commit -m "Update CHANGELOG for Feast v$MAJOR.$MINOR.$PATCH"
}
update_changelog()
{
    echo "Running changelog generator (will take up to a few minutes)"
    echo -e "# Changelog\n" > temp \
    && docker run -it --rm ferrarimarco/github-changelog-generator \
    --user feast-dev \
    --project feast  \
    --release-branch master  \
    --future-release v$MAJOR.$MINOR.$PATCH  \
    --unreleased-only  \
    --no-issues  \
    --bug-labels kind/bug  \
    --enhancement-labels kind/feature  \
    --breaking-labels compat/breaking  \
    -t $GH_TOKEN \
    --max-issues 1 -o \
    | sed -n '/## \[v'"$MAJOR.$MINOR.$PATCH"'\]/,$p' \
    | sed '$d' | sed '$d' | sed '$d' | tr -d '\r' >> temp \
    && sed '1d' $CHANGELOG >> temp && mv temp $CHANGELOG
    git diff $CHANGELOG
    echo "Check CHANGELOG.md carefully and fix any errors. In particular, make sure the new enhancements/PRs/bugfixes aren't already listed somewhere lower down in the file."
    read -p "Once you're done checking, continue to commit the changelog (y) or exit (n)? " choice
    case "$choice" in
        y|Y ) commit_changelog ;;
        * ) exit ;;
    esac ;
}
echo "Step 2: Updating CHANGELOG.md"
if grep -q "https://github.com/feast-dev/feast/tree/v$MAJOR.$MINOR.$PATCH" $CHANGELOG ; then
    read -p "CHANGELOG.md appears updated. Skip this step (y/n)? " choice
    case "$choice" in
        y|Y ) echo "Skipping this step" ;;
        * ) update_changelog ;;
    esac ;
else
    update_changelog ;
fi

tag_commit()
{
    echo "Tagging commit"
    git tag v$MAJOR.$MINOR.$PATCH
    echo "Commit tagged"
}
echo "Step 3: Tag commit"
if git tag | grep -q "v$MAJOR.$MINOR.$PATCH" ; then
    read -p "The tag already exists. Skip this step (y/n)? " choice
    case "$choice" in
        y|Y ) echo "Skipping this step" ;;
        * ) tag_commit ;;
    esac ;
else
    tag_commit ;
fi

echo "Step 4: Push commits and tags"
push_commits()
{
    echo "Pushing commits"
    git push $REMOTE v$MAJOR.$MINOR-branch
    echo "Commits pushed"
}
echo "Step 4a: Push commits"
if git status | grep -q "nothing to commit" ; then
    echo "The commits appear pushed. Skipping this sub-step"
else
    read -p "Commits are not pushed. Continue (y) or skip this sub-step (n)? " choice
    case "$choice" in
        y|Y ) push_commits ;;
        * ) echo "Skipping this sub-step" ;;
    esac ;
fi

push_tag()
{
    echo "Pushing tag"
    git push $REMOTE v$MAJOR.$MINOR.$PATCH
    echo "Tag pushed"
}
echo "Step 4b: Push tag"
if git ls-remote --tags $REMOTE | grep -q "v$MAJOR.$MINOR.$PATCH" ; then
    read -p "The tag appears pushed. Skip this sub-step (y/n)? " choice
    case "$choice" in
        y|Y ) echo "Skipping this sub-step" ;;
        * ) push_tag ;;
    esac ;
else
    read -p "The tag is not pushed. Continue (y) or skip this sub-step (n)? " choice
    case "$choice" in
        y|Y ) push_tag ;;
        * ) echo "Skipping this sub-step" ;;
    esac ;
fi

read -p "Now wait for the CI to pass. Continue (y) or exit and fix the problem (n)? " choice
case "$choice" in
    y|Y ) echo "Moving on to the next step" ;;
    * ) exit ;;
esac ;

echo "Step 6: Add changelog to master"
changelog_hash=$(git rev-parse HEAD)
git checkout master
cp_changelog()
{
    echo "Cherry-picking"
    git cherry-pick $changelog_hash
    echo "Cherry-pick done"
}
echo "Step 6a: Cherry-pick changelog to master"
if grep -q "https://github.com/feast-dev/feast/tree/v$MAJOR.$MINOR.$PATCH" $CHANGELOG ; then
    read -p "The changelog appears to be cherry-picked onto master. Skip this sub-step (y/n)? " choice
    case "$choice" in
        y|Y ) echo "Skipping this sub-step" ;;
        * ) cp_changelog ;;
    esac ;
else
    read -p "The changelog does not appear to be cherry-picked onto master. Continue (y) or skip this sub-step (n)? " choice
    case "$choice" in
        y|Y ) cp_changelog ;;
        * ) echo "Skipping this sub-step" ;;
    esac ;
fi

push_cp()
{
    echo "Pushing cherry-pick"
    git push $REMOTE master
    echo "Commit pushed"
}
echo "Step 6b: Push changelog to master"
if git status | grep -q "nothing to commit" ; then
    echo "The commit appears pushed. Skipping this sub-step"
else
    read -p "The commit is not pushed. Continue (y) or skip this sub-step (n)? " choice
    case "$choice" in
        y|Y ) push_cp ;;
        * ) echo "Skipping this sub-step" ;;
    esac ;
fi

create_release()
{
    echo "Creating GitHub release"
    cat $CHANGELOG | sed -n '/## \[v'"$MAJOR.$MINOR.$PATCH"'\]/,/## \[v'"$MAJOR.$MINOR.$(($PATCH-1))"'\]/p' | sed -n '/**Implemented enhancements/,$p' | sed '$d' > temp2 \
    && gh release create v$MAJOR.$MINOR.$PATCH -t "Feast v$MAJOR.$MINOR.$PATCH" --repo feast-dev/feast --notes-file temp2 \
    && rm temp2
    echo "GitHub release created"
}
echo "Step 7: Create a GitHub release"
if gh release list --repo feast-dev/feast | grep -q "v$MAJOR.$MINOR.$PATCH" ; then
    read -p "GitHub release appears created. Skip this step (y/n)? " choice
    case "$choice" in
        y|Y ) echo "Skipping this step" ;;
        * ) create_release ;;
    esac ;
else
    read -p "A GitHub release has not been created. Continue (y) or skip this step (n)? " choice
    case "$choice" in
        y|Y ) create_release ;;
        * ) echo "Skipping this step" ;;
    esac ;
fi

echo "Step 8: Update the Upgrade Guide manually (docs/advanced/upgrading.md)"
