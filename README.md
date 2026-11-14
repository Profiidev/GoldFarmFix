# GoldFarmFix

Paper plugin that brings back zombified piglin gold farms.

Minecraft 1.21.5 fixed [MC-56653](https://bugs.mojang.com/browse/MC-56653): angered zombified piglins
killed by anything other than a player (fall damage, magma blocks, entity cramming, ...) no longer drop
XP or player-kill loot such as gold ingots. This plugin reverts that fix.

## Behavior

- When a zombified piglin targets a player, the plugin remembers that player.
- When an angered zombified piglin takes damage, the remembered player is credited as the killer,
  so XP and rare drops work as before 1.21.5.
- The player must be online for the kill to count.

## Installation

Download the jar from the [latest release](../../releases/latest) and put it in your server's `plugins` folder.
Requires Paper 26.3 or newer.

## Building

```sh
./gradlew build      # jar in build/libs
./gradlew runServer  # local test server with the plugin
```

Requires Java 25.

## Releasing

Run the **Release Version** workflow with a version like `v1.0.0`. It opens a PR that bumps
`gradle.properties`, merges it, tags the commit and publishes a GitHub release with the jar.
