SUMMARY = "mbs Linux"

LINUX_KERNEL_TYPE = "mbs"
KCONFIG_MODE = "--allnoconfig"

require recipes-kernel/linux/linux-yocto.inc

LINUX_VERSION = "4.19.75"
LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

SRCREV = "linux_raspberrypi-kernel_1.20190925-1"
PV = "${LINUX_VERSION}"

SRC_URI = " \
    git://github.com/typedivision/raspberrypi.git;destsuffix=raspberry \
    file://base.cfg \
"

S = "${WORKDIR}/raspberry/linux"

# cleanup/empty KERNEL_FEATURES
KERNEL_FEATURES_remove = "features/debug/printk.scc features/kernel-sample/kernel-sample.scc"
