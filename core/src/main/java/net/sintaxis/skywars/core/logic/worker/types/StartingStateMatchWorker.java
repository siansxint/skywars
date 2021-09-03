package net.sintaxis.skywars.core.logic.worker.types;

import me.yushust.inject.InjectAll;

import net.sintaxis.skywars.api.cache.ObjectCache;
import net.sintaxis.skywars.core.cage.spawner.CageSpawner;
import net.sintaxis.skywars.core.chest.SkyChest;
import net.sintaxis.skywars.core.chest.filler.ChestFiller;
import net.sintaxis.skywars.core.file.YamlFile;
import net.sintaxis.skywars.core.logic.announcer.CountdownAnnouncer;
import net.sintaxis.skywars.core.logic.worker.Worker;
import net.sintaxis.skywars.core.match.Match;
import net.sintaxis.skywars.core.match.info.timing.TimedMatchInfo;
import net.sintaxis.skywars.core.match.state.MatchState;
import net.sintaxis.skywars.core.scoreboard.Scoreboard;
import net.sintaxis.skywars.core.scoreboard.registry.ScoreboardRegistry;
import net.sintaxis.skywars.core.team.Team;
import net.sintaxis.skywars.core.team.finder.TeamFinder;
import net.sintaxis.skywars.core.user.finder.UserFinder;

import javax.inject.Named;

import java.util.List;

@InjectAll
public class StartingStateMatchWorker
        implements Worker {

 @Named("match-distributor-cache")
 private ObjectCache<Match> matchDistributorCache;
 private TeamFinder teamFinder;
 private CageSpawner cageSpawner;
 private UserFinder userFinder;
 private CountdownAnnouncer countdownAnnouncer;
 private ScoreboardRegistry scoreboardRegistry;
 @Named("scoreboards")
 private YamlFile scoreboards;
 private ChestFiller chestFiller;
 private ObjectCache<SkyChest> chestObjectCache;

 @Override
 public void work(Match match) {

  TimedMatchInfo timedMatchConfiguration
          = match.getTimedMatchInfo();

  List<Team> teams = teamFinder.getAliveTeams(match);

  if (teams.size() < match.getMatchInfo().getMinTeamsCapacity() ||
          teams.size() == 0) {
   timedMatchConfiguration.setStartingTime(
           timedMatchConfiguration.getDefaultStartingTime()
   );
   matchDistributorCache.remove(match.getId());
   return;
  }

  int second = timedMatchConfiguration.getStartingTime() - 1;

  if (second <= 0) {
   timedMatchConfiguration.setStartingTime(
           timedMatchConfiguration.getDefaultStartingTime()
   );
   cageSpawner.removeAllCages(match, 1);

   userFinder.getMatchPlayers(match).forEach(player -> {
    String id = player.getUniqueId().toString();

    Scoreboard scoreboard = new Scoreboard(
            scoreboards.getConfigurationSection("game-scoreboard"),
            id
    );
    scoreboard.show(player);
    scoreboardRegistry.addToRegistry(id, scoreboard);
   });

   // TODO: 12/07/2021 de-hardcode this lol
   SkyChest skyChest = chestObjectCache.find("normal")
           .orElseThrow(() -> new RuntimeException(
                   "An error occurred while searching the chest type normal!"
           ));

   match.getChests()
           .forEach(
                   coordinates -> chestFiller.fillChest(
                           coordinates,
                           skyChest
                   )
           );
   match.getTemporally().setState(MatchState.IN_GAME);
   return;
  }

  countdownAnnouncer.announce(CountdownAnnouncer.CountdownType.CAGES, match, second);
  timedMatchConfiguration.setStartingTime(second);
 }
}