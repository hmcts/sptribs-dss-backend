#!/usr/bin/env bash

scriptPath=$(dirname $(realpath $0))

# Roles used during the CCD import
${scriptPath}/add-ccd-role.sh "caseworker-privatelaw"
${scriptPath}/add-ccd-role.sh "caseworker-privatelaw-solicitor"
${scriptPath}/add-ccd-role.sh "caseworker-privatelaw-courtadmin"
${scriptPath}/add-ccd-role.sh "citizen"
