#!/bin/bash
echo "Compiling"

kotlinc $(find . -name "*.kt") -include-runtime -d $1.jar