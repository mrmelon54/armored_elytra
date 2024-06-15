#!/bin/bash
function mkcp() {
  echo "Copying $1 to $2"
  mkdir -p -- "`dirname "$2"`"
  if ! cp "$1" "$2"; then
    echo "Failed, stopping execution"
    exit 1
  fi
}

function checkDatapack() {
  # 1 - path, 2 - name
  if [ ! -f "$1" ]; then
    echo "Missing $2 datapack in test-levels/"
    exit 1
  fi
  echo "Found $2 datapack: $1"
}

function downloadToFile() {
  curl -X GET "$1" -o "$2"
}

function downloadArmoredElytra() {
  result="$(curl -X POST https://vanillatweaks.net/assets/server/zipdatapacks.php -d 'packs={"items":["armored+elytra"]}' -d 'version=1.20')"
  link="https://vanillatweaks.net/$(echo "$result" | jq '.link' -r)"
  echo "Downloading Armored Elytra from $link"
  downloadToFile "$link" "vanilla-tweaks-armored-elytra-wrap.zip"
  unzip "vanilla-tweaks-armored-elytra-wrap.zip"
  final="$(find . -name 'armored elytra *')"
  mv "$final" "vanilla-tweaks-armored-elytra.zip"
}

mkdir -p pack-downloads/
cd pack-downloads || exit

if [ ! -f "paper-1.20.4.jar" ]; then
  downloadToFile "https://api.papermc.io/v2/projects/paper/versions/1.20.4/builds/497/downloads/paper-1.20.4-497.jar" "paper-1.20.4.jar"
fi

if [ ! -f "pim-armored-elytra.jar" ]; then
  echo "Please download Pim Armored Elytra from this link: https://www.spigotmc.org/resources/armored-elytra.47136/download?version=427798"
  echo "Rename it to 'pim-armored-elytra.jar' to allow this script to continue"
  exit 1
fi

if [ ! -f "teaks-tweaks-armored-elytra.jar" ]; then
  downloadToFile "https://cdn.modrinth.com/data/Xdn5t532/versions/mkHGM4AJ/TeaksTweaks%20-%201.10.6-beta.jar" "teaks-tweaks-armored-elytra.jar"
fi

if [ ! -f "illusivec4-colytra.jar" ]; then
  downloadToFile "https://cdn.modrinth.com/data/ulY7WPQy/versions/1D5z9JcI/colytra-fabric-6.2.2%2B1.20.1.jar" "illusivec4-colytra.jar"
fi

if [ ! -f "valorless-haven-elytra.jar" ]; then
  echo "Please download Valorless Haven Elytra from this link: https://www.spigotmc.org/resources/havenelytra-combined-chestplates-and-elytras-1-18-1-20-4.109583/download?version=540821"
  echo "Rename it to 'valorless-haven-elytra.jar' to allow this script to continue"
  exit 1
fi

if [ ! -f "valorlessutils.jar" ]; then
  echo "Please download ValorlessUtils from this link: https://www.spigotmc.org/resources/valorlessutils-1-17-1-20-4.109586/download?version=540882"
  echo "Rename it to 'valorlessutils.jar' to allow this script to continue"
  exit 1
fi

if [ ! -f "vanilla-tweaks-armored-elytra.zip" ]; then
  downloadArmoredElytra
fi

if [ ! -f "voodoo-tweaks-plated-elytra.zip" ]; then
  downloadToFile "https://mc.voodoobeard.com/downloads/Datapacks/1.20.4/Plated_Elytra_3.5.1.zip" "voodoo-tweaks-plated-elytra.zip"
fi

checkDatapack "pim-armored-elytra.jar" "Pim16aap2 Armored Elytra"
checkDatapack "teaks-tweaks-armored-elytra.jar" "Teaks Tweaks Armored Elytra"
checkDatapack "illusivec4-colytra.jar" "TheIllusiveC4 Colytra"
checkDatapack "valorless-haven-elytra.jar" "Valorless Haven Elytra"
checkDatapack "vanilla-tweaks-armored-elytra.zip" "Vanilla Tweaks Armored Elytra"
checkDatapack "voodoo-tweaks-plated-elytra.zip" "Voodoo Tweaks Plated Elytra"

cd .. || exit

echo

for platform in fabric forge quilt; do
  mkcp "test-levels/options.txt" "$platform/run/options.txt"
  for pack in vanilla-tweaks-armored-elytra voodoo-tweaks-plated-elytra; do
    mkcp "test-levels/$pack-level.dat" "$platform/run/saves/$pack/level.dat"
    mkcp "pack-downloads/$pack.zip" "$platform/run/saves/$pack/datapacks/$pack.zip"
  done

  mkcp "pack-downloads/illusivec4-colytra.jar" "$platform/run/saves/illusivec4-colytra/mods/illusivec4-colytra.jar"
done

for plugin in valorless-haven-elytra teaks-tweaks-armored-elytra pim-armored-elytra; do
  mkcp "test-levels/$plugin-level.dat" "run/paper-servers/$plugin/world/level.dat"
  mkcp "pack-downloads/$plugin.jar" "run/paper-servers/$plugin/plugins/$plugin.jar"
  if [ "$plugin" == "valorless-haven-elytra" ]; then
    mkcp "pack-downloads/valorlessutils.jar" "run/paper-servers/$plugin/plugins/valorlessutils.jar"
  fi
  mkcp "pack-downloads/paper-1.20.4.jar" "run/paper-servers/$plugin/paper-1.20.4.jar"
  echo "eula=true" > "run/paper-servers/$plugin/eula.txt"
  echo "online-mode=false" > "run/paper-servers/$plugin/server.properties"
done
