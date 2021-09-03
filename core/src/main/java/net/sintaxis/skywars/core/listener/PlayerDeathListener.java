package net.sintaxis.skywars.core.listener;

import me.yushust.message.MessageHandler;

import net.sintaxis.skywars.api.cache.ObjectCache;
import net.sintaxis.skywars.core.coordinate.AbbreviatedCoordinates;
import net.sintaxis.skywars.core.match.Match;
import net.sintaxis.skywars.core.user.User;
import net.sintaxis.skywars.core.util.PlayerResetUtil;
import net.sintaxis.skywars.core.match.state.MatchState;
import net.sintaxis.skywars.core.packet.actionbar.ActionbarMessenger;
import net.sintaxis.skywars.core.packet.title.TitleMessenger;
import net.sintaxis.skywars.core.user.state.UserState;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import javax.inject.Inject;

import java.util.Optional;

public class PlayerDeathListener
        extends AbstractMatchListener {

 private final ObjectCache<Match> matchObjectCache;
 private final TitleMessenger titleMessenger;
 private final ActionbarMessenger actionbarMessenger;
 private final MessageHandler messageHandler;

 @Inject
 public PlayerDeathListener(ObjectCache<User> userObjectCache,
                            ObjectCache<Match> matchObjectCache,
                            TitleMessenger titleMessenger,
                            ActionbarMessenger actionbarMessenger,
                            MessageHandler messageHandler
 ) {
  super(userObjectCache);
  this.matchObjectCache = matchObjectCache;
  this.titleMessenger = titleMessenger;
  this.actionbarMessenger = actionbarMessenger;
  this.messageHandler = messageHandler;
 }

 @EventHandler
 public void onPlayerDeath(PlayerDeathEvent event) {

  event.setDeathMessage(null);

  Player player = event.getEntity();
  User user = getUserOrThrowException(player);

  if (!user.getTemporally().getMatchName().isPresent()) {
   return;
  }

  Optional<Match> optionalMatch = matchObjectCache.find(user.getTemporally().getMatchName().get());

  if (!optionalMatch.isPresent()) {
   return;
  }

  Match match = optionalMatch.get();

  if (requireCancel(match)) {
   return;
  }

  user.getTemporally().setRole(UserState.DEAD);

  event.getDrops().forEach(itemStack -> player.getWorld().dropItemNaturally(player.getLocation(), itemStack));
  event.getDrops().clear();

  user.getStatisticsByType(match.getMatchType())
          .getDeaths().add(1);
  user.getStatisticsByType(match.getMatchType())
          .getLosses().add(1);
  match.getTemporally().setCurrentPlayers(match.getTemporally().getCurrentPlayers() - 1);

  PlayerResetUtil.reset(player);

  player.teleport(AbbreviatedCoordinates.toLocation(match.getMapInfo().getSpectatorCoordinates()));

  player.setAllowFlight(true);
  player.setFlying(true);

  titleMessenger.sendBoth(
          player,
          messageHandler.get(player, "user-events.spectator.title"),
          messageHandler.get(player, "user-events.spectator.sub-title"),
          20,
          20,
          20);

  if (player.getKiller() != null && !player.getKiller().getName().equalsIgnoreCase(player.getName())) {
   User killer = getUserOrThrowException(player.getKiller());
   killer.getStatisticsByType(match.getMatchType())
           .getKills().add(1);
   killer.getTemporally()
           .getMatchKills().add(1);
   actionbarMessenger.send(
           player.getKiller(),
           messageHandler.replacing(player.getKiller(), "user-events.kill.action-bar", "%player-name%", player.getName()));
  }
 }

 @Override
 protected boolean requireCancel(Match match) {
  MatchState state = match.getTemporally().getState();
  return state == MatchState.WAITING ||
          state == MatchState.STARTING ||
          state == MatchState.ENDING;
 }
}