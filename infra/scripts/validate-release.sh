# This script ensures that we don't accidentally cut the wrong kind of release on master or release branches

if [ "$#" -ne 2 ]
then
  echo "Usage: validate-release.sh [major, minor, patch] branch"
  echo "Example: validate-release.sh patch master"
  exit 1
fi

if [ "$1" = "minor" ]; then
    if [ "$2" = "master" ]; then
        echo "Releasing a minor version on master, looks good!"
        exit 0
    else
        echo "Can't release a minor version from a non-master branch! Please confirm the version you are releasing!!"
        exit 1
    fi
elif [ "$1" = "patch" ]; then
    if [ "$2" = "master" ]; then
        echo "Can't release a patch version from master branch! Please confirm the version you are releasing!!"
        exit 1
    else
        echo "Releasing a patch version from a non-master branch, looks good!"
        exit 0
    fi
else
    echo "Not sure what kind of release is happening. Please confirm that you are creating a minor release from master
    or a patch from a release branch"
    exit 1
fi