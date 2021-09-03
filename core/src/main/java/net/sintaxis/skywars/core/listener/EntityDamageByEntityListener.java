package net.sintaxis.skywars.core.listener;

import me.yushust.message.MessageHandler;

import net.sintaxis.skywars.api.cache.ObjectCache;
import net.sintaxis.skywars.core.SkyWars;
import net.sintaxis.skywars.core.match.Match;
import net.sintaxis.skywars.core.match.state.MatchState;
import net.sintaxis.skywars.core.user.User;
import net.sintaxis.skywars.core.user.state.UserState;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;


import javax.inject.Inject;
import java.util.Optional;

public class EntityDamageByEntityListener
        extends AbstractMatchListener {

 private final SkyWars skyWars;
 private final MessageHandler messageHandler;
 private final ObjectCache<Match> matchObjectCache;

 @Inject
 public EntityDamageByEntityListener(ObjectCache<User> userObjectCache,
                                     ObjectCache<Match> matchObjectCache,
                                     MessageHandler messageHandler,
                                     SkyWars skyWars
 ) {
  super(userObjectCache);
  this.matchObjectCache = matchObjectCache;
  this.messageHandler = messageHandler;
  this.skyWars = skyWars;
 }

 @EventHandler
 public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

  if (!(event.getEntity() instanceof Player)) {
   return;
  }

  Player player = (Player) event.getEntity();

  User user = getUserOrThrowException(player);

  if (user.getTemporally().getRole() == UserState.DEAD) {
   event.setCancelled(true);
   return;
  }

  if (event.getDamager() instanceof Projectile) {
   Projectile projectile = ((Projectile) event.getDamager());

   if (projectile.getType() != EntityType.ARROW) {
    return;
   }

   if (!(projectile.getShooter() instanceof Player)) {
    return;
   }

   Bukkit.getScheduler().runTaskLater(skyWars, () -> messageHandler.sendReplacing(
           projectile.getShooter(),
           "user-events.arrow-message",
           "%target-name%", player.getName(), "%target-health%", Math.round(player.getHealth())), 1);
   return;
  }

  if (!(event.getDamager() instanceof Player)) {
   return;
  }

  User damager = getUserOrThrowException((Player) event.getDamager());

  Optional<String> userMatchName = user.getTemporally().getMatchName();
  Optional<String> damagerMatchName = user.getTemporally().getMatchName();

  if (!(userMatchName.isPresent() &&
          damagerMatchName.isPresent())) {
   return;
  }

  if (!damagerMatchName.get().equalsIgnoreCase(userMatchName.get())) {
   return;
  }

  Optional<String> userTeamName = user.getTemporally().getTeamName();
  Optional<String> damagerTeamName = damager.getTemporally().getTeamName();

  if (!(damagerTeamName.isPresent() &&
          userTeamName.isPresent())) {
   return;
  }

  if (damagerTeamName.get().equalsIgnoreCase(userTeamName.get()) ||
          damager.getTemporally().getRole() == UserState.DEAD) {
   event.setCancelled(true);
   return;
  }

  matchObjectCache.find(userMatchName.get())
          .ifPresent(match -> event.setCancelled(requireCancel(match)));
 }

 @Override
 protected boolean requireCancel(Match match) {
  return match.getTemporally().getState() != MatchState.IN_GAME;
 }
}