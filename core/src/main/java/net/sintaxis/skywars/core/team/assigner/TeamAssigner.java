package net.sintaxis.skywars.core.team.assigner;

import net.sintaxis.skywars.core.file.YamlFile;
import net.sintaxis.skywars.core.team.Team;

import javax.inject.Inject;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TeamAssigner {

 @Inject
 private YamlFile config;

 public Set<Team> assignMatchTeams(int maxTeams) {
  return IntStream
          .range(0, maxTeams)
          .mapToObj(id -> new Team(
                          config.getString("team.format")
                                  .replace("%team-id%", (id + 1) + "")
                  )
          )
          .collect(Collectors.toSet());
 }
}