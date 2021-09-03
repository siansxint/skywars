package net.sintaxis.skywars.core.world.types;

import net.sintaxis.skywars.core.SkyWars;
import net.sintaxis.skywars.core.match.Match;
import net.sintaxis.skywars.core.world.WorldHandler;

import org.apache.commons.io.FileUtils;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Biome;
import org.bukkit.entity.Entity;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import javax.inject.Inject;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FileWorldHandler
        implements WorldHandler {

 private final File folder;

 private static final ChunkGenerator EMPTY_CHUNK_GENERATOR
         = new EmptyChunkGenerator();

 @Inject
 public FileWorldHandler(SkyWars skyWars) {
  folder = new File(skyWars.getDataFolder() + "/match-backups");
 }

 @Override
 public World retrieveOrCreateEmptyWorld(String worldName) {

  World world = WorldCreator.name(MATCH_WORLD_PREFIX + worldName)
          .environment(World.Environment.NORMAL)
          .generator(EMPTY_CHUNK_GENERATOR)
          .generateStructures(false)
          .createWorld();

  world.setDifficulty(Difficulty.NORMAL);
  world.setAutoSave(false);
  world.setGameRuleValue("mobGriefing", "true");
  world.setGameRuleValue("doFireTick", "false");
  world.setGameRuleValue("showDeathMessages", "true");
  world.setGameRuleValue("doDaylightCycle", "false");
  world.setGameRuleValue("randomTickSpeed", "0");
  world.getWorldBorder().reset();
  world.setStorm(false);
  world.setThundering(false);

  world.getEntities().forEach(Entity::remove);

  return world;
 }

 @Override
 public void createBackup(String worldName) {

  World world = Bukkit.getWorld(MATCH_WORLD_PREFIX + worldName);

  if (world == null) {
   return;
  }

  File backupFile = new File(folder, MATCH_WORLD_PREFIX + worldName);

  try {
   FileUtils.copyDirectory(world.getWorldFolder(), backupFile, false);
  } catch (IOException exception) {
   exception.printStackTrace();
  }
 }

 @Override
 public void rollbackMatch(Match match) {

  Bukkit.unloadWorld(MATCH_WORLD_PREFIX + match.getId(), false);

  File backupDirectory = new File(folder, MATCH_WORLD_PREFIX + match.getId());
  File worldFolder = new File(Bukkit.getWorldContainer(), MATCH_WORLD_PREFIX + match.getId());

  try {
   FileUtils.cleanDirectory(worldFolder);
  } catch (IOException exception) {
   exception.printStackTrace();
  }

  if (worldFolder.delete()) {
   try {
    FileUtils.copyDirectory(backupDirectory, new File(Bukkit.getWorldContainer(), MATCH_WORLD_PREFIX + match.getId()), false);
   } catch (IOException exception) {
    exception.printStackTrace();
   }

   retrieveOrCreateEmptyWorld(match);
  }
 }

 private static class EmptyChunkGenerator
         extends ChunkGenerator {

  @Override
  public List<BlockPopulator> getDefaultPopulators(World world) {
   return Collections.emptyList();
  }

  @Override
  public boolean canSpawn(World world, int x, int z) {
   return true;
  }

  @Override
  public ChunkData generateChunkData(World world, Random random, int cx, int cz, BiomeGrid biome) {
   ChunkData chunkData = createChunkData(world);

   for (int x = 0; x <= 15; x++) {
    for (int z = 0; z <= 15; z++) {
     biome.setBiome(x, z, Biome.PLAINS);
    }
   }

   if (cx == 0 && cz == 0) {
    chunkData.setBlock(0, 100, 0, Material.BEDROCK);
   }

   return chunkData;
  }

  @Override
  public Location getFixedSpawnLocation(World world, Random random) {
   return new Location(
           world,
           0,
           101,
           0
   );
  }
 }
}