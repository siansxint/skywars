package net.sintaxis.skywars.core.listener;

import net.sintaxis.skywars.api.cache.ObjectCache;
import net.sintaxis.skywars.core.match.Match;
import net.sintaxis.skywars.core.match.state.MatchState;
import net.sintaxis.skywars.core.user.User;
import net.sintaxis.skywars.core.user.state.UserState;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

import javax.inject.Inject;

public class EntityDamageListener
        extends AbstractMatchListener {

 private final ObjectCache<Match> matchObjectCache;

 @Inject
 public EntityDamageListener(ObjectCache<User> userObjectCache,
                             ObjectCache<Match> matchObjectCache
 ) {
  super(userObjectCache);
  this.matchObjectCache = matchObjectCache;
 }

 @EventHandler
 public void onEntityDamage(EntityDamageEvent event) {

  if (!(event.getEntity() instanceof Player)) {
   return;
  }

  User.Temporally temporally = getUserOrThrowException((Player) event.getEntity())
          .getTemporally();

  temporally
          .getMatchName()
          .flatMap(matchObjectCache::find)
          .ifPresent(match -> {
           if (requireCancel(match) || temporally.getRole() == UserState.DEAD) {
            event.setDamage(0);
            event.setCancelled(true);
            return;
           }

           if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
            event.setDamage(1000);
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