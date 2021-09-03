package net.sintaxis.skywars.core.chest.item.parser;

import net.sintaxis.skywars.core.chest.item.ChestItem;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;

import team.unnamed.gui.core.item.type.ItemBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChestItemParser {

 public Set<ChestItem> parseAll(ConfigurationSection section) {

  Set<ChestItem> items = new HashSet<>();

  for (String key : section.getKeys(false)) {
   ItemBuilder itemBuilder = ItemBuilder
           .newBuilder(
                   Material.valueOf(section.getString(key + ".material").toUpperCase()),
                   1,
                   section.isSet(key + ".data") ? (byte) section.getInt(key + ".data") : 0)
           .setName(ChatColor.translateAlternateColorCodes('&', section.getString(key + ".name")))
           .setLore(colorize(section.getStringList(key + ".lore")))
           .addFlag(ItemFlag.HIDE_ENCHANTS)
           .addFlag(ItemFlag.HIDE_ATTRIBUTES);

   for (String serializedEnchantment : section.getStringList(key + ".enchantments")) {
    String[] splittedEnchantment = serializedEnchantment.split(":");
    itemBuilder.addEnchant(Enchantment.getByName(splittedEnchantment[0].toUpperCase()), Integer.parseInt(splittedEnchantment[1]));
   }

   items.add(new ChestItem(
           section.getInt(key + ".probability"),
           section.getInt(key + ".min"),
           section.getInt(key + ".max"),
           itemBuilder.build())
   );
  }

  return items;
 }

 private List<String> colorize(List<String> list) {
  list.replaceAll(line -> ChatColor.translateAlternateColorCodes('&', line));
  return list;
 }
}