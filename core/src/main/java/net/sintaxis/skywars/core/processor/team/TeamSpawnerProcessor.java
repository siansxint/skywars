package net.sintaxis.skywars.core.processor.team;

import me.yushust.inject.InjectAll;

import net.sintaxis.skywars.api.processor.Processor;
import net.sintaxis.skywars.core.cage.matcher.CageMatcher;
import net.sintaxis.skywars.core.cage.spawner.CageSpawner;
import net.sintaxis.skywars.core.coordinate.AbbreviatedCoordinates;
import net.sintaxis.skywars.core.match.Match;
import net.sintaxis.skywars.core.team.Team;

import org.bukkit.Location;

@InjectAll
public class TeamSpawnerProcessor
        implements Processor<Match> {

 private CageSpawner cageSpawner;
 private CageMatcher cageMatcher;

 @Override
 public void process(Match match) {

  int index = 0;
  for (Team team : match.getTemporally().getTeams().values()) {

   Location spawnCoordinates = AbbreviatedCoordinates.toLocation(match.getMapInfo().getSpawnsCoordinates().get(index));

   cageSpawner.spawnCage(cageMatcher.findCage(team), spawnCoordinates, 1);

   team.setCoordinates(AbbreviatedCoordinates.fromLocation(spawnCoordinates));

   team.forEach(user -> user.getTemporally().getRaw().ifPresent(player -> player.teleport(spawnCoordinates)));

   index++;
  }
 }
}