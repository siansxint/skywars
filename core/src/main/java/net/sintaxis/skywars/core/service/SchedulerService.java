package net.sintaxis.skywars.core.service;

import me.yushust.inject.InjectAll;

import net.sintaxis.skywars.api.model.Service;
import net.sintaxis.skywars.core.SkyWars;
import net.sintaxis.skywars.core.scheduler.MatchScheduler;
import net.sintaxis.skywars.core.scheduler.NameTagScheduler;
import net.sintaxis.skywars.core.scheduler.ScoreboardScheduler;

@InjectAll
public class SchedulerService
        implements Service {

 private SkyWars skyWars;
 private MatchScheduler matchScheduler;
 private ScoreboardScheduler scoreboardScheduler;
 private NameTagScheduler nameTagScheduler;

 @Override
 public void start() {
  matchScheduler.runTaskTimer(skyWars, 600, 20);
  scoreboardScheduler.runTaskTimerAsynchronously(skyWars, 300, 1);
  nameTagScheduler.runTaskTimer(skyWars, 20, 20);
 }

 @Override
 public void stop() {
  matchScheduler.cancel();
  scoreboardScheduler.cancel();
  nameTagScheduler.cancel();
 }
}