package net.sintaxis.skywars.core.service;

import me.yushust.inject.InjectAll;

import net.sintaxis.skywars.api.cache.ObjectCache;
import net.sintaxis.skywars.api.model.Service;
import net.sintaxis.skywars.core.SkyWars;
import net.sintaxis.skywars.core.chest.SkyChest;
import net.sintaxis.skywars.core.chest.parser.ChestParser;
import net.sintaxis.skywars.core.file.YamlFile;

import java.io.File;

@InjectAll
public class SkyChestService
        implements Service {

 private ObjectCache<SkyChest> chestObjectCache;
 private ChestParser chestParser;
 private SkyWars skyWars;

 @Override
 public void start() {
  File folder = new File(skyWars.getDataFolder(), "chests");

  if (!folder.exists()) {
   if (folder.mkdirs()) {
    skyWars.saveResource("chests/normal.yml", false);
   }
  }

  File[] files = folder.listFiles(file -> file.getName().endsWith(".yml"));

  if (files == null) {
   return;
  }

  for (File file : files) {

   String name = file.getName();

   YamlFile fileCreator = new YamlFile(skyWars, folder, name);
   chestObjectCache.add(name.replace(".yml", ""), chestParser.parse(fileCreator));
  }
 }
}