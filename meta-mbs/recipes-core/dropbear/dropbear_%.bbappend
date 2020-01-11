SBINCOMMANDS = "dropbear dropbearkey"
BINCOMMANDS = "dbclient ssh scp"

EXTRA_OECONF += "--disable-wtmp --disable-lastlog"

do_install_append () {
    rm -rf ${D}${sysconfdir}
    rm -rf ${D}${localstatedir}
}
