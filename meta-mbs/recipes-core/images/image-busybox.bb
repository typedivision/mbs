SUMMARY = "minimal busybox image"

inherit core-image qemuboot

IMAGE_FSTYPES = "cpio.gz"
IMAGE_FEATURES += "read-only-rootfs empty-root-password"
IMAGE_LINGUAS = ""

DEPENDS += "qemu-native qemu-helper-native coreutils-native"

CORE_IMAGE_BASE_INSTALL = "base-files base-passwd busybox"
