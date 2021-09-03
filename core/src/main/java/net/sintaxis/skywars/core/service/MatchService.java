package net.sintaxis.skywars.core.service;

import me.yushust.inject.InjectAll;

import net.sintaxis.skywars.api.cache.ObjectCache;
import net.sintaxis.skywars.api.model.Service;
import net.sintaxis.skywars.api.processor.Processor;
import net.sintaxis.skywars.api.storage.ObjectStorage;
import net.sintaxis.skywars.core.match.Match;
import net.sintaxis.skywars.core.world.WorldHandler;

import java.util.Set;

@InjectAll
public class MatchService
        implements Service {

 private ObjectStorage<Match> matchObjectStorage;
 private ObjectCache<Match> matchObjectCache;
 private Processor<Match> matchProcessor;
 private WorldHandler worldHandler;

 @Override
 public void start() {
  Set<Match> results = matchObjectStorage.getAllSync();

  if (results.size() == 0) {
   return;
  }

  for (Match match : results) {
   matchProcessor.process(match);
   worldHandler.rollbackMatch(match);
   matchObjectCache.add(match.getId(), match);
  }
 }
}