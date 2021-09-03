package net.sintaxis.skywars.core.match;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import net.sintaxis.skywars.api.model.Savable;
import net.sintaxis.skywars.core.coordinate.AbbreviatedCoordinates;
import net.sintaxis.skywars.core.cuboid.Cuboid;
import net.sintaxis.skywars.core.match.info.map.MapInfo;
import net.sintaxis.skywars.core.match.info.match.MatchInfo;
import net.sintaxis.skywars.core.match.info.timing.TimedMatchInfo;
import net.sintaxis.skywars.core.match.event.MatchEvent;
import net.sintaxis.skywars.core.match.state.MatchState;
import net.sintaxis.skywars.core.match.type.MatchType;
import net.sintaxis.skywars.core.team.Team;

import java.beans.ConstructorProperties;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Match
        implements Savable {

 @JsonProperty("_id")
 private final String name;

 private final MatchType matchType;
 private final Set<AbbreviatedCoordinates> chests;
 private final List<MatchEvent> events;

 private final TimedMatchInfo timedMatchInfo;
 private final MatchInfo matchInfo;
 private final MapInfo mapInfo;

 private final Cuboid zone;

 @JsonIgnore
 private final Match.Temporally temporally;

 @ConstructorProperties({
         "name",
         "matchType",
         "timedMatchConfiguration",
         "matchConfiguration",
         "mapConfiguration",
         "zone"
 })
 public Match(String name,
              MatchType matchType,
              TimedMatchInfo timedMatchInfo,
              MatchInfo matchInfo,
              MapInfo mapInfo,
              Cuboid cuboidZone
 ) {
  this.name = name;
  this.matchType = matchType;
  this.chests = new HashSet<>();
  this.events = new LinkedList<>();
  this.timedMatchInfo = timedMatchInfo;
  this.matchInfo = matchInfo;
  this.mapInfo = mapInfo;
  this.zone = cuboidZone;
  this.temporally = new Temporally();
 }

 @Override
 public String getId() {
  return name;
 }

 public MatchType getMatchType() {
  return matchType;
 }

 public Set<AbbreviatedCoordinates> getChests() {
  return chests;
 }

 public List<MatchEvent> getEvents() {
  return events;
 }

 public TimedMatchInfo getTimedMatchInfo() {
  return timedMatchInfo;
 }

 public MatchInfo getMatchInfo() {
  return matchInfo;
 }

 public MapInfo getMapInfo() {
  return mapInfo;
 }

 public Cuboid getZone() {
  return zone;
 }

 public Match.Temporally getTemporally() {
  return temporally;
 }

 public static class Temporally {

  private final Map<String, Team> teams
          = new ConcurrentHashMap<>();
  private MatchState state;
  private int currentPlayers = 0;

  public MatchState getState() {
   return state;
  }

  public void setState(MatchState matchState) {
   this.state = matchState;
  }

  public Map<String, Team> getTeams() {
   return teams;
  }

  public int getCurrentPlayers() {
   return currentPlayers;
  }

  public void setCurrentPlayers(int currentPlayers) {
   this.currentPlayers = currentPlayers;
  }
 }
}