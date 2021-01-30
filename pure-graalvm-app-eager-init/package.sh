#!/bin/bash

docker build . -t pure-graalvm-app-eager-init
mkdir -p target
docker run --rm --entrypoint cat pure-graalvm-app-eager-init /home/application/target/function.zip > target/function.zip
