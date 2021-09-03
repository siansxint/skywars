package net.sintaxis.skywars.core.scoreboard.registry;

import net.sintaxis.skywars.core.scoreboard.Scoreboard;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ScoreboardRegistry {

 private final Map<String, Scoreboard> scoreboardRegistry
         = new ConcurrentHashMap<>();

 public void addToRegistry(String ownerId, Scoreboard scoreboard) {
  removeFromRegistry(ownerId);
  scoreboardRegistry.put(ownerId, scoreboard);
 }

 public void removeFromRegistry(String ownerId) {
  scoreboardRegistry.remove(ownerId);
 }

 public Optional<Scoreboard> getScoreboardByOwner(String ownerId) {
  return Optional.ofNullable(scoreboardRegistry.get(ownerId));
 }

 public void updateAll() {
  for (Scoreboard scoreboard : scoreboardRegistry.values()) {
   scoreboard.run();
  }
 }
}