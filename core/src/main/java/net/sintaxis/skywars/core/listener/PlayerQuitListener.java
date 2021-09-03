package net.sintaxis.skywars.core.listener;

import me.yushust.inject.InjectAll;

import net.sintaxis.skywars.api.cache.ObjectCache;
import net.sintaxis.skywars.api.storage.ObjectStorage;
import net.sintaxis.skywars.core.event.MatchLeaveEvent;
import net.sintaxis.skywars.core.scoreboard.registry.ScoreboardRegistry;
import net.sintaxis.skywars.core.user.User;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;

@InjectAll
public class PlayerQuitListener
        implements Listener {

 private ObjectCache<User> userObjectCache;
 private ObjectStorage<User> userObjectStorage;
 private ScoreboardRegistry scoreboardRegistry;

 @EventHandler
 public void onPlayerQuit(PlayerQuitEvent event) {

  String id = event.getPlayer()
          .getUniqueId()
          .toString();

  scoreboardRegistry.removeFromRegistry(id);

  Optional<User> optionalUser = userObjectCache.find(id);

  if (!optionalUser.isPresent()) {
   return;
  }

  User user = optionalUser.get();

  Bukkit.getPluginManager().callEvent(new MatchLeaveEvent(user, MatchLeaveEvent.Cause.DISCONNECT));

  userObjectCache.remove(id);
  userObjectStorage.save(user);
 }
}