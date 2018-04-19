#!/bin/bash
set -eu

# You can run it from any directory.
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

pushd "$DIR/.." > /dev/null

# Clean previous build output.
echo "Cleaning previous build output..."
./gradlew clean

# Build codegen app.
echo "Building codegen app..."
./gradlew :uncheck-exceptions-codegen:build

# Run codegen to generate uncheck-exceptions.jar
echo "Generating uncheck-exceptions.jar..."
./gradlew :uncheck-exceptions-codegen:run
mkdir -p build/libs
mv uncheck-exceptions-codegen/uncheck-exceptions.jar build/libs

# Run integration tests against uncheck-exceptions.jar
echo "Running integration tests against uncheck-exceptions.jar..."
./gradlew :uncheck-exceptions-integration-test:build

echo "Done!"
echo "uncheck-exceptions.jar can be found in build/libs directory."
