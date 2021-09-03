package net.sintaxis.skywars.core.processor.match.restart;

import me.yushust.inject.InjectAll;

import net.sintaxis.skywars.api.processor.Processor;
import net.sintaxis.skywars.core.match.Match;
import net.sintaxis.skywars.core.match.event.MatchEvent;
import net.sintaxis.skywars.core.world.WorldHandler;

@InjectAll
public class MatchRestartProcessor
        implements Processor<Match> {

 private WorldHandler worldHandler;
 private Processor<Match> matchProcessor;

 @Override
 public void process(Match match) {

  Match.Temporally temporally = match.getTemporally();
  temporally.getTeams().clear();
  temporally.setCurrentPlayers(0);

  match.getEvents().forEach(MatchEvent::resetTime);

  worldHandler.rollbackMatch(match);

  matchProcessor.process(match);
 }
}