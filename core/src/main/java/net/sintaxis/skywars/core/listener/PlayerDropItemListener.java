package net.sintaxis.skywars.core.listener;

import net.sintaxis.skywars.api.cache.ObjectCache;
import net.sintaxis.skywars.core.match.Match;
import net.sintaxis.skywars.core.match.state.MatchState;
import net.sintaxis.skywars.core.user.User;
import net.sintaxis.skywars.core.user.state.UserState;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;

import javax.inject.Inject;

public class PlayerDropItemListener
        extends AbstractMatchListener {

 private final ObjectCache<Match> matchObjectCache;

 @Inject
 public PlayerDropItemListener(ObjectCache<User> userObjectCache,
                               ObjectCache<Match> matchObjectCache
 ) {
  super(userObjectCache);
  this.matchObjectCache = matchObjectCache;
 }

 @EventHandler
 public void onPlayerItemDrop(PlayerDropItemEvent event) {
  User.Temporally temporally = getUserOrThrowException(event.getPlayer())
          .getTemporally();

  temporally.getMatchName()
          .flatMap(matchObjectCache::find)
          .ifPresent(match -> event.setCancelled(requireCancel(match) || temporally.getRole() == UserState.DEAD));
 }

 @Override
 protected boolean requireCancel(Match match) {
  return match.getTemporally().getState() == MatchState.WAITING ||
          match.getTemporally().getState() == MatchState.STARTING;
 }
}