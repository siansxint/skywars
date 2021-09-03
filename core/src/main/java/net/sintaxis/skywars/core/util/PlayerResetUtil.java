package net.sintaxis.skywars.core.util;

import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PlayerResetUtil {

 public static void reset(Player player) {
  player.setHealth(player.getMaxHealth());
  player.setFoodLevel(20);
  player.getInventory().clear();
  player.getInventory().setArmorContents(null);
  player.setGameMode(GameMode.SURVIVAL);
  player.setExp(0);
  player.setLevel(0);
  player.setAllowFlight(false);
  player.setFlying(false);
  player.setFireTicks(0);
  player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
  ((CraftPlayer) player).getHandle().getDataWatcher().watch(9, (byte) 0);
 }
}