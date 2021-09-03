package net.sintaxis.skywars.core.command.internal;

import me.fixeddev.commandflow.Namespace;
import me.fixeddev.commandflow.bukkit.BukkitCommandManager;
import me.fixeddev.commandflow.translator.TranslationProvider;

import me.yushust.message.MessageHandler;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class CustomTranslationProvider
        implements TranslationProvider {

 @Inject
 private MessageHandler messageHandler;

 @Override
 public String getTranslation(Namespace namespace, String key) {
  CommandSender commandSender = namespace.getObject(CommandSender.class, BukkitCommandManager.SENDER_NAMESPACE);

  if (commandSender instanceof Player) {
   Player player = (Player) commandSender;
   return messageHandler.get(player, "commands." + key);
  }

  return messageHandler.getMessage("commands." + key);
 }
}