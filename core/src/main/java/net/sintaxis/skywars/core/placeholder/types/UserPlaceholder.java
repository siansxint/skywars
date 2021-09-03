package net.sintaxis.skywars.core.placeholder.types;

import net.sintaxis.skywars.api.cache.ObjectCache;
import net.sintaxis.skywars.core.placeholder.Placeholder;
import net.sintaxis.skywars.core.user.User;

import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.Optional;

public class UserPlaceholder
        implements Placeholder {

 @Inject
 private ObjectCache<User> userObjectCache;

 @Override
 public String getName() {
  return "statistics";
 }

 @Override
 public String apply(Player player, String text) {

  if (player == null) {
   return "";
  }

  Optional<User> optionalUser = userObjectCache.find(player.getUniqueId().toString());

  if (!optionalUser.isPresent()) {
   return "";
  }

  User user = optionalUser.get();

  // TODO: 12/07/2021 ugly, refactor this
  switch (text) {
   case "kills":
    return user.getTemporally().getMatchKills().get() + "";
   case "money":
    return user.getMoney().get() + "";
   case "solo-wins":
    return user.getSoloStatistics().getWins().get() + "";
   case "solo-losses":
    return user.getSoloStatistics().getLosses().get() + "";
   case "solo-kills":
    return user.getSoloStatistics().getKills().get() + "";
   case "solo-deaths":
    return user.getSoloStatistics().getDeaths().get() + "";
   case "team-wins":
    return user.getTeamStatistics().getWins().get() + "";
   case "team-losses":
    return user.getTeamStatistics().getLosses().get() + "";
   case "team-kills":
    return user.getTeamStatistics().getKills().get() + "";
   case "team-deaths":
    return user.getTeamStatistics().getDeaths().get() + "";
   case "team-name":
    return user.getTemporally().getTeamName().orElse("");
   default:
    return "";
  }
 }
}