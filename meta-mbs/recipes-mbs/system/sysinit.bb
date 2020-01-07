DESCRIPTION = "mbs system init scripts"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = " \
    file://klogd.run \
    file://syslogd.run \
"

do_install () {
    install -Dm755 ${WORKDIR}/klogd.run ${D}${sysconfdir}/neoinit/klogd/run
    install -Dm755 ${WORKDIR}/syslogd.run ${D}${sysconfdir}/neoinit/syslogd/run
}

pkg_postinst_${PN} () {
#!/bin/sh -e
echo syslogd >> $D/etc/neoinit/default.sys/depends
echo klogd >> $D/etc/neoinit/default.sys/depends
}
