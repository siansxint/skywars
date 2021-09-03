package net.sintaxis.skywars.core.placeholder.applier;

import me.clip.placeholderapi.PlaceholderAPI;

import net.sintaxis.skywars.core.placeholder.Placeholder;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Taken from: https://github.com/PatoTheBest/MiniGames/blob/master/GameCore/src/main/java/me/patothebest/gamecore/placeholder/PlaceHolderManager.java
 * Author: PatoTheBest
 */

public class PlaceholderApplier {

 private static final Set<Placeholder> PLACEHOLDERS = new HashSet<>();
 private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{(?:skywars_)?(.*?)(?::(.*?))?}");

 public static String replace(Player player, String text) {

  Matcher matcher = PLACEHOLDER_PATTERN.matcher(text);

  while (matcher.find()) {
   String replacedPortion = matcher.group();
   String placeholderName = matcher.group(1);
   String data = matcher.group(2);
   for (Placeholder placeHolder : PLACEHOLDERS) {
    if (placeHolder.getName().equalsIgnoreCase(placeholderName)) {
     text = text.replace(replacedPortion, placeHolder.apply(player, data));
     break;
    }
   }
  }

  return ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, text));
 }

 public Set<Placeholder> getPlaceHolders() {
  return PLACEHOLDERS;
 }
}