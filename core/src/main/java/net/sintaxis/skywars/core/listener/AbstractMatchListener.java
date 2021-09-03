package net.sintaxis.skywars.core.listener;

import net.sintaxis.skywars.api.cache.ObjectCache;
import net.sintaxis.skywars.core.match.Match;
import net.sintaxis.skywars.core.user.User;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public abstract class AbstractMatchListener
        implements Listener {

 private final ObjectCache<User> userObjectCache;

 public AbstractMatchListener(ObjectCache<User> userObjectCache) {
  this.userObjectCache = userObjectCache;
 }

 public User getUserOrThrowException(Player player) {
  return userObjectCache.find(player.getUniqueId().toString())
          .orElseThrow(() ->
                  new RuntimeException(
                          "An error occurred while obtaining the information of the user " + player.getName())
          );
 }

 protected boolean requireCancel(Match match) {
  return false;
 }
}