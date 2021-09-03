package net.sintaxis.skywars.core.match.event.types;

import com.fasterxml.jackson.annotation.JsonIgnore;

import net.sintaxis.skywars.core.coordinate.AbbreviatedCoordinates;
import net.sintaxis.skywars.core.match.Match;
import net.sintaxis.skywars.core.match.event.MatchEvent;

import org.bukkit.Location;
import org.bukkit.WorldBorder;

public class BorderEvent
        implements MatchEvent {

 @JsonIgnore
 private final int defaultTimeUntilNextEvent;
 private int timeUntilNextEvent;

 public BorderEvent(int timeUntilNextEvent) {
  this.timeUntilNextEvent = timeUntilNextEvent;
  this.defaultTimeUntilNextEvent = timeUntilNextEvent;
 }

 @Override
 public int getTimeUntilNextEvent() {
  return timeUntilNextEvent;
 }

 @Override
 public EventType getType() {
  return EventType.BORDER;
 }

 @Override
 public void tick() {
  timeUntilNextEvent--;
 }

 @Override
 public void start(Match match) {

  Location centerLocation = AbbreviatedCoordinates.toLocation(match.getZone().getCenter());

  double size = Math.max(match.getZone().getSizeX(), match.getZone().getSizeZ());
  int startSize = (int) (100 * size / 100.0);

  WorldBorder worldBorder = centerLocation.getWorld().getWorldBorder();
  worldBorder.setCenter(centerLocation);
  worldBorder.setSize(startSize);
  worldBorder.setWarningDistance(10);
  worldBorder.setWarningTime(15);
  worldBorder.setDamageAmount(2);

  int endSize = Math.max((int) (30 * size / 100.0), 2);
  worldBorder.setSize(endSize, 120);
 }

 @Override
 public void resetTime() {
  this.timeUntilNextEvent = defaultTimeUntilNextEvent;
 }
}