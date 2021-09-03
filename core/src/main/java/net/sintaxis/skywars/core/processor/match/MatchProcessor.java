package net.sintaxis.skywars.core.processor.match;

import net.sintaxis.skywars.api.processor.Processor;
import net.sintaxis.skywars.core.match.Match;
import net.sintaxis.skywars.core.match.state.MatchState;
import net.sintaxis.skywars.core.team.assigner.TeamAssigner;

import javax.inject.Inject;

public class MatchProcessor
        implements Processor<Match> {

 @Inject
 private TeamAssigner teamAssigner;

 @Override
 public void process(Match match) {
  teamAssigner.assignMatchTeams(match.getMatchInfo().getMaxTeamsCapacity())
          .forEach(team -> match.getTemporally().getTeams().put(team.getName(), team));

  match.getTemporally().setState(MatchState.WAITING);
 }
}