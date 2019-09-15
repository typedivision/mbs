SUMMARY = "minimal mbs busybox image"

inherit core-image

IMAGE_FSTYPES = "squashfs-xz"
IMAGE_FEATURES += "read-only-rootfs empty-root-password"
IMAGE_LINGUAS = ""

DEPENDS += "coreutils-native"

CORE_IMAGE_BASE_INSTALL = "base-files base-passwd busybox neoinit"
