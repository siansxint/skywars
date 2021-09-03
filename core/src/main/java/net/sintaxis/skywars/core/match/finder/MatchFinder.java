package net.sintaxis.skywars.core.match.finder;

import net.sintaxis.skywars.api.cache.ObjectCache;
import net.sintaxis.skywars.core.match.Match;
import net.sintaxis.skywars.core.match.state.MatchState;
import net.sintaxis.skywars.core.match.type.MatchType;

import javax.inject.Inject;

import java.util.Optional;

public class MatchFinder {

 @Inject
 private ObjectCache<Match> matchObjectCache;

 public Optional<Match> findAvailableMatch(MatchType matchType) {
  return matchObjectCache
          .get()
          .values()
          .stream()
          .filter(match -> (match.getTemporally().getState() == MatchState.WAITING || match.getTemporally().getState() == MatchState.STARTING) &&
                  match.getMatchType() == matchType &&
                  match.getTemporally().getCurrentPlayers() < match.getMatchInfo().getMaxTeamsCapacity())
          .findFirst();
 }
}