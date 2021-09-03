package net.sintaxis.skywars.core.processor;

import me.yushust.inject.AbstractModule;
import me.yushust.inject.key.TypeReference;

import net.sintaxis.skywars.api.processor.Processor;
import net.sintaxis.skywars.core.match.Match;
import net.sintaxis.skywars.core.processor.match.MatchProcessor;
import net.sintaxis.skywars.core.processor.match.event.MatchEventProcessor;
import net.sintaxis.skywars.core.processor.match.restart.MatchRestartProcessor;
import net.sintaxis.skywars.core.processor.team.TeamSpawnerProcessor;

public class ProcessorModule
        extends AbstractModule {

 @Override
 protected void configure() {
  bind(new TypeReference<Processor<Match>>() {})
          .to(MatchProcessor.class)
          .singleton();

  bind(new TypeReference<Processor<Match>>() {})
          .named("team-spawner")
          .to(TeamSpawnerProcessor.class)
          .singleton();

  bind(new TypeReference<Processor<Match>>() {})
          .named("match-event-processor")
          .to(MatchEventProcessor.class)
          .singleton();

  bind(new TypeReference<Processor<Match>>() {})
          .named("match-restart-processor")
          .to(MatchRestartProcessor.class)
          .singleton();
 }
}