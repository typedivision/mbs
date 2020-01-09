inherit image_types

# Rootfs image type
IMAGE_TYPEDEP_rpi-sdimg = "squashfs-xz"

# Boot partition size [in KiB] (will be rounded up to IMAGE_ALIGNMENT)
BOOT_SPACE = "16384"

# Data partition size [in KiB]
DATA_SPACE = "8172"

# Set alignment to 4MB [in KiB]
IMAGE_ALIGNMENT = "4096"

# SD card image name
SDIMG = "${IMGDEPLOYDIR}/${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.rpi-sdimg"

# For the names of kernel artifacts
inherit kernel-artifact-names

do_image_rpi_sdimg[depends] = " \
    parted-native:do_populate_sysroot \
    dosfstools-native:do_populate_sysroot \
    mtools-native:do_populate_sysroot \
    virtual/kernel:do_deploy \
    bcm2835-bootfiles:do_deploy \
    boot-config:do_deploy \
"

IMAGE_CMD_rpi-sdimg () {
    # Align partitions
    BOOT_SPACE_ALIGNED=$(expr ${BOOT_SPACE} + ${IMAGE_ALIGNMENT} - 1)
    BOOT_SPACE_ALIGNED=$(expr ${BOOT_SPACE_ALIGNED} - ${BOOT_SPACE_ALIGNED} % ${IMAGE_ALIGNMENT})
    SDIMG_SIZE=$(expr ${IMAGE_ALIGNMENT} + ${BOOT_SPACE_ALIGNED} + ${DATA_SPACE})

    # Initialize sdcard image file
    dd if=/dev/zero of=${SDIMG} bs=1024 count=0 seek=${SDIMG_SIZE}

    # Create partition table
    parted -s ${SDIMG} mklabel msdos
    parted -s ${SDIMG} unit KiB mkpart primary fat32 ${IMAGE_ALIGNMENT} $(expr ${BOOT_SPACE_ALIGNED} + ${IMAGE_ALIGNMENT})
    parted -s ${SDIMG} -- unit KiB mkpart primary ext2 $(expr ${BOOT_SPACE_ALIGNED} + ${IMAGE_ALIGNMENT}) -1s

    # Create a vfat image
    BOOT_BLOCKS=$(parted -s ${SDIMG} unit b print | awk '/ 1 / { print substr($4, 1, length($4 -1)) / 512 / 2 }')
    rm -f ${WORKDIR}/boot.img
    mkfs.vfat -n "BOOT" -S 512 -C ${WORKDIR}/boot.img ${BOOT_BLOCKS}

    # Copy kernel and device tree
    mcopy -v -i ${WORKDIR}/boot.img -s ${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE} ::kernel8.img || \
        bbfatal "mcopy cannot copy ${KERNEL_IMAGETYPE} into boot.img"
    dtb=`basename ${KERNEL_DEVICETREE}`
    mcopy -v -i ${WORKDIR}/boot.img -s ${DEPLOY_DIR_IMAGE}/$dtb ::$dtb || \
        bbfatal "mcopy cannot copy $dtb into boot.img"

    # Copy rootfs
    rootfs=${IMAGE_LINK_NAME}.squashfs-xz
    mcopy -v -i ${WORKDIR}/boot.img -s ${IMGDEPLOYDIR}/$rootfs ::rootfs.sqfs || \
        bbfatal "mcopy cannot copy $rootfs into boot.img"

    # Copy boot files
    for f in bootcode.bin start.elf fixup.dat; do
        mcopy -v -i ${WORKDIR}/boot.img -s ${DEPLOY_DIR_IMAGE}/bcm2835-bootfiles/$f ::/ || \
            bbfatal "mcopy cannot copy bcm2835-bootfiles/$f into boot.img"
    done
    # Copy boot config
    for f in config.txt cmdline.txt; do
        mcopy -v -i ${WORKDIR}/boot.img -s ${DEPLOY_DIR_IMAGE}/boot-config/$f ::/ || \
            bbfatal "mcopy cannot copy boot-config/$f into boot.img"
    done

    # Add stamp file
    echo "${IMAGE_NAME}" > ${WORKDIR}/image-version-info.txt
    mcopy -v -i ${WORKDIR}/boot.img ${WORKDIR}/image-version-info.txt :: || \
        bbfatal "mcopy cannot copy image-version-info.txt into boot.img"

    # Create data partition image
    dd if=/dev/zero of=${WORKDIR}/data.img bs=1024 count=0 seek=${DATA_SPACE}
    mkfs.ext4 -L "data" ${WORKDIR}/data.img

    # Burn Partitions
    dd if=${WORKDIR}/boot.img of=${SDIMG} conv=notrunc seek=1 bs=$(expr ${IMAGE_ALIGNMENT} \* 1024)
    dd if=${WORKDIR}/data.img of=${SDIMG} conv=notrunc seek=1 bs=$(expr ${IMAGE_ALIGNMENT} \* 1024 + ${BOOT_SPACE_ALIGNED} \* 1024)
}
