#!/bin/sh
CMDPATH=$(cd $(dirname $0) && pwd)

ip="$1"
image="$2"

if [ -z "$ip" ] || [ -z "$image" ]; then
  echo "usage: $(basename $0) <ip> <update>"
  exit 0
fi

sshopt="-o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -o LogLevel=ERROR -o ConnectTimeout=5"
image="$CMDPATH"/../deploy/images/rpi3-64/$image-rpi3-64.update.tar.gz

if ! ssh $sshopt -q root@$ip exit; then
  echo "device not available"
  exit 1
fi

if ! [ -f "$image" ]; then
  echo "image not found"
  exit 1
fi

ssh $sshopt root@$ip "mount -o remount,rw /boot"
ssh $sshopt root@$ip "
  tar -xzv -C /boot
  cd /boot
  if md5sum -c update.md5; then
    for file in kernel8.img rootfs.sqfs; do
      mv \$file.update \$file
    done
    sync
    echo 'restarting...'
    neorc -o reboot
  else
    rm -rf /boot/update.md5 /boot/*.update
    mount -o remount,ro /boot
    echo '==> ERROR update failed'
  fi
" < "$image"
