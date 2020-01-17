DESCRIPTION = "a service start and supervising daemon"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=393a5ca445f6965873eca0259a17f833"

SRC_URI = " \
    git://github.com/typedivision/${BPN}.git;branch=v${PV} \
    file://sysinit.run \
    file://getty.run \
    file://reboot.run \
"

SRCREV = "2a5d8bc2c992868fc2d62d7432e51d506b6d526a"

S = "${WORKDIR}/git"

do_compile () {
    make
}

do_install () {
    make DESTDIR=${D} install-files
    install -m755 killall5 ${D}/bin/killall5
    rm -rf ${D}/usr

    install -d ${D}/etc/neoinit/boot
    install -d ${D}/etc/neoinit/default
    install -d ${D}/etc/neoinit/default.sys
    install -d ${D}/etc/neoinit/default.usr

    echo "default.sys" >> ${D}/etc/neoinit/default/depends
    echo "default.usr" >> ${D}/etc/neoinit/default/depends

    install -d ${D}/media/overlay
    install -d ${D}/media/volatile
    install -d ${D}/media/data
    install -Dm755 ${WORKDIR}/sysinit.run ${D}/etc/neoinit/sysinit/run
    echo "sysinit" >> ${D}/etc/neoinit/boot/depends

    install -Dm755 ${WORKDIR}/getty.run ${D}/etc/neoinit/getty/run
    touch ${D}/etc/neoinit/getty/respawn
    echo "getty" >> ${D}/etc/neoinit/default.usr/depends

    install -Dm755 ${WORKDIR}/reboot.run ${D}/etc/neoinit/reboot/run

    ln -s /sbin/neoinit ${D}/sbin/init
}

FILES_${PN} += "/media"

pkg_postinst_${PN} () {
#!/bin/sh -e
mkfifo -m 600 $D/etc/neoinit/in $D/etc/neoinit/out
}
