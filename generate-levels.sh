#!/bin/bash
function mkcp() {
  echo "Copying $1 to $2"
  mkdir -p -- "`dirname "$2"`"
  cp "$1" "$2"
}

function checkDatapack() {
  # 1 - path, 2 - name
  if [ -z "$1" ]; then
    echo "Missing $2 datapack in test-levels/"
    exit 1
  fi
  echo "Found $2 datapack: $1"
}

armoredElytraZip=`find test-levels/ -name 'armored elytra *.zip' -print`
platedElytraZip=`find test-levels/ -name 'Plated_Elytra_*.zip' -print`

checkDatapack "$armoredElytraZip" "Armored Elytra"
checkDatapack "$platedElytraZip" "PlatedElytra"

echo

for platform in fabric forge quilt; do
  mkcp "test-levels/options.txt" "$platform/run/options.txt"
  mkcp "test-levels/Armored-Elytra-level.dat" "$platform/run/saves/Armored Elytra/level.dat"
  mkcp "$armoredElytraZip" "$platform/run/saves/Armored Elytra/datapacks/$(basename "$armoredElytraZip")"
  mkcp "test-levels/Plated-Elytra-level.dat" "$platform/run/saves/Plated Elytra/level.dat"
  mkcp "$platedElytraZip" "$platform/run/saves/Plated Elytra/datapacks/$(basename "$platedElytraZip")"
done
