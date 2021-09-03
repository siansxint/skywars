package net.sintaxis.skywars.core.match.event.types;

import com.fasterxml.jackson.annotation.JsonIgnore;

import net.sintaxis.skywars.core.coordinate.AbbreviatedCoordinates;
import net.sintaxis.skywars.core.match.Match;
import net.sintaxis.skywars.core.match.event.MatchEvent;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class GiantZombieEvent
        implements MatchEvent {

 @JsonIgnore
 private final int defaultTimeUntilNextEvent;
 private int timeUntilNextEvent;

 public GiantZombieEvent(int timeUntilNextEvent) {
  this.timeUntilNextEvent = timeUntilNextEvent;
  this.defaultTimeUntilNextEvent = timeUntilNextEvent;
 }

 @Override
 public int getTimeUntilNextEvent() {
  return timeUntilNextEvent;
 }

 @Override
 public EventType getType() {
  return EventType.GIANT_ZOMBIE;
 }

 @Override
 public void tick() {
  timeUntilNextEvent--;
 }

 @Override
 public void start(Match match) {
  Location spawnLocation = AbbreviatedCoordinates.toLocation(match.getZone().getCenter());
  for (int i = 0; i < 3; i++) {
   spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.GIANT);
  }
 }

 @Override
 public void resetTime() {
  this.timeUntilNextEvent = defaultTimeUntilNextEvent;
 }
}