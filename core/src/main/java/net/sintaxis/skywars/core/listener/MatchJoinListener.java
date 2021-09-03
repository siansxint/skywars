package net.sintaxis.skywars.core.listener;

import me.yushust.inject.InjectAll;
import me.yushust.message.MessageHandler;

import net.sintaxis.skywars.api.cache.ObjectCache;
import net.sintaxis.skywars.core.coordinate.AbbreviatedCoordinates;
import net.sintaxis.skywars.core.match.Match;
import net.sintaxis.skywars.core.match.info.match.MatchInfo;
import net.sintaxis.skywars.core.match.info.timing.TimedMatchInfo;
import net.sintaxis.skywars.core.team.Team;
import net.sintaxis.skywars.core.team.finder.TeamFinder;
import net.sintaxis.skywars.core.user.User;
import net.sintaxis.skywars.core.util.PlayerResetUtil;
import net.sintaxis.skywars.core.event.MatchJoinEvent;
import net.sintaxis.skywars.core.file.YamlFile;
import net.sintaxis.skywars.core.match.state.MatchState;
import net.sintaxis.skywars.core.scoreboard.Scoreboard;
import net.sintaxis.skywars.core.scoreboard.registry.ScoreboardRegistry;
import net.sintaxis.skywars.core.user.state.UserState;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import javax.inject.Named;

import java.util.Optional;

@InjectAll
public class MatchJoinListener
        implements Listener {

 private TeamFinder teamFinder;
 private MessageHandler messageHandler;
 @Named("match-distributor-cache")
 private ObjectCache<Match> matchDistributorObjectCache;
 private ScoreboardRegistry scoreboardRegistry;
 @Named("scoreboards")
 private YamlFile scoreboards;

 @EventHandler
 public void onMatchJoin(MatchJoinEvent event) {

  User user = event.getUser();
  Match match = event.getRequestedMatch();

  Optional<Team> optionalTeam = teamFinder.findAvailableTeam(match);

  if (!optionalTeam.isPresent()) {
   messageHandler.sendReplacing(user, "match.full-team");
   return;
  }

  Match.Temporally matchTemporally = match.getTemporally();
  User.Temporally userTemporally = user.getTemporally();

  Team team = optionalTeam.get();

  team.getMembers().add(user);
  userTemporally.setTeamName(team.getName());
  matchTemporally.setCurrentPlayers(matchTemporally.getCurrentPlayers() + 1);
  userTemporally.setMatchName(match.getId());
  userTemporally.setRole(UserState.ALIVE);

  Player player = userTemporally.getRaw()
          .orElseThrow(() -> new RuntimeException("An error occurred while obtaining the player information of the user " + user.getId()));

  PlayerResetUtil.reset(player);

  Scoreboard scoreboard = new Scoreboard(
          scoreboards.getConfigurationSection("waiting-scoreboard"),
          user.getId()
  );

  scoreboard.show(player);

  scoreboardRegistry.addToRegistry(user.getId(), scoreboard);

  if (matchTemporally.getState() == MatchState.WAITING) {
   player.teleport(AbbreviatedCoordinates.toLocation(match.getMapInfo().getLobbyCoordinates()));
  } else if (matchTemporally.getState() == MatchState.STARTING) {
   team.getCoordinates().ifPresent(coordinates -> player.teleport(AbbreviatedCoordinates.toLocation(coordinates)));
  }

  MatchInfo matchInfo = match.getMatchInfo();

  messageHandler.sendReplacing(
          matchTemporally.getTeams().values(),
          "match.join",
          "%player-name%", player.getName(),
          "%current_players%", matchTemporally.getCurrentPlayers(),
          "%maximum_players%", (matchInfo.getMaxTeamsCapacity() * matchInfo.getMembersPerTeamSize()));

  TimedMatchInfo timedMatchConfiguration = match.getTimedMatchInfo();

  if (teamFinder.getAliveTeams(match).size() >= matchInfo.getMinTeamsCapacity() &&
         timedMatchConfiguration.getWaitingTime() == timedMatchConfiguration.getDefaultWaitingTime()) {
   matchDistributorObjectCache.add(match.getId(), match);
  }
 }
}