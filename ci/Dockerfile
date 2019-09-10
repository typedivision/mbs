FROM ubuntu:bionic-20191029

ARG UID=1000
ARG GID=1000

RUN apt-get update \
 && apt-get install -y sudo locales vim screen libncurses-dev \
    build-essential gcc-multilib python python3 git \
    gawk wget diffstat texinfo chrpath cpio \
 && rm -rf /var/lib/apt/lists/*

RUN locale-gen en_US.UTF-8 \
 && groupadd -g $GID -o ci \
 && useradd -m -u $UID -g $GID -o -s /bin/bash ci \
 && echo 'ci ALL=(ALL) NOPASSWD:ALL' >> /etc/sudoers

USER ci
ENV LC_ALL=en_US.UTF-8

CMD /bin/bash
