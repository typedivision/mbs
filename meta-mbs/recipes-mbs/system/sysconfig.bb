DESCRIPTION = "mbs system configuration files"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

RDEPENDS_${PN} = "kconfig-frontends"

SRC_URI = " \
    file://system.kconfig \
    file://sysconfig.run \
    file://mbs-config.sh \
"

do_install () {
    install -Dm644 ${WORKDIR}/system.kconfig ${D}${sysconfdir}/sysconf/system.kconfig
    install -Dm755 ${WORKDIR}/sysconfig.run ${D}${sysconfdir}/neoinit/sysconfig/run

    install -Dm755 ${WORKDIR}/mbs-config.sh ${D}${bindir}/mbs-config
}

pkg_postinst_${PN} () {
#!/bin/sh -e
echo sysconfig >> $D/etc/neoinit/boot/depends
}
