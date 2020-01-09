SUMMARY = "Linux kernel style configuration framework for other projects"

LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=9b8cf60ff39767ff04b671fca8302408"

DEPENDS += "ncurses gperf-native bison-native"

SRC_URI = " \
    git://gitlab.com/ymorin/kconfig-frontends.git;protocol=https;branch=4.11.x \
    file://0001-disable-written-msg.patch \
"

SRCREV = "f22fce3a308be1c7790ebefc6bbedb33c5f7c86a"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

EXTRA_OECONF += "--enable-frontends=conf,mconf"

do_install_append () {
    rm ${D}${bindir}/kconfig-diff
    rm ${D}${bindir}/kconfig-tweak
}

# Some packages have the version preceeding the .so instead properly
# versioned .so.<version>, so we need to reorder and repackage.
SOLIBS = "-${@d.getVar('PV')[:-2]}.so"
FILES_SOLIBSDEV = "${libdir}/libkconfig-parser.so"

BBCLASSEXTEND = "native"
