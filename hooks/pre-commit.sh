#!/bin/sh
# From gist at https://gist.github.com/chadmaughan/5889802

./gradlew spotlessKotlinApply

# run the tests with the gradle wrapper
./gradlew test

# store the last exit code in a variable
RESULT=$?

# return the './gradlew test' exit code
exit $RESULT