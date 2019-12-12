SUMMARY = "mbs Linux"

LINUX_KERNEL_TYPE = "mbs"
KCONFIG_MODE = "--allnoconfig"

require recipes-kernel/linux/linux-yocto.inc

LINUX_VERSION ?= "5.2.21"
LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

SRCREV ?= "4465f51dc5d37e42a58dc5c36cd2a74cfb074aa6"
PV = "${LINUX_VERSION}+${SRCPV}"

KBRANCH = "rpi-5.2.y"

SRC_URI = " \
    git://github.com/raspberrypi/linux.git;branch=${KBRANCH} \
    file://base.cfg \
"

# cleanup/empty KERNEL_FEATURES
KERNEL_FEATURES_remove = "features/debug/printk.scc features/kernel-sample/kernel-sample.scc"
