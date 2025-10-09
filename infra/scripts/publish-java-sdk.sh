#!/usr/bin/env bash

set -e 
set -o pipefail

GPG_KEY_IMPORT_DIR=/etc/gpg

usage()
{
    echo "usage: publish-java-sdk.sh

  --revision            Value for the revision e.g. '0.2.3'
  --gpg-key-import-dir  Directory containing existing GPG keys to import.
                        The directory should contain these 2 files:
                        - public-key
                        - private-key
                        The default value is '/etc/gpg'
  
  This script assumes the GPG private key is protected by a passphrase.
  The passphrase can be specified in \$HOME/.m2/settings.xml. In the same xml
  file, credentials to upload releases to Sonatype must also be provided.

  # Example settings: ~/.m2/settings.xml
  <settings>
    <servers>
      <server>
        <id>ossrh</id>
        <username>SONATYPE_USER</username>
        <password>SONATYPE_PASSWORD</password>
      </server>
    </servers>
    <profiles>
      <profile>
        <id>ossrh</id>
        <properties>
          <gpg.passphrase>GPG_PASSPHRASE</gpg.passphrase>
        </properties>
      </profile>
    </profiles>
  </settings>
"
}

while [ "$1" != "" ]; do
  case "$1" in
      --revision )             REVISION="$2";            shift;;
      --gpg-key-import-dir )   GPG_KEY_IMPORT_DIR="$2";  shift;;
      -h | --help )            usage;                    exit;; 
      * )                      usage;                    exit 1
  esac
  shift
done

if [ -z $REVISION ]; then usage; exit 1; fi

echo "============================================================"
echo "Checking Maven and GPG versions"
echo "============================================================"
mvn -f java/pom.xml --version
echo ""
gpg --version

echo "============================================================"
echo "Importing GPG keys"
echo "============================================================"
gpg --import --batch --yes $GPG_KEY_IMPORT_DIR/public-key
gpg --import --batch --yes $GPG_KEY_IMPORT_DIR/private-key

echo "============================================================"
echo "Deploying Java SDK with revision: $REVISION"
echo "============================================================"
mvn -f java/pom.xml --projects .,datatypes,serving-client -Drevision=$REVISION --batch-mode clean deploy
