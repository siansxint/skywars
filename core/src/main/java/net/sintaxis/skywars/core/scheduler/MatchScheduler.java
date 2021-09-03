package net.sintaxis.skywars.core.scheduler;

import net.sintaxis.skywars.core.logic.distributor.CountdownMatchDistributor;
import org.bukkit.scheduler.BukkitRunnable;

import javax.inject.Inject;

public class MatchScheduler
        extends BukkitRunnable {

 @Inject
 private CountdownMatchDistributor countdownMatchDistributor;

 @Override
 public void run() {
  countdownMatchDistributor.run();
 }
}