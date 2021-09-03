package net.sintaxis.skywars.core.command;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.Named;
import me.fixeddev.commandflow.bukkit.annotation.Sender;

import me.yushust.inject.InjectAll;
import me.yushust.message.MessageHandler;

import net.sintaxis.skywars.core.event.MatchJoinEvent;
import net.sintaxis.skywars.core.event.MatchLeaveEvent;
import net.sintaxis.skywars.core.match.Match;
import net.sintaxis.skywars.core.match.finder.MatchFinder;
import net.sintaxis.skywars.core.match.state.MatchState;
import net.sintaxis.skywars.core.match.type.MatchType;
import net.sintaxis.skywars.core.team.finder.TeamFinder;
import net.sintaxis.skywars.core.user.User;
import net.sintaxis.skywars.core.user.state.UserState;

import org.bukkit.Bukkit;

import java.util.Optional;

@Command(names = {"sw", "skywars"})
@InjectAll
public class SkyWarsMainCommand
        implements CommandClass {

 private MatchFinder matchFinder;
 private TeamFinder teamFinder;
 private MessageHandler messageHandler;

 @Command(names = "join")
 public void joinCommand(@Sender User user,
                         @Named("match-name") Match match
 ) {
  if (user.getTemporally().getRole() != UserState.LOBBY) {
   messageHandler.send(user, "match.already-in-match");
   return;
  }

  if (match.getTemporally().getState() != MatchState.WAITING &&
          match.getTemporally().getState() != MatchState.STARTING) {
   messageHandler.send(user, "match.not-available");
   return;
  }

  if (teamFinder.getAliveTeams(match).size() >= match.getMatchInfo().getMaxTeamsCapacity()) {
   messageHandler.send(user, "match.full-match");
   return;
  }

  Bukkit.getPluginManager().callEvent(new MatchJoinEvent(user, match));
 }

 @Command(names = "random")
 public void randomCommand(@Sender User user,
                           @Named("match-type") MatchType type
 ) {

  Optional<Match> optionalMatch = matchFinder.findAvailableMatch(type);

  if (!optionalMatch.isPresent()) {
   messageHandler.sendReplacing(
           user,
           "match.any-match-found",
           "%match-type%", type.toString().toLowerCase());
   return;
  }

  joinCommand(user, optionalMatch.get());
 }

 @Command(names = "leave")
 public void leaveCommand(@Sender User user) {

  if (!user.getTemporally().getMatchName().isPresent()) {
   messageHandler.send(user, "match.not-in-game");
   return;
  }

  Bukkit.getPluginManager().callEvent(new MatchLeaveEvent(user, MatchLeaveEvent.Cause.COMMAND));
 }
}