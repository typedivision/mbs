SUMMARY = "mbs basic sdcard image"

inherit core-image sdcard-image-mbs

IMAGE_FSTYPES = "rpi-sdimg"
IMAGE_FEATURES += "read-only-rootfs empty-root-password"
IMAGE_LINGUAS = ""

DEPENDS += "coreutils-native"

CORE_IMAGE_BASE_INSTALL = " \
    base-files \
    base-passwd \
    busybox \
    busybox-udhcpc \
    neoinit \
    kernel-modules \
    linux-firmware-rpidistro-bcm43430 \
    wpa-supplicant \
    wpa-supplicant-passphrase \
    dropbear \
    sysinit \
    sysconfig \
"

rootfs_cleanup () {
    rm -rf ${IMAGE_ROOTFS}/boot/*
}

pkgstatus () {
    cp ${IMAGE_ROOTFS}${OPKGLIBDIR}/opkg/status ${IMGDEPLOYDIR}/${IMAGE_NAME}.rootfs.pkgstatus
}

update_bundle () {
    rm -rf ${IMAGE_NAME}.update
    mkdir ${IMAGE_NAME}.update
    cp ${IMGDEPLOYDIR}/${IMAGE_NAME}.rootfs.squashfs-xz ${IMAGE_NAME}.update/rootfs.sqfs.update
    cp ${DEPLOY_DIR_IMAGE}/Image-${MACHINE}.bin ${IMAGE_NAME}.update/kernel8.img.update
    (
        cd ${IMAGE_NAME}.update
        find . -type f | xargs md5sum > ../update.md5
        mv ../update.md5 .
    )
    tar -czf ${IMGDEPLOYDIR}/${IMAGE_NAME}.update.tar.gz -C ${IMAGE_NAME}.update .
    ln -sf ${IMAGE_NAME}.update.tar.gz ${IMGDEPLOYDIR}/${IMAGE_BASENAME}-${MACHINE}.update.tar.gz
}

ROOTFS_POSTPROCESS_COMMAND += "rootfs_cleanup; pkgstatus;"
IMAGE_POSTPROCESS_COMMAND += "update_bundle;"
