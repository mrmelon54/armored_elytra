#!/bin/bash
back="$(pwd)"
for plugin in valorless-haven-elytra teaks-tweaks-armored-elytra pim-armored-elytra; do
  cd "$back/run/paper-servers/$plugin" || exit
  java -jar "paper-1.20.4.jar" nogui
done
