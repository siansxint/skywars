package net.sintaxis.skywars.core.command.internal.part.user;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.bukkit.BukkitCommandManager;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.exception.CommandException;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.stack.ArgumentStack;

import net.kyori.text.TranslatableComponent;

import net.sintaxis.skywars.api.cache.ObjectCache;
import net.sintaxis.skywars.core.user.User;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class UserSenderPart
        implements CommandPart {

 private final String name;
 private final ObjectCache<User> userObjectCache;

 public UserSenderPart(String name,
                       ObjectCache<User> userObjectCache
 ) {
  this.name = name;
  this.userObjectCache = userObjectCache;
 }

 @Override
 public String getName() {
  return name;
 }

 @Override
 public void parse(CommandContext context,
                   ArgumentStack stack
 ) throws ArgumentParseException {
  CommandSender commandSender = context.getObject(CommandSender.class, BukkitCommandManager.SENDER_NAMESPACE);

  if (commandSender == null) {
   throw new CommandException(TranslatableComponent.of("sender.unknown"));
  }

  if (!(commandSender instanceof Player)) {
   throw new ArgumentParseException(TranslatableComponent.of("sender.only-player"));
  }

  Player player = (Player) commandSender;

  Optional<User> userOptional = userObjectCache.find(player.getUniqueId().toString());

  if (!userOptional.isPresent()) {
   throw new ArgumentParseException(TranslatableComponent.of("user.not-found"));
  }

  context.setValue(this, userOptional.get());
 }
}