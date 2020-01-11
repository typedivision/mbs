DESCRIPTION = "mbs system init scripts"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = " \
    file://klogd.run \
    file://syslogd.run \
    file://network.run \
    file://wifi.wpa.setup \
    file://wifi.wpa.run \
    file://wifi.dhcp.run \
    file://dropbear.run \
"

do_install () {
    for svc in klogd syslogd network wifi.wpa wifi.dhcp dropbear; do
        install -Dm755 ${WORKDIR}/$svc.run ${D}${sysconfdir}/neoinit/$svc/run
    done
    install -Dm755 ${WORKDIR}/wifi.wpa.setup ${D}${sysconfdir}/neoinit/wifi.wpa/setup

    install -d ${D}${sysconfdir}/neoinit/wifi
    echo "klogd"     >> ${D}${sysconfdir}/neoinit/syslogd/depends
    echo "wifi.wpa"  >> ${D}${sysconfdir}/neoinit/wifi/depends
    echo "wifi.dhcp" >> ${D}${sysconfdir}/neoinit/wifi/depends
}

pkg_postinst_${PN} () {
#!/bin/sh -e
echo "syslogd"  >> $D/etc/neoinit/default.sys/depends
echo "network"  >> $D/etc/neoinit/default.sys/depends
echo "wifi"     >> $D/etc/neoinit/default.sys/depends
echo "dropbear" >> $D/etc/neoinit/default.sys/depends
}
