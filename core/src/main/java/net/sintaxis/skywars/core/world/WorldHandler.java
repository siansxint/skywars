package net.sintaxis.skywars.core.world;

import net.sintaxis.skywars.core.match.Match;

import org.bukkit.World;

public interface WorldHandler {

 /**
  * The prefix used to find/save/load the match world
  */

 String MATCH_WORLD_PREFIX = "match_";

 /**
  * An util method to retrieve a world, if not exists, an empty world will be created
  *
  * @param match The match to request their name (Yes, request their name if better, but in this case I only need the name of an specific object, so the match)
  * @return The retrieve, or the creation of an empty world
  */

 default World retrieveOrCreateEmptyWorld(Match match) {
  return retrieveOrCreateEmptyWorld(match.getId());
 }

 World retrieveOrCreateEmptyWorld(String worldName);

 /**
  * used to create a backup of this world
  *
  * @param match The match to back up their name
  */

 default void createBackup(Match match) {
  createBackup(match.getId());
 }

 void createBackup(String worldName);

 /**
  * @param match The match to rollback
  */

 void rollbackMatch(Match match);
}