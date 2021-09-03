package net.sintaxis.skywars.core.placeholder.types;

import me.yushust.inject.InjectAll;

import net.sintaxis.skywars.api.cache.ObjectCache;
import net.sintaxis.skywars.core.match.Match;
import net.sintaxis.skywars.core.match.event.MatchEvent;
import net.sintaxis.skywars.core.match.event.types.NoneEvent;
import net.sintaxis.skywars.core.placeholder.Placeholder;
import net.sintaxis.skywars.core.team.finder.TeamFinder;
import net.sintaxis.skywars.core.user.User;

import org.bukkit.entity.Player;

import java.util.Optional;

@InjectAll
public class MatchPlaceholder
        implements Placeholder {

 private ObjectCache<Match> matchObjectCache;
 private ObjectCache<User> userObjectCache;
 private TeamFinder teamFinder;

 @Override
 public String getName() {
  return "match";
 }

 @Override
 public String apply(Player player, String text) {

  if (player == null) {
   return "";
  }

  Optional<User> optionalUser = userObjectCache.find(player.getUniqueId().toString());

  if (!optionalUser.isPresent()) {
   return "";
  }

  Optional<String> optionalMatchName = optionalUser.get().getTemporally().getMatchName();

  if (!optionalMatchName.isPresent()) {
   return "";
  }

  Optional<Match> optionalMatch = matchObjectCache.find(optionalMatchName.get());

  if (!optionalMatch.isPresent()) {
   return "";
  }

  Match match = optionalMatch.get();

  // TODO: 12/07/2021 ugly, refactor this
  switch (text) {
   case "event-name":
    return getNowEvent(match).getType().toString().toLowerCase();
   case "event-time":
    return formatTime(getNowEvent(match).getTimeUntilNextEvent()) + "";
   case "match-name":
    return match.getId();
   case "match-state":
    return match.getTemporally().getState().name();
   case "match-type":
    return match.getMatchType().name();
   case "alive-players":
    return match.getTemporally().getCurrentPlayers() + "";
   case "alive-teams":
    return teamFinder.getAliveTeams(match).size() + "";
   case "max-teams":
    return match.getMatchInfo().getMaxTeamsCapacity() * match.getMatchInfo().getMembersPerTeamSize() + "";
   case "waiting-time":
    return formatTime(match.getTimedMatchInfo().getWaitingTime()) + "";
   case "starting-time":
    return formatTime(match.getTimedMatchInfo().getStartingTime()) + "";
   default:
    return "";
  }
 }

 private MatchEvent getNowEvent(Match match) {
  for (MatchEvent matchEvent : match.getEvents()) {
   if (matchEvent.getTimeUntilNextEvent() > 0) {
    return matchEvent;
   }
  }
  return new NoneEvent();
 }

 private String formatTime(int timeInSeconds) {
  return String.format("%02d:%02d", (timeInSeconds / 60) % 60, timeInSeconds % 60);
 }
}