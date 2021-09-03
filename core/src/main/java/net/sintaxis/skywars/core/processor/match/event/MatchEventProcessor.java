package net.sintaxis.skywars.core.processor.match.event;

import me.yushust.message.MessageHandler;

import net.sintaxis.skywars.api.processor.Processor;
import net.sintaxis.skywars.core.match.Match;
import net.sintaxis.skywars.core.match.event.MatchEvent;

import javax.inject.Inject;

public class MatchEventProcessor
        implements Processor<Match> {

 @Inject
 private MessageHandler messageHandler;

 @Override
 public void process(Match match) {

  for (MatchEvent matchEvent : match.getEvents()) {

   if (matchEvent.getTimeUntilNextEvent() < 1) {
    continue;
   }

   matchEvent.tick();

   if (matchEvent.getTimeUntilNextEvent() == 0) {
    matchEvent.start(match);
    messageHandler.send(match.getTemporally().getTeams().values(),
            "event-type." + matchEvent.getType().toString().toLowerCase());
   }
  }
 }
}