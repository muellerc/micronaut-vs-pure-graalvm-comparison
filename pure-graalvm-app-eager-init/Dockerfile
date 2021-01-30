FROM amazonlinux:2018.03.0.20201209.1 as builder

ENV LANG=en_US.UTF-8
ENV GRAAL_VERSION 21.0.0
ENV JDK_VERSION java11
ENV GRAAL_FILENAME graalvm-ce-${JDK_VERSION}-linux-amd64-${GRAAL_VERSION}.tar.gz

RUN yum install -y gcc gcc-c++ libc6-dev  zlib1g-dev curl bash zlib zlib-devel zip
RUN curl -4 -L https://github.com/graalvm/graalvm-ce-builds/releases/download/vm-${GRAAL_VERSION}/${GRAAL_FILENAME} -o /tmp/${GRAAL_FILENAME}
RUN tar -zxvf /tmp/${GRAAL_FILENAME} -C /tmp \
    && mv /tmp/graalvm-ce-${JDK_VERSION}-${GRAAL_VERSION} /usr/lib/graalvm
RUN rm -rf /tmp/*
RUN /usr/lib/graalvm/bin/gu install native-image

ENV JAVA_HOME /usr/lib/graalvm
ENV PATH ${JAVA_HOME}/bin:${PATH}


FROM builder
COPY . /home/application
WORKDIR /home/application
RUN ./mvnw clean package

