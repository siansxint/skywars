package net.sintaxis.skywars.core.match.event.types;

import com.fasterxml.jackson.annotation.JsonIgnore;

import net.sintaxis.skywars.core.coordinate.AbbreviatedCoordinates;
import net.sintaxis.skywars.core.match.Match;
import net.sintaxis.skywars.core.match.event.MatchEvent;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class WitherEvent
        implements MatchEvent {

 @JsonIgnore
 private final int defaultTimeUntilNextEvent;
 private int timeUntilNextEvent;

 public WitherEvent(int timeUntilNextEvent) {
  this.timeUntilNextEvent = timeUntilNextEvent;
  this.defaultTimeUntilNextEvent = timeUntilNextEvent;
 }

 @Override
 public int getTimeUntilNextEvent() {
  return timeUntilNextEvent;
 }

 @Override
 public EventType getType() {
  return EventType.WITHER;
 }

 @Override
 public void tick() {
  timeUntilNextEvent--;
 }

 @Override
 public void start(Match match) {
  Location spawnLocation = AbbreviatedCoordinates.toLocation(match.getZone().getCenter());
  spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.WITHER);
 }

 @Override
 public void resetTime() {
  this.timeUntilNextEvent = defaultTimeUntilNextEvent;
 }
}