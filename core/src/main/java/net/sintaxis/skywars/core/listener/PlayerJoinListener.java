package net.sintaxis.skywars.core.listener;

import me.yushust.inject.InjectAll;

import net.sintaxis.skywars.api.cache.ObjectCache;
import net.sintaxis.skywars.api.storage.ObjectStorage;
import net.sintaxis.skywars.core.file.YamlFile;
import net.sintaxis.skywars.core.scoreboard.Scoreboard;
import net.sintaxis.skywars.core.scoreboard.registry.ScoreboardRegistry;
import net.sintaxis.skywars.core.user.User;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import javax.inject.Named;

@InjectAll
public class PlayerJoinListener
        implements Listener {

 private ScoreboardRegistry scoreboardRegistry;
 @Named("scoreboards")
 private YamlFile scoreboards;
 private ObjectCache<User> userObjectCache;
 private ObjectStorage<User> userObjectStorage;

 @EventHandler
 public void onPlayerJoin(PlayerJoinEvent event) {

  Player player = event.getPlayer();

  String id = player.getUniqueId().toString();

  Scoreboard scoreboard = new Scoreboard(
          scoreboards.getConfigurationSection("lobby-scoreboard"),
          id
  );

  scoreboard.show(player);

  scoreboardRegistry.addToRegistry(id, scoreboard);

  userObjectStorage
          .findOne(id)
          .whenComplete((optionalUser, throwable) -> {
           User user = optionalUser.orElse(new User(id));
           userObjectCache.add(id, user);
          });
 }
}