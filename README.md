# Minecraft-Messaging-System

This project is a messaging system mod for minecraft

# Description
This mod implements a messaging system (similar to the [Souls games](https://darksouls.fandom.com/wiki/Messages)) so players can leave messages to each other in singleplayer mode. 
The 'Messaging Crystal' which can be crafted from 2 diamonds in survival mode, allows a player to leave a message in the spot they stand on in their singleplayer world, for other players to see in their world.

# Features
- :writing_hand: Leave your own messages and read messages of other players
- :thumbsup: Like messages
- :wastebasket: Permanently delete your own messages from existence

# Running The Mod
The mod is not yet published so you can only run it localy with the server included in the repository. Make sure to set your MongoDB username and password in the ```.env``` file. To run the server you can use ```nodemon server``` command in the terminal.

The most simple way to try the mod out is to download [forge for 1.19.2](https://files.minecraftforge.net/net/minecraftforge/forge/), then copy the .jar file in ```mod jar/``` folder, to ```%appdata%/.minecraft/mods``` (if you don't have a mods folder create it). After you are done, open the minecraft launcher and select the forge profile created.

Alternatively as a developer, you can create a workspace in eclipse under the ```workspace``` folder and import ```firstmod``` as a gradle project. In eclipse navigate to ```gradle tasks``` tab in the lower panel. Run ```eclipse``` under ```ide```, run ```genEclipseRuns``` under ```forgegradle runs``` and finally run ```runclient``` under the same folder, now minecraft should open.
#### **_See another explanation of this under_ ```workspace/firstmod/README.txt```**.
Important to note that if you choose to run the mod by the second option, the messages that you write, will never show who wrote it because your player UUID in this environment is not real.
