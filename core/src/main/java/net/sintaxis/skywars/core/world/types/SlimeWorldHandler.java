package net.sintaxis.skywars.core.world.types;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.exceptions.*;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;

import net.sintaxis.skywars.core.match.Match;
import net.sintaxis.skywars.core.world.WorldHandler;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.io.File;
import java.io.IOException;

public class SlimeWorldHandler
        implements WorldHandler {

 private final SlimePlugin slimePlugin;

 public SlimeWorldHandler(SlimePlugin slimePlugin) {
  this.slimePlugin = slimePlugin;
 }

 @Override
 public World retrieveOrCreateEmptyWorld(String worldName) {

  if (Bukkit.getWorld(WorldHandler.MATCH_WORLD_PREFIX + worldName) != null) {
   throw new RuntimeException("The world " + WorldHandler.MATCH_WORLD_PREFIX + worldName + " is already loaded!");
  }

  SlimeLoader slimeLoader = slimePlugin.getLoader("file");

  //if now exists, create a slime world empty to save
  try {
   if (!slimeLoader.worldExists(WorldHandler.MATCH_WORLD_PREFIX + worldName)) {

    //If the world can't be imported, try to create an empty world
    createBackup(worldName);
   }
  } catch (IOException exception) {
   exception.printStackTrace();
  }

  //Load the world, and generate a copy of the real world, and save the clone world in cache
  SlimeWorld slimeWorld = null;

  try {
   slimeWorld = slimePlugin.loadWorld(slimeLoader, WorldHandler.MATCH_WORLD_PREFIX + worldName, true, getDefaultProperties());
  } catch (UnknownWorldException | CorruptedWorldException | NewerFormatException | WorldInUseException | IOException exception) {
   exception.printStackTrace();
  }

  if (slimeWorld == null) {
   throw new IllegalStateException("The world " + WorldHandler.MATCH_WORLD_PREFIX + worldName + " was not loaded, check if the world exists!");
  }


  slimePlugin.generateWorld(slimeWorld.clone(WorldHandler.MATCH_WORLD_PREFIX + worldName));

  World world = Bukkit.getWorld(WorldHandler.MATCH_WORLD_PREFIX + worldName);
  world.getEntities().forEach(Entity::remove);
  return world;
 }

 @Override
 public void createBackup(String worldName) {

  try {
   slimePlugin.importWorld(new File(Bukkit.getWorldContainer(), WorldHandler.MATCH_WORLD_PREFIX + worldName), WorldHandler.MATCH_WORLD_PREFIX + worldName, slimePlugin.getLoader("file"));
  } catch (WorldAlreadyExistsException | InvalidWorldException | WorldTooBigException | WorldLoadedException | IOException e) {
   try {
    slimePlugin.createEmptyWorld(slimePlugin.getLoader("file"), WorldHandler.MATCH_WORLD_PREFIX + worldName, true, getDefaultProperties());
   } catch (WorldAlreadyExistsException | IOException exception) {
    exception.printStackTrace();
   }
  }
 }

 @Override
 public void rollbackMatch(Match match) {

  Bukkit.unloadWorld(WorldHandler.MATCH_WORLD_PREFIX + match.getId(), false);

  retrieveOrCreateEmptyWorld(match);
 }

 private SlimePropertyMap getDefaultProperties() {
  SlimePropertyMap slimePropertyMap = new SlimePropertyMap();
  slimePropertyMap.setString(SlimeProperties.DIFFICULTY, "hard");
  slimePropertyMap.setInt(SlimeProperties.SPAWN_X, 0);
  slimePropertyMap.setInt(SlimeProperties.SPAWN_Y, 101);
  slimePropertyMap.setInt(SlimeProperties.SPAWN_Z, 0);
  slimePropertyMap.setBoolean(SlimeProperties.ALLOW_MONSTERS, false);
  slimePropertyMap.setBoolean(SlimeProperties.ALLOW_ANIMALS, false);
  slimePropertyMap.setBoolean(SlimeProperties.PVP, true);
  slimePropertyMap.setString(SlimeProperties.ENVIRONMENT, "normal");
  return slimePropertyMap;
 }
}