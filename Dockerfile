FROM quay.io/openshift/origin-jenkins-agent-nodejs

LABEL author="Ricardo Zanini <ricardozanini@gmail.com>"

ARG PACKAGE_JSON_PATH

COPY contrib/bin/* $HOME
COPY $PACKAGE_JSON_PATH $HOME

RUN $HOME/npm-packages-cache-install.sh
