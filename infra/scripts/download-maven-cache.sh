#!/usr/bin/env bash
set -e

# This script downloads previous maven packages that have been downloaded 
# from Google Cloud Storage to local path for faster build

usage()
{
    echo "usage: prepare_maven_cache.sh
    --archive-uri   gcs uri to retrieve maven .m2 archive
    --output-dir    output directory for .m2 directory"
}

while [ "$1" != "" ]; do
  case "$1" in
      --archive-uri )       ARCHIVE_URI="$2";    shift;;
      --output-dir )        OUTPUT_DIR="$2";     shift;;
      * )                   usage; exit 1
  esac
  shift
done

if [[ ! ${ARCHIVE_URI} ]]; then usage; exit 1; fi
if [[ ! ${OUTPUT_DIR}  ]]; then usage; exit 1; fi

# Install Google Cloud SDK if gsutil command not exists
if [[ ! $(command -v gsutil) ]]; then 
  CURRENT_DIR=$(dirname "$BASH_SOURCE")
  . "${CURRENT_DIR}"/install-google-cloud-sdk.sh
fi  

gsutil -q cp ${ARCHIVE_URI} /tmp/.m2.tar
tar xf /tmp/.m2.tar -C ${OUTPUT_DIR}
