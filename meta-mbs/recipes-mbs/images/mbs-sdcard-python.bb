SUMMARY = "mbs python sdcard image"

require mbs-sdcard-basic.bb

BOOT_SPACE = "65536"

CORE_IMAGE_BASE_INSTALL += " \
    python3 \
"
