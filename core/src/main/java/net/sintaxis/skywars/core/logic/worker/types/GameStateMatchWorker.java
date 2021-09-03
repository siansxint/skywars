package net.sintaxis.skywars.core.logic.worker.types;

import net.sintaxis.skywars.api.processor.Processor;
import net.sintaxis.skywars.core.event.MatchEndEvent;
import net.sintaxis.skywars.core.logic.worker.Worker;
import net.sintaxis.skywars.core.match.Match;

import org.bukkit.Bukkit;

import javax.inject.Inject;
import javax.inject.Named;

public class GameStateMatchWorker
        implements Worker {

 @Inject
 @Named("match-event-processor")
 private Processor<Match> matchEventProcessor;

 @Override
 public void work(Match match) {

  MatchEndEvent matchEndEvent = new MatchEndEvent(match);
  Bukkit.getPluginManager().callEvent(matchEndEvent);

  if (!matchEndEvent.isCancelled()) {
   return;
  }

  matchEventProcessor.process(match);
 }
}