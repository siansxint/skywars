package net.sintaxis.skywars.core.listener;

import net.sintaxis.skywars.api.cache.ObjectCache;
import net.sintaxis.skywars.core.file.YamlFile;
import net.sintaxis.skywars.core.match.Match;
import net.sintaxis.skywars.core.match.type.MatchType;
import net.sintaxis.skywars.core.placeholder.applier.PlaceholderApplier;
import net.sintaxis.skywars.core.user.User;
import net.sintaxis.skywars.core.user.finder.UserFinder;
import net.sintaxis.skywars.core.world.WorldHandler;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import javax.inject.Inject;

public class AsyncPlayerChatListener
        extends AbstractMatchListener {

 private final ObjectCache<Match> matchObjectCache;
 private final UserFinder userFinder;
 private final YamlFile config;

 @Inject
 public AsyncPlayerChatListener(ObjectCache<User> userObjectCache,
                                ObjectCache<Match> matchObjectCache,
                                UserFinder userFinder,
                                YamlFile config
 ) {
  super(userObjectCache);
  this.matchObjectCache = matchObjectCache;
  this.userFinder = userFinder;
  this.config = config;
 }

 @EventHandler
 public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {

  Player player = event.getPlayer();

  World world = player.getWorld();

  if (!world.getName().startsWith(WorldHandler.MATCH_WORLD_PREFIX)) {
   event.getRecipients().clear();
   event.getRecipients().addAll(world.getPlayers());
   return;
  }

  User.Temporally temporally = getUserOrThrowException(player).getTemporally();

  temporally.getMatchName()
          .flatMap(matchObjectCache::find)
          .ifPresent(match -> {

           event.setCancelled(true);

           if (!event.getMessage().startsWith(config.getString("chat.global-id")) &&
                   temporally.getTeamName().isPresent() &&
                   match.getMatchType() != MatchType.SOLO) {

            match.getTemporally().getTeams()
                    .get(temporally.getTeamName().get())
                    .forEach(recipient -> recipient.getTemporally().getRaw()
                            .ifPresent(target -> target.sendMessage(
                                    PlaceholderApplier.replace(player,
                                            config.getString("chat.team-format"))
                                            .replace("%message%", event.getMessage().replaceFirst("!", "")))
                            )
                    );
            return;
           }

           userFinder.getMatchPlayers(match).forEach(target -> target.sendMessage(
                   PlaceholderApplier.replace(player,
                           config.getString("chat.global-format"))
                           .replace("%message%", event.getMessage())));
          });
 }
}