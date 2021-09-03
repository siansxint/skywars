package net.sintaxis.skywars.core.listener;

import me.yushust.inject.InjectAll;
import me.yushust.message.MessageHandler;

import net.sintaxis.skywars.api.cache.ObjectCache;
import net.sintaxis.skywars.core.match.Match;
import net.sintaxis.skywars.core.match.info.match.MatchInfo;
import net.sintaxis.skywars.core.match.state.MatchState;
import net.sintaxis.skywars.core.event.MatchLeaveEvent;
import net.sintaxis.skywars.core.file.YamlFile;
import net.sintaxis.skywars.core.scoreboard.Scoreboard;
import net.sintaxis.skywars.core.scoreboard.registry.ScoreboardRegistry;
import net.sintaxis.skywars.core.user.User;
import net.sintaxis.skywars.core.user.state.UserState;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import javax.inject.Named;

@InjectAll
public class MatchLeaveListener
        implements Listener {

 private ObjectCache<Match> matchObjectCache;
 private MessageHandler messageHandler;
 private ScoreboardRegistry scoreboardRegistry;
 @Named("scoreboards")
 private YamlFile scoreboards;
 private YamlFile config;

 @EventHandler
 public void onMatchLeave(MatchLeaveEvent event) {

  User user = event.getUser();
  Player player = user.getTemporally().getRaw()
          .orElseThrow(() -> new RuntimeException("An error occurred while obtaining the player information of the user " + user.getId()));

  user.getTemporally().getMatchName()
          .flatMap(matchObjectCache::find)
          .ifPresent(match -> {

           Match.Temporally matchTemporally = match.getTemporally();

           matchTemporally.getTeams().values().forEach(team -> team.getMembers().remove(user));
           matchTemporally.setCurrentPlayers(matchTemporally.getCurrentPlayers() - 1);

           World lobbyWorld = Bukkit.getWorld(config.getString("lobby-world-name"));
           player.teleport(lobbyWorld == null ? Bukkit.getWorlds().get(0).getSpawnLocation() : lobbyWorld.getSpawnLocation());

           User.Temporally userTemporally = user.getTemporally();

           userTemporally.setRole(UserState.LOBBY);
           userTemporally.setMatchName(null);
           userTemporally.setTeamName(null);
           userTemporally.getMatchKills().set(0);

           Scoreboard scoreboard = new Scoreboard(
                   scoreboards.getConfigurationSection("lobby-scoreboard"),
                   player.getUniqueId().toString()
           );

           scoreboard.show(player);
           scoreboardRegistry.addToRegistry(player.getUniqueId().toString(), scoreboard);

           if ((event.getCause() == MatchLeaveEvent.Cause.DISCONNECT ||
                   event.getCause() == MatchLeaveEvent.Cause.COMMAND) &&
                   match.getTemporally().getState() == MatchState.IN_GAME
           ) {
            user.getStatisticsByType(match.getMatchType())
                    .getDeaths().add(1);
            user.getStatisticsByType(match.getMatchType())
                    .getLosses().add(1);

            MatchInfo matchInfo = match.getMatchInfo();

            messageHandler.sendReplacing(
                    match.getTemporally().getTeams().values(),
                    "match.leave",
                    "%player-name%", player.getName(),
                    "%current_players%", match.getTemporally().getCurrentPlayers(),
                    "%maximum_players%", (matchInfo.getMaxTeamsCapacity() * matchInfo.getMembersPerTeamSize())
            );
           }
          });
 }
}