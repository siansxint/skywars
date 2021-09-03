package net.sintaxis.skywars.core.tag;

import me.yushust.inject.InjectAll;

import net.sintaxis.skywars.api.cache.ObjectCache;
import net.sintaxis.skywars.core.placeholder.applier.PlaceholderApplier;
import net.sintaxis.skywars.core.user.User;
import net.sintaxis.skywars.core.user.state.UserState;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Optional;

@InjectAll
public class NameTagApplier {

 private NameTagFinder nameTagFinder;
 private ObjectCache<User> userObjectCache;

 public void applyToPlayer(Player player) {

  Scoreboard scoreboard = player.getScoreboard() != null ? player.getScoreboard() : Bukkit.getScoreboardManager().getNewScoreboard();

  for (Player target : Bukkit.getOnlinePlayers()) {

   if (!player.canSee(target)) {
    continue;
   }

   Optional<User> optionalUser =
           userObjectCache.find(target.getUniqueId().toString());

   if (!optionalUser.isPresent()) {
    continue;
   }

   User user = optionalUser.get();

   if (user.getTemporally().getRole() == UserState.LOBBY) {
    continue;
   }

   NameTagFormat format = nameTagFinder.getTagFormatByType(user.getTemporally().getRole());

   Team team = scoreboard.getTeam(target.getName());

   if (team == null) {
    team = scoreboard.registerNewTeam(target.getName());
   }

   team.setPrefix(PlaceholderApplier.replace(target, format.getPrefix()));
   team.setSuffix(PlaceholderApplier.replace(target, format.getSuffix()));

   if (!team.hasEntry(target.getName())) {
    team.addEntry(target.getName());
   }
  }
 }
}