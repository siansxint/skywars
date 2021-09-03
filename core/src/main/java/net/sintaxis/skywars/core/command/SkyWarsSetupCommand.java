package net.sintaxis.skywars.core.command;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.Named;
import me.fixeddev.commandflow.bukkit.annotation.Sender;

import me.yushust.inject.InjectAll;
import me.yushust.message.MessageHandler;

import net.sintaxis.skywars.api.cache.ObjectCache;
import net.sintaxis.skywars.api.processor.Processor;
import net.sintaxis.skywars.api.storage.ObjectStorage;
import net.sintaxis.skywars.core.chest.scanner.ChestScanner;
import net.sintaxis.skywars.core.coordinate.AbbreviatedCoordinates;
import net.sintaxis.skywars.core.file.YamlFile;
import net.sintaxis.skywars.core.cuboid.Cuboid;
import net.sintaxis.skywars.core.match.Match;
import net.sintaxis.skywars.core.match.info.map.MapInfo;
import net.sintaxis.skywars.core.match.info.match.MatchInfo;
import net.sintaxis.skywars.core.match.info.timing.TimedMatchInfo;
import net.sintaxis.skywars.core.match.type.MatchType;
import net.sintaxis.skywars.core.world.WorldHandler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Command(names = "setup")
@InjectAll
public class SkyWarsSetupCommand
        implements CommandClass {

 private ObjectCache<Match> matchObjectCache;
 private MessageHandler messageHandler;
 private WorldHandler worldHandler;
 private ChestScanner chestScanner;
 private Processor<Match> matchProcessor;
 private ObjectStorage<Match> matchObjectStorage;
 private WorldEditPlugin worldEditPlugin;
 private YamlFile config;

 @Command(names = "teleport", permission = "skywars.setup.teleport")
 public void teleportCommand(@Sender Player player,
                             @Named("world-name") String worldName
 ) {
  player.teleport(worldHandler.retrieveOrCreateEmptyWorld(worldName).getSpawnLocation());
  messageHandler.sendReplacing(
          player,
          "match-setup.teleported",
          "%world-name%",
          worldName
  );
 }

 @Command(names = "create", permission = "skywars.setup.create")
 public void setupCommand(@Sender Player player,
                          @Named("match-type") MatchType matchType,
                          @Named("match-name") String matchName,
                          @Named("min-players") int minPlayers,
                          @Named("max-players") int maxPlayers,
                          @Named("players-per-team") int playersPerTeam
 ) {
  Selection selection = worldEditPlugin.getSelection(player);

  if (selection == null) {
   player.sendMessage("You need select the zone of the match!");
   return;
  }

  Match match = new Match(
          matchName,
          matchType,
          new TimedMatchInfo(
                  config.getInt("match.waiting-time"),
                  config.getInt("match.starting-time")
          ),
          new MatchInfo(
                  minPlayers,
                  maxPlayers,
                  playersPerTeam
          ),
          new MapInfo(),
          new Cuboid(
                  AbbreviatedCoordinates.fromLocation(selection.getMinimumPoint()),
                  AbbreviatedCoordinates.fromLocation(selection.getMaximumPoint())
          )
  );

  player.teleport(Bukkit.getWorld(
          config.getString("lobby-world-name")
  ).getSpawnLocation());

  player.teleport(worldHandler.retrieveOrCreateEmptyWorld(match)
          .getSpawnLocation()
  );

  messageHandler.sendReplacing(
          player,
          "match-setup.created",
          "%match-name%", matchName);

  matchObjectCache.add(matchName, match);
 }

 @Command(names = "lobby-location")
 public void setLobbyLocationCommand(@Sender Player player,
                                     @Named("match-name") Match match
 ) {
  match.getMapInfo().setLobbyCoordinates(
          AbbreviatedCoordinates.fromLocation(player.getLocation())
  );

  messageHandler.sendReplacing(
          player,
          "match-setup.lobby-coordinates",
          "%match-name%", match.getId(),
          "%position-x%", player.getLocation().getX(),
          "%position-y%", player.getLocation().getY(),
          "%position-z%", player.getLocation().getZ()
  );
 }

 @Command(names = "spectator-location")
 public void setSpectatorLocationCommand(@Sender Player player,
                                         @Named("match-name") Match match
 ) {
  match.getMapInfo().setSpectatorCoordinates(
          AbbreviatedCoordinates.fromLocation(player.getLocation())
  );

  messageHandler.sendReplacing(
          player,
          "match-setup.spectator-coordinates",
          "%match-name%", match.getId(),
          "%position-x%", player.getLocation().getX(),
          "%position-y%", player.getLocation().getY(),
          "%position-z%", player.getLocation().getZ()
  );
 }

 @Command(names = "spawn-location")
 public void addSpawnLocation(@Sender Player player,
                              @Named("match-name") Match match
 ) {
  if (match.getMapInfo().getSpawnsCoordinates().size() >= match.getMatchInfo().getMaxTeamsCapacity()) {
   messageHandler.sendReplacing(
           player,
           "match-setup.max-spawns",
           "%spawns-limit%", match.getMatchInfo().getMaxTeamsCapacity()
   );
   return;
  }

  match.getMapInfo().getSpawnsCoordinates()
          .add(AbbreviatedCoordinates.fromLocation(player.getLocation()));

  messageHandler.sendReplacing(
          player,
          "match-setup.spawn-coordinates",
          "%match-name%", match.getId(),
          "%spawn-number%", match.getMapInfo().getSpawnsCoordinates().size(),
          "%position-x%", player.getLocation().getX(),
          "%position-y%", player.getLocation().getY(),
          "%position-z%", player.getLocation().getZ()
  );
 }

 @Command(names = "scan-chests")
 public void scanChestsCommand(@Sender Player player,
                               @Named("match-name") Match match
 ) {
  if (match.getZone() == null) {

   messageHandler.sendReplacing(
           player,
           "match-setup.null-zone",
           "%match-name%", match.getId()
   );
   return;
  }

  chestScanner.scanAndSetupMatchChests(match);

  messageHandler.sendReplacing(
          player,
          "match-setup.scanned-chests",
          "%match-name%", match.getId(),
          "%scanned-chests%", match.getChests().size()
  );
 }

 @Command(names = "save")
 public void saveMatchCommand(@Sender Player player,
                              @Named("match-name") Match match
 ) {

  matchProcessor.process(match);

  worldHandler.createBackup(match);

  messageHandler.sendReplacing(
          player,
          "match-setup.saving",
          "%match-name%", match.getId()
  );

  matchObjectStorage
          .save(match)
          .whenComplete((unused, throwable) -> messageHandler.sendReplacing(
                  player,
                  "match-setup.saved",
                  "%match-name%", match.getId()));
 }
}