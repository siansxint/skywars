package net.sintaxis.skywars.core.logic.worker.provider;

import net.sintaxis.skywars.core.logic.worker.Worker;
import net.sintaxis.skywars.core.match.state.MatchState;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class WorkerProvider {

 private final Map<MatchState, Worker> workersContainer
         = new ConcurrentHashMap<>();

 public Optional<Worker> getWorkerForType(MatchState matchState) {
  return Optional.ofNullable(workersContainer.get(matchState));
 }

 public void addWorker(MatchState matchState, Worker worker) {
  workersContainer.put(matchState, worker);
 }
}