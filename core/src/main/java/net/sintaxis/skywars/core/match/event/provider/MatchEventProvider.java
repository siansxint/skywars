package net.sintaxis.skywars.core.match.event.provider;

import net.sintaxis.skywars.api.cache.ObjectCache;
import net.sintaxis.skywars.core.SkyWars;
import net.sintaxis.skywars.core.chest.SkyChest;
import net.sintaxis.skywars.core.chest.filler.ChestFiller;
import net.sintaxis.skywars.core.match.event.MatchEvent;
import net.sintaxis.skywars.core.match.event.types.*;
import net.sintaxis.skywars.core.user.finder.UserFinder;

public class MatchEventProvider {

 private final SkyWars skyWars;

 private final ChestFiller chestFiller
         = new ChestFiller();
 private final UserFinder userFinder
         = new UserFinder();

 private final ObjectCache<SkyChest> chestObjectCache;

 public MatchEventProvider(SkyWars skyWars,
                           ObjectCache<SkyChest> chestObjectCache
 ) {
  this.skyWars = skyWars;
  this.chestObjectCache = chestObjectCache;
 }

 public MatchEvent provide(MatchEvent.EventType eventType, int timeUntilNextEvent) {

  switch (eventType) {
   case REFILL:
    return new RefillEvent(timeUntilNextEvent, chestFiller, chestObjectCache);
   case WITHER:
    return new WitherEvent(timeUntilNextEvent);
   case DRAGON:
    return new DragonEvent(timeUntilNextEvent);
   case BORDER:
    return new BorderEvent(timeUntilNextEvent);
   case TNT_RAIN:
    return new TNTRainEvent(timeUntilNextEvent, userFinder, skyWars);
   case GIANT_ZOMBIE:
    return new GiantZombieEvent(timeUntilNextEvent);
   default:
    throw new IllegalArgumentException("What happen here? The event type " + eventType.name() + " was not found!");
  }
 }
}