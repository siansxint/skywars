package net.sintaxis.skywars.core.logic.worker.types;

import me.yushust.inject.InjectAll;

import net.sintaxis.skywars.api.processor.Processor;
import net.sintaxis.skywars.core.event.MatchLeaveEvent;
import net.sintaxis.skywars.core.logic.worker.Worker;
import net.sintaxis.skywars.core.match.Match;
import net.sintaxis.skywars.core.user.finder.UserFinder;

import org.bukkit.Bukkit;

import javax.inject.Named;

@InjectAll
public class EndingStateMatchWorker
        implements Worker {

 @Named("match-restart-processor")
 private Processor<Match> matchRestartProcessor;
 private UserFinder userFinder;

 @Override
 public void work(Match match) {
  userFinder.getMatchUsers(match)
          .forEach(user -> user.getTemporally().getRaw()
                  .ifPresent(player ->
                          Bukkit.getPluginManager().callEvent(
                                  new MatchLeaveEvent(user, MatchLeaveEvent.Cause.END_OF_MATCH)
                          )
                  )
          );
  matchRestartProcessor.process(match);
 }
}