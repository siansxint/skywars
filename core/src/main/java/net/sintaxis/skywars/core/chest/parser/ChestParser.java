package net.sintaxis.skywars.core.chest.parser;

import net.sintaxis.skywars.core.chest.SkyChest;
import net.sintaxis.skywars.core.chest.item.parser.ChestItemParser;
import net.sintaxis.skywars.core.file.YamlFile;

import javax.inject.Inject;

public class ChestParser {

 @Inject
 private ChestItemParser itemParser;

 public SkyChest parse(YamlFile file) {
  return new SkyChest(
          file.getName().replace(".yml", ""),
          itemParser.parseAll(file.getConfigurationSection("items"))
  );
 }
}