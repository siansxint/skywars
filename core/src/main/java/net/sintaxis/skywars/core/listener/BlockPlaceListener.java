package net.sintaxis.skywars.core.listener;

import net.sintaxis.skywars.api.cache.ObjectCache;
import net.sintaxis.skywars.core.match.Match;
import net.sintaxis.skywars.core.match.state.MatchState;
import net.sintaxis.skywars.core.user.User;
import net.sintaxis.skywars.core.user.state.UserState;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

import javax.inject.Inject;

public class BlockPlaceListener
        extends AbstractMatchListener {

 private final ObjectCache<Match> matchObjectCache;

 @Inject
 public BlockPlaceListener(ObjectCache<User> userObjectCache,
                           ObjectCache<Match> matchObjectCache
 ) {
  super(userObjectCache);
  this.matchObjectCache = matchObjectCache;
 }

 @EventHandler
 public void onBlockPlace(BlockPlaceEvent event) {

  User user = getUserOrThrowException(event.getPlayer());

  User.Temporally temporally = user.getTemporally();

  temporally.getMatchName()
          .flatMap(matchObjectCache::find)
          .ifPresent(match -> {

           if (requireCancel(match) || temporally.getRole() == UserState.DEAD) {
            event.setCancelled(true);
            return;
           }

           Block block = event.getBlock();

           if (block.getType() == Material.TNT) {
            block.setType(Material.AIR);

            Location location = block.getLocation();
            location.getWorld().spawnEntity(location, EntityType.PRIMED_TNT);
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