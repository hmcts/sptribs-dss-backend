#!/usr/bin/env bash

BASEDIR=$(realpath $(dirname ${0})/../../)

if [ -f $BASEDIR/.env ]
then
  export $(cat $BASEDIR/.env | sed 's/#.*//g' | xargs)
fi

set -e

username=${1}
password=${2}

IDAM_API_URL=${IDAM_API_URL_BASE:-http://localhost:5000}
IDAM_URL=${IDAM_STUB_LOCALHOST:-$IDAM_API_URL}
CLIENT_ID=${CLIENT_ID:-ds-ui}
#CLIENT_ID=${CLIENT_ID:-xuiwebapp}
clientSecret=${OAUTH2_CLIENT_SECRET}
redirectUri=http://localhost:3000/receiver
#redirectUri=http://localhost:3000/oauth2/callback
echo "client secret === ${CLIENT_ID}"
#echo "curl --location --request POST '${IDAM_API_URL}/o/token?username=${username}&password=${password}&scope=openid%20profile%20roles&client_id=${client_id}&client_secret=${clientSecret}&redirect_uri=${redirectUri}&grant_type=password' --header 'accept: application/json' --header 'Content-Type: application/x-www-form-urlencoded'"
curl --location --request POST "${IDAM_API_URL}/o/token?username=${username}&password=${password}&scope=openid%20profile%20roles&client_id=ds-ui&client_secret=${clientSecret}&redirect_uri=${redirectUri}&grant_type=password'" --header 'accept: application/json' --header 'Content-Type: application/x-www-form-urlencoded' -d "" | docker run --rm --interactive stedolan/jq -r .access_token
