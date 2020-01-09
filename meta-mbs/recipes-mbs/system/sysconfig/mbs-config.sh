#!/bin/sh
data=/media/data
tmp=$(mktemp -d -t sysconfig.XXXXXX)

trap 'rm -rf $tmp' EXIT

cd $tmp
if [ -f $data/config/system.config ]; then
  cp $data/config/system.config .config
fi
kconfig-conf --olddefconfig /etc/sysconf/system.kconfig >/dev/null
kconfig-conf --oldaskconfig /etc/sysconf/system.kconfig

mkdir -p $data/config
if [ -d $data/config ]; then
  cp .config $data/config/.config
  sync
  mv $data/config/.config $data/config/system.config
  sync
fi
