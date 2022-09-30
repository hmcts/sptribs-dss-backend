ARG APP_INSIGHTS_AGENT_VERSION=3.2.8
FROM hmctspublic.azurecr.io/base/java:11-distroless

COPY build/libs/sptribs-dss-backend.jar /opt/app/

EXPOSE 8099
CMD [ "sptribs-dss-backend.jar" ]
