package net.sintaxis.skywars.core.user.finder;

import net.sintaxis.skywars.core.match.Match;
import net.sintaxis.skywars.core.user.User;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class UserFinder {

 public Set<User> getMatchUsers(Match match) {
  Set<User> users = new HashSet<>();
  match.getTemporally().getTeams()
          .values()
          .forEach(team -> users.addAll(team.getMembers()));
  return users;
 }

 public Set<User> getMatchUsersByFilter(Match match, Predicate<User> userPredicate) {
  Set<User> users = new HashSet<>();
  match.getTemporally().getTeams()
          .values()
          .forEach(team -> team.getMembers()
                  .stream()
                  .filter(userPredicate)
                  .forEach(users::add));
  return users;
 }

 public Set<Player> getMatchPlayers(Match match) {
  Set<Player> players = new HashSet<>();
  getMatchUsers(match)
          .forEach(user -> user.getTemporally().getRaw().ifPresent(players::add));
  return players;
 }

 public Set<Player> getMatchPlayersByFilter(Match match, Predicate<User> userPredicate) {
  Set<Player> players = new HashSet<>();
  getMatchUsersByFilter(match, userPredicate)
          .forEach(user -> user.getTemporally().getRaw().ifPresent(players::add));
  return players;
 }
}