package net.sintaxis.skywars.core.match.info.map;

import net.sintaxis.skywars.core.coordinate.AbbreviatedCoordinates;

import java.util.ArrayList;
import java.util.List;

public class MapInfo {

 private AbbreviatedCoordinates lobbyCoordinates;
 private AbbreviatedCoordinates spectatorCoordinates;

 private final List<AbbreviatedCoordinates> spawnsCoordinates
         = new ArrayList<>();

 public AbbreviatedCoordinates getLobbyCoordinates() {
  return lobbyCoordinates;
 }

 public void setLobbyCoordinates(AbbreviatedCoordinates lobbyCoordinates) {
  this.lobbyCoordinates = lobbyCoordinates;
 }

 public AbbreviatedCoordinates getSpectatorCoordinates() {
  return spectatorCoordinates;
 }

 public void setSpectatorCoordinates(AbbreviatedCoordinates spectatorCoordinates) {
  this.spectatorCoordinates = spectatorCoordinates;
 }

 public List<AbbreviatedCoordinates> getSpawnsCoordinates() {
  return spawnsCoordinates;
 }
}