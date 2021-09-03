package net.sintaxis.skywars.core.logic.distributor;

import me.yushust.inject.InjectAll;

import net.sintaxis.skywars.api.cache.ObjectCache;
import net.sintaxis.skywars.core.logic.worker.Worker;
import net.sintaxis.skywars.core.logic.worker.provider.WorkerProvider;
import net.sintaxis.skywars.core.match.Match;

import javax.inject.Named;

import java.util.Map;
import java.util.Optional;

@InjectAll
public class CountdownMatchDistributor
        implements Runnable {

 @Named("match-distributor-cache")
 private ObjectCache<Match> matchDistributorObjectCache;
 private WorkerProvider workerProvider;

 @Override
 public void run() {

  Map<String, Match> matches = matchDistributorObjectCache.get();

  if (matches.size() == 0) {
   return;
  }

  for (Match match : matches.values()) {

   Optional<Worker> optionalWorker = workerProvider.getWorkerForType(match.getTemporally().getState());

   if (!optionalWorker.isPresent()) {
    matchDistributorObjectCache.remove(match.getId());
    continue;
   }

   optionalWorker.get().work(match);
  }
 }
}