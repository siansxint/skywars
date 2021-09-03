package net.sintaxis.skywars.core.translation;

import me.yushust.inject.InjectAll;

import me.yushust.message.MessageHandler;
import me.yushust.message.MessageProvider;
import me.yushust.message.bukkit.BukkitMessageAdapt;
import me.yushust.message.source.MessageSource;
import me.yushust.message.source.MessageSourceDecorator;

import net.sintaxis.skywars.core.SkyWars;
import net.sintaxis.skywars.core.translation.linguistic.PlayerLinguistProvider;
import net.sintaxis.skywars.core.user.User;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import javax.inject.Provider;

@InjectAll
public class MessageHandlerProvider
        implements Provider<MessageHandler> {

 private SkyWars skyWars;
 private PlayerLinguistProvider playerLinguistProvider;

 @Override
 public MessageHandler get() {
  MessageSource messageSource = MessageSourceDecorator
          .decorate(BukkitMessageAdapt.newYamlSource(skyWars, "lang_%lang%.yml"))
          .addFallbackLanguage("es")
          .get();

  MessageProvider messageProvider = MessageProvider
          .create(
                  messageSource,
                  config -> {
                   config.specify(Player.class)
                           .setLinguist(playerLinguistProvider)
                           .setMessageSender((player, s, text) -> player.sendMessage(text))
                           .resolveFrom(User.class, user -> user.getTemporally().getRaw().orElse(null));
                   config.addInterceptor(s -> ChatColor.translateAlternateColorCodes('&', s));
                  }
          );
  return MessageHandler.of(messageProvider);
 }
}