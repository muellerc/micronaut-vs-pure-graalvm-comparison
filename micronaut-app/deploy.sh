#!/bin/bash
docker build . -t micronaut-app
mkdir -p build
docker run --rm --entrypoint cat micronaut-app  /home/application/function.zip > build/function.zip
