package net.sintaxis.skywars.core.service;

import me.yushust.inject.InjectAll;

import net.sintaxis.skywars.api.model.Service;
import net.sintaxis.skywars.core.logic.worker.provider.WorkerProvider;
import net.sintaxis.skywars.core.logic.worker.types.EndingStateMatchWorker;
import net.sintaxis.skywars.core.logic.worker.types.GameStateMatchWorker;
import net.sintaxis.skywars.core.logic.worker.types.StartingStateMatchWorker;
import net.sintaxis.skywars.core.logic.worker.types.WaitingStateMatchWorker;
import net.sintaxis.skywars.core.match.state.MatchState;

@InjectAll
public class WorkersService
        implements Service {

 private WorkerProvider workerProvider;
 private WaitingStateMatchWorker waitingStateMatchWorker;
 private StartingStateMatchWorker startingStateMatchWorker;
 private GameStateMatchWorker gameStateMatchWorker;
 private EndingStateMatchWorker endingStateMatchWorker;

 @Override
 public void start() {
  workerProvider.addWorker(MatchState.WAITING, waitingStateMatchWorker);
  workerProvider.addWorker(MatchState.STARTING, startingStateMatchWorker);
  workerProvider.addWorker(MatchState.IN_GAME, gameStateMatchWorker);
  workerProvider.addWorker(MatchState.ENDING, endingStateMatchWorker);
 }
}