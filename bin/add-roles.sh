#!/usr/bin/env bash

scriptPath=$(dirname $(realpath $0))

# Roles used during the CCD import
${scriptPath}/add-ccd-role.sh "caseworker-fis"
${scriptPath}/add-ccd-role.sh "caseworker-fis-caseworker"
${scriptPath}/add-ccd-role.sh "caseworker-fis-courtadmin"
${scriptPath}/add-ccd-role.sh "caseworker-fis-la"
${scriptPath}/add-ccd-role.sh "caseworker-fis-judge"
${scriptPath}/add-ccd-role.sh "caseworker-fis-superuser"
${scriptPath}/add-ccd-role.sh "caseworker-fis-solicitor"
${scriptPath}/add-ccd-role.sh "citizen"
${scriptPath}/add-ccd-role.sh "caseworker-family-solicitor"
${scriptPath}/add-ccd-role.sh "caseworker-family-caseworker"
${scriptPath}/add-ccd-role.sh "caseworker-family"
${scriptPath}/add-ccd-role.sh "caseworker-family-judge"
${scriptPath}/add-ccd-role.sh "caseworker-family-la"
${scriptPath}/add-ccd-role.sh "caseworker-family-superuser"
