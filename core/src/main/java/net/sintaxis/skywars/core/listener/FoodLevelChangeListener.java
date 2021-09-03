package net.sintaxis.skywars.core.listener;

import net.sintaxis.skywars.api.cache.ObjectCache;
import net.sintaxis.skywars.core.match.Match;
import net.sintaxis.skywars.core.match.state.MatchState;
import net.sintaxis.skywars.core.user.User;
import net.sintaxis.skywars.core.user.state.UserState;

import org.bukkit.entity.Player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import javax.inject.Inject;

public class FoodLevelChangeListener
        extends AbstractMatchListener {

 private final ObjectCache<Match> matchObjectCache;

 @Inject
 public FoodLevelChangeListener(ObjectCache<User> userObjectCache,
                                ObjectCache<Match> matchObjectCache
 ) {
  super(userObjectCache);
  this.matchObjectCache = matchObjectCache;
 }

 @EventHandler
 public void onFoodLevelChange(FoodLevelChangeEvent event) {
  if (!(event.getEntity() instanceof Player)) {
   return;
  }

  User.Temporally temporally = getUserOrThrowException((Player) event.getEntity())
          .getTemporally();

  temporally.getMatchName()
          .flatMap(matchObjectCache::find)
          .ifPresent(match -> event.setCancelled(requireCancel(match) || temporally.getRole() == UserState.DEAD));
 }

 @Override
 protected boolean requireCancel(Match match) {
  MatchState state = match.getTemporally().getState();
  return state == MatchState.WAITING ||
          state == MatchState.STARTING ||
          state == MatchState.ENDING;
 }
}