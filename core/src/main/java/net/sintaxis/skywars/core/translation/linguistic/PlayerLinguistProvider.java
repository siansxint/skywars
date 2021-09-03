package net.sintaxis.skywars.core.translation.linguistic;

import me.yushust.message.language.Linguist;

import net.sintaxis.skywars.api.cache.ObjectCache;
import net.sintaxis.skywars.core.user.User;

import org.bukkit.entity.Player;

import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;

public class PlayerLinguistProvider
        implements Linguist<Player> {

 @Inject
 private ObjectCache<User> userObjectCache;

 @Override
 public @Nullable String getLanguage(Player player) {
  return userObjectCache.find(player.getUniqueId().toString())
          .map(User::getLanguage)
          .orElse("es");
 }
}