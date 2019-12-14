DESCRIPTION = "boot configuration files"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = " \
    file://cmdline.txt \
    file://config.txt \
"

inherit deploy nopackages
INHIBIT_DEFAULT_DEPS = "1"

do_deploy () {
    install -m644 ${WORKDIR}/cmdline.txt ${DEPLOYDIR}/${PN}/cmdline.txt
    install -m644 ${WORKDIR}/config.txt ${DEPLOYDIR}/${PN}/config.txt
}

addtask deploy before do_build after do_install
do_deploy[cleandirs] += "${DEPLOYDIR}/${PN}"
