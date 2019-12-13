DESCRIPTION = "Linux kernel firmware files from Raspbian distribution"

LICENSE = "broadcom_bcm43xx"
LIC_FILES_CHKSUM = "file://LICENCE.broadcom_bcm43xx;md5=3160c14df7228891b868060e1951dfbc"
NO_GENERIC_LICENSE[broadcom_bcm43xx] = "LICENCE.broadcom_bcm43xx"

SRCREV = "130cb86fa30cafbd575d38865fa546350d4c5f9c"
PV = "${SRCPV}"

SRC_URI = "git://github.com/RPi-Distro/firmware-nonfree.git"

S = "${WORKDIR}/git"

inherit allarch

do_install () {
    install -Dm644 LICENCE.broadcom_bcm43xx -t ${D}${nonarch_base_libdir}/firmware/

    install -d ${D}${nonarch_base_libdir}/firmware/brcm/
    install -m644 brcm/brcmfmac43430-sdio.bin ${D}${nonarch_base_libdir}/firmware/brcm/
    install -m644 brcm/brcmfmac43430-sdio.txt ${D}${nonarch_base_libdir}/firmware/brcm/
}

PACKAGES = "${PN}-bcm43430"
FILES_${PN}-bcm43430 = "${nonarch_base_libdir}"
