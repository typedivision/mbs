DESCRIPTION = "a service start and supervising daemon"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=393a5ca445f6965873eca0259a17f833"

SRC_URI = " \
  git://github.com/typedivision/${BPN}.git;branch=v${PV} \
  file://sysinit.run \
  file://ramfs.run \
  file://getty.run \
"

SRCREV = "ff13a4393d8a15f85a569d7f01d3a8670e02146b"

S = "${WORKDIR}/git"

do_compile () {
    make
}

do_install () {
    make DESTDIR=${D} install-files
    rm -rf ${D}/usr

    install -d ${D}/etc/neoinit/boot
    install -d ${D}/etc/neoinit/default
    install -d ${D}/etc/neoinit/default.sys
    install -d ${D}/etc/neoinit/default.usr

    echo "default.sys" >> ${D}/etc/neoinit/default/depends
    echo "default.usr" >> ${D}/etc/neoinit/default/depends

    install -Dm755 ${WORKDIR}/sysinit.run ${D}/etc/neoinit/sysinit/run
    echo "sysinit" >> ${D}/etc/neoinit/boot/depends

    install -d ${D}/ramfs/overlay
    install -d ${D}/ramfs/volatile
    install -Dm755 ${WORKDIR}/ramfs.run ${D}/etc/neoinit/ramfs/run
    echo "ramfs" >> ${D}/etc/neoinit/boot/depends

    install -Dm755 ${WORKDIR}/getty.run ${D}/etc/neoinit/getty/run
    touch ${D}/etc/neoinit/getty/respawn
    echo "getty" >> ${D}/etc/neoinit/default.usr/depends

    ln -s /sbin/neoinit ${D}/sbin/init
}

FILES_${PN} += "/ramfs"

pkg_postinst_${PN} () {
#!/bin/sh -e
mkfifo -m 600 $D/etc/neoinit/in $D/etc/neoinit/out
}
