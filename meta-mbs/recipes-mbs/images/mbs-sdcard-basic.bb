SUMMARY = "mbs basic sdcard image"

inherit core-image sdcard-image-mbs

IMAGE_FSTYPES = "rpi-sdimg"
IMAGE_FEATURES += "read-only-rootfs empty-root-password"
IMAGE_LINGUAS = ""

DEPENDS += "coreutils-native"

CORE_IMAGE_BASE_INSTALL = "base-files base-passwd busybox neoinit"
