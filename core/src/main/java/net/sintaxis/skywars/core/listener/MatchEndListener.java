package net.sintaxis.skywars.core.listener;

import me.yushust.inject.InjectAll;
import me.yushust.message.MessageHandler;

import net.sintaxis.skywars.core.event.MatchEndEvent;
import net.sintaxis.skywars.core.match.Match;
import net.sintaxis.skywars.core.match.state.MatchState;
import net.sintaxis.skywars.core.packet.title.TitleMessenger;
import net.sintaxis.skywars.core.team.Team;
import net.sintaxis.skywars.core.team.finder.TeamFinder;
import net.sintaxis.skywars.core.user.User;
import net.sintaxis.skywars.core.user.finder.UserFinder;
import net.sintaxis.skywars.core.util.PlayerResetUtil;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

@InjectAll
public class MatchEndListener
        implements Listener {

 private TeamFinder teamFinder;
 private UserFinder userFinder;
 private MessageHandler messageHandler;
 private TitleMessenger titleMessenger;

 @EventHandler
 public void onMatchEnd(MatchEndEvent event) {

  Match match = event.getMatch();

  List<Team> teams = teamFinder.getAliveTeams(match);
  if (teams.size() > 1) {
   event.setCancelled(true);
   return;
  }

  if (teams.size() == 1) {
   for (User user : teams.get(0)) {
    user.getTemporally().getRaw().ifPresent(player -> titleMessenger.sendBoth(
            player,
            messageHandler.get(player, "user-events.victory.title"),
            messageHandler.get(player, "user-events.victory.sub-title"),
            20,
            20,
            20));
    user.getStatisticsByType(match.getMatchType())
            .getWins().add(1);
   }
  }

  userFinder.getMatchPlayers(match)
          .forEach(PlayerResetUtil::reset);

  match.getTemporally().setState(MatchState.ENDING);
 }
}