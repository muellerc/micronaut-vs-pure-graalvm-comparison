#!/bin/bash

docker build . -t pure-graalvm-app
mkdir -p target
docker run --rm --entrypoint cat pure-graalvm-app /home/application/target/function.zip > target/function.zip
