#!/bin/bash
echo "Running"

kotlinc $1 -include-runtime -d $1.jar && java -jar $1.jar
