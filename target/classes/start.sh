#!/bin/bash

SITE='EXTRACTORMONITOR'

java -classpath ./lib/*:./ com.ossean.extractormonitor.Monitor >>log/${SITE}.log 2>&1 &
