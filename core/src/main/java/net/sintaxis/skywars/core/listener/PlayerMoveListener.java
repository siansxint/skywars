package net.sintaxis.skywars.core.listener;

import net.sintaxis.skywars.api.cache.ObjectCache;
import net.sintaxis.skywars.core.coordinate.AbbreviatedCoordinates;
import net.sintaxis.skywars.core.match.Match;
import net.sintaxis.skywars.core.match.state.MatchState;
import net.sintaxis.skywars.core.user.User;
import net.sintaxis.skywars.core.user.state.UserState;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import javax.inject.Inject;

public class PlayerMoveListener
        extends AbstractMatchListener {

 private final ObjectCache<Match> matchObjectCache;

 @Inject
 public PlayerMoveListener(ObjectCache<User> userObjectCache,
                           ObjectCache<Match> matchObjectCache
 ) {
  super(userObjectCache);
  this.matchObjectCache = matchObjectCache;
 }

 @EventHandler
 public void onPlayerMove(PlayerMoveEvent event) {
  Player player = event.getPlayer();

  User.Temporally temporally = getUserOrThrowException(player)
          .getTemporally();

  temporally.getMatchName()
          .flatMap(matchObjectCache::find)
          .ifPresent(match -> {
           if ((temporally.getRole() == UserState.DEAD ||
                   requireCancel(match)) &&
                   player.getLocation().getY() < 20) {
            player.teleport(AbbreviatedCoordinates.toLocation(match.getMapInfo().getLobbyCoordinates()));
            player.setFallDistance(0);
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