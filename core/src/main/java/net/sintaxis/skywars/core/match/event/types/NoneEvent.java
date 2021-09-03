package net.sintaxis.skywars.core.match.event.types;

import net.sintaxis.skywars.core.match.Match;
import net.sintaxis.skywars.core.match.event.MatchEvent;

public class NoneEvent
        implements MatchEvent {

 @Override
 public int getTimeUntilNextEvent() {
  return 0;
 }

 @Override
 public EventType getType() {
  return EventType.NONE;
 }

 @Override
 public void tick() {}

 @Override
 public void start(Match match) {}

 @Override
 public void resetTime() {}
}