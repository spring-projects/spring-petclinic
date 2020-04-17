#!/bin/bash
pushd .
mkdir ../../ap
cd ../../ap
curl -OL https://github.com/jvm-profiling-tools/async-profiler/releases/download/v1.7/async-profiler-1.7-linux-x64.tar.gz
tar xzvf async-profiler-1.7-linux-x64.tar.gz
popd
