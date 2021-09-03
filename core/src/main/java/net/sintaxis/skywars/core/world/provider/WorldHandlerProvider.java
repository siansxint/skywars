package net.sintaxis.skywars.core.world.provider;

import com.grinderwolf.swm.api.SlimePlugin;

import me.yushust.inject.InjectAll;

import net.sintaxis.skywars.core.SkyWars;
import net.sintaxis.skywars.core.file.YamlFile;
import net.sintaxis.skywars.core.world.WorldHandler;
import net.sintaxis.skywars.core.world.types.FileWorldHandler;
import net.sintaxis.skywars.core.world.types.SlimeWorldHandler;

import org.bukkit.Bukkit;

import javax.inject.Provider;

@InjectAll
public class WorldHandlerProvider
        implements Provider<WorldHandler> {

 private YamlFile config;
 private SkyWars skyWars;

 @Override
 public WorldHandler get() {

  String worldHandlerType = config.getString("world-handler.type")
          .toLowerCase();

  switch (worldHandlerType) {
   case "file":
    return new FileWorldHandler(skyWars);

   case "slime":
    SlimePlugin slimePlugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");

    if (slimePlugin == null) {
     throw new RuntimeException("You need SlimeWorldManager in your server to use this world handler type!");
    }

    return new SlimeWorldHandler(slimePlugin);
   default:
    throw new IllegalArgumentException("Failed to parse the world handler name!");
  }
 }
}