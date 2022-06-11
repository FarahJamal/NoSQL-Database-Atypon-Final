#!/usr/bin/env bash

set -ev

if ["$NODE_ENV"= "development"]; then
  mvn spring-boot:run
else
  ngix -g "daemon off;"
fi