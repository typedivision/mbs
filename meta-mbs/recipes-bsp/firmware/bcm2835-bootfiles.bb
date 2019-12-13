DESCRIPTION = "closed source binary files to help boot the ARM on the BCM2835"

LICENSE = "broadcom"
LIC_FILES_CHKSUM = "file://LICENCE.broadcom;md5=4a4d169737c0786fb9482bb6d30401d1"
NO_GENERIC_LICENSE[broadcom] = "LICENCE.broadcom"

PV = "1.20190925"

SRC_URI = "git://github.com/typedivision/raspberrypi.git;tag=firmware_${PV}"

S = "${WORKDIR}/git/firmware/boot"

inherit deploy nopackages
INHIBIT_DEFAULT_DEPS = "1"

do_deploy () {
    for i in ${S}/*.elf ; do
        cp $i ${DEPLOYDIR}/${PN}
    done
    for i in ${S}/*.dat ; do
        cp $i ${DEPLOYDIR}/${PN}
    done
    for i in ${S}/*.bin ; do
        cp $i ${DEPLOYDIR}/${PN}
    done
}

addtask deploy before do_build after do_install
do_deploy[dirs] += "${DEPLOYDIR}/${PN}"
