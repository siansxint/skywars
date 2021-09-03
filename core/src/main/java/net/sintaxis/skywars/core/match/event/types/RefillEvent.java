package net.sintaxis.skywars.core.match.event.types;

import com.fasterxml.jackson.annotation.JsonIgnore;

import net.sintaxis.skywars.api.cache.ObjectCache;
import net.sintaxis.skywars.core.chest.SkyChest;
import net.sintaxis.skywars.core.chest.filler.ChestFiller;
import net.sintaxis.skywars.core.match.Match;
import net.sintaxis.skywars.core.match.event.MatchEvent;

public class RefillEvent
        implements MatchEvent {

 @JsonIgnore
 private final int defaultTimeUntilNextEvent;
 @JsonIgnore
 private final ChestFiller chestFiller;
 @JsonIgnore
 private final ObjectCache<SkyChest> chestObjectCache;
 private int timeUntilNextEvent;

 public RefillEvent(int timeUntilNextEvent,
                    ChestFiller chestFiller,
                    ObjectCache<SkyChest> chestObjectCache
 ) {
  this.timeUntilNextEvent = timeUntilNextEvent;
  this.defaultTimeUntilNextEvent = timeUntilNextEvent;
  this.chestFiller = chestFiller;
  this.chestObjectCache = chestObjectCache;
 }

 @Override
 public int getTimeUntilNextEvent() {
  return timeUntilNextEvent;
 }

 @Override
 public EventType getType() {
  return EventType.REFILL;
 }

 @Override
 public void tick() {
  timeUntilNextEvent--;
 }

 @Override
 public void start(Match match) {
  // TODO: 12/07/2021 de-hardcode this lol
  SkyChest skyChest = chestObjectCache.find("normal")
          .orElseThrow(() -> new RuntimeException("An error occurred while searching the chest type normal!"));

  match.getChests()
          .forEach(
                  coordinates -> chestFiller.fillChest(
                          coordinates,
                          skyChest
                  )
          );
 }

 @Override
 public void resetTime() {
  this.timeUntilNextEvent = defaultTimeUntilNextEvent;
 }
}