package net.sintaxis.skywars.core.listener;

import net.sintaxis.skywars.api.cache.ObjectCache;
import net.sintaxis.skywars.core.match.Match;
import net.sintaxis.skywars.core.match.state.MatchState;
import net.sintaxis.skywars.core.user.User;
import net.sintaxis.skywars.core.user.state.UserState;

import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import javax.inject.Inject;

public class BlockBreakListener
        extends AbstractMatchListener {

 private final ObjectCache<Match> matchObjectCache;

 @Inject
 public BlockBreakListener(ObjectCache<User> userObjectCache,
                           ObjectCache<Match> matchObjectCache
 ) {
  super(userObjectCache);
  this.matchObjectCache = matchObjectCache;
 }

 @EventHandler
 public void onBlockBreak(BlockBreakEvent event) {

  User.Temporally temporally = getUserOrThrowException(
          event.getPlayer()
  ).getTemporally();

  temporally.getMatchName()
          .flatMap(matchObjectCache::find)
          .ifPresent(match -> {
           if (requireCancel(match) || temporally.getRole() == UserState.DEAD) {
            event.setCancelled(true);
           }
          });
 }

 @Override
 protected boolean requireCancel(Match match) {
  MatchState state = match.getTemporally().getState();
  return state == MatchState.WAITING ||
          state == MatchState.STARTING ||
          state == MatchState.ENDING;
 }
}