package net.sintaxis.skywars.core.team.finder;

import net.sintaxis.skywars.core.match.Match;
import net.sintaxis.skywars.core.team.Team;
import net.sintaxis.skywars.core.user.state.UserState;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TeamFinder {

 public Optional<Team> findAvailableTeam(Match match) {
  return match
          .getTemporally()
          .getTeams()
          .values()
          .stream()
          .filter(team -> team.getMembers().size() < match.getMatchInfo().getMembersPerTeamSize())
          .min(Comparator.comparingInt(value -> value.getMembers().size()));
 }

 public List<Team> getAliveTeams(Match match) {
  return match
          .getTemporally()
          .getTeams()
          .values()
          .stream()
          .filter(team -> getAliveUsers(team) > 0)
          .collect(Collectors.toList());
 }

 private int getAliveUsers(Team team) {
  return (int) team
          .getMembers()
          .stream()
          .filter(user -> user.getTemporally().getRole() == UserState.ALIVE)
          .count();
 }
}