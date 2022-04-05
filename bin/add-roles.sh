#!/usr/bin/env bash

scriptPath=$(dirname $(realpath $0))

# Roles used during the CCD import
${scriptPath}/add-ccd-role.sh "caseworker-adoption"
${scriptPath}/add-ccd-role.sh "caseworker-adoption-caseworker"
${scriptPath}/add-ccd-role.sh "caseworker-adoption-courtadmin"
${scriptPath}/add-ccd-role.sh "caseworker-adoption-la"
${scriptPath}/add-ccd-role.sh "caseworker-adoption-judge"
${scriptPath}/add-ccd-role.sh "caseworker-adoption-superuser"
${scriptPath}/add-ccd-role.sh "caseworker-adoption-solicitor"
${scriptPath}/add-ccd-role.sh "citizen"