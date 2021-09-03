package net.sintaxis.skywars.core.logic.worker.types;

import me.yushust.inject.InjectAll;

import net.sintaxis.skywars.api.cache.ObjectCache;
import net.sintaxis.skywars.api.processor.Processor;
import net.sintaxis.skywars.core.file.YamlFile;
import net.sintaxis.skywars.core.logic.announcer.CountdownAnnouncer;
import net.sintaxis.skywars.core.logic.worker.Worker;
import net.sintaxis.skywars.core.match.Match;
import net.sintaxis.skywars.core.match.info.timing.TimedMatchInfo;
import net.sintaxis.skywars.core.match.state.MatchState;
import net.sintaxis.skywars.core.scoreboard.Scoreboard;
import net.sintaxis.skywars.core.scoreboard.registry.ScoreboardRegistry;
import net.sintaxis.skywars.core.team.finder.TeamFinder;
import net.sintaxis.skywars.core.user.finder.UserFinder;

import javax.inject.Named;

@InjectAll
public class WaitingStateMatchWorker
        implements Worker {

 @Named("match-distributor-cache")
 private ObjectCache<Match> countdownMatchDistributor;
 private TeamFinder teamFinder;
 @Named("team-spawner")
 private Processor<Match> teamSpawnerProcessor;
 private CountdownAnnouncer countdownAnnouncer;
 private UserFinder userFinder;
 @Named("scoreboards")
 private YamlFile scoreboards;
 private ScoreboardRegistry scoreboardRegistry;

 @Override
 public void work(Match match) {

  TimedMatchInfo timedMatchConfiguration
          = match.getTimedMatchInfo();

  if (teamFinder.getAliveTeams(match).size() < match.getMatchInfo().getMinTeamsCapacity()) {
   timedMatchConfiguration.setWaitingTime(timedMatchConfiguration.getDefaultWaitingTime());
   countdownMatchDistributor.remove(match.getId());
   return;
  }

  int second = timedMatchConfiguration.getWaitingTime() - 1;

  if (second <= 0) {
   timedMatchConfiguration.setWaitingTime(timedMatchConfiguration.getDefaultWaitingTime());
   teamSpawnerProcessor.process(match);

   userFinder.getMatchPlayers(match).forEach(player -> {
    String id = player.getUniqueId().toString();

    Scoreboard scoreboard = new Scoreboard(
            scoreboards.getConfigurationSection("starting-scoreboard"),
            id
    );
    scoreboard.show(player);
    scoreboardRegistry.addToRegistry(id, scoreboard);
   });

   match.getTemporally().setState(MatchState.STARTING);
   return;
  }

  countdownAnnouncer.announce(CountdownAnnouncer.CountdownType.LOBBY, match, second);
  timedMatchConfiguration.setWaitingTime(second);
 }
}