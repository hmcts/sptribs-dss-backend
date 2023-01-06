#!/usr/bin/env bash

scriptPath=$(dirname $(realpath $0))

# Roles used during the CCD import
${scriptPath}/add-ccd-role.sh "caseworker-st-cic"
${scriptPath}/add-ccd-role.sh "citizen"
