package net.sintaxis.skywars.core.match.event;

import net.sintaxis.skywars.core.match.Match;

public interface MatchEvent {

 /**
  * Used to get the time until the next event will be called, and when this event start
  *
  * @return The time until the next event
  */

 int getTimeUntilNextEvent();

 /**
  * Used to get the type of event, and more specific their name, see more in {@link EventType}
  *
  * @return The type of event
  */

 EventType getType();

 /**
  * Called when the worker of the current match with this event need subtract to the current time
  * until the next phase
  */

 void tick();

 /**
  * Called when the time until the next phase was already passed and this event need to be executed
  *
  * @param match The match to execute this event
  */

 void start(Match match);

 void resetTime();

 enum EventType {

  REFILL("event-type.refill"),
  DRAGON("event-type.dragon"),
  WITHER("event-type.wither"),
  BORDER("event-type.border"),
  TNT_RAIN("event-type.tnt-rain"),
  GIANT_ZOMBIE("event-type.giant-zombie"),
  NONE("event-type.none");

  private final String namePath;

  EventType(String namePath) {
   this.namePath = namePath;
  }

  public String getNamePath() {
   return namePath;
  }
 }
}