package net.sintaxis.skywars.core.scheduler;

import net.sintaxis.skywars.core.tag.NameTagApplier;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import javax.inject.Inject;

public class NameTagScheduler
        extends BukkitRunnable {

 @Inject
 private NameTagApplier nameTagApplier;

 @Override
 public void run() {
  for (Player player : Bukkit.getOnlinePlayers()) {
   nameTagApplier.applyToPlayer(player);
  }
 }
}