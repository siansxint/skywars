package net.sintaxis.skywars.core.service;

import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilder;
import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilderImpl;
import me.fixeddev.commandflow.annotated.part.Key;
import me.fixeddev.commandflow.annotated.part.PartInjector;
import me.fixeddev.commandflow.annotated.part.defaults.DefaultsModule;
import me.fixeddev.commandflow.annotated.part.defaults.factory.EnumPartFactory;
import me.fixeddev.commandflow.bukkit.BukkitCommandManager;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import me.fixeddev.commandflow.bukkit.factory.BukkitModule;
import me.fixeddev.commandflow.translator.DefaultTranslator;

import me.yushust.inject.InjectAll;

import net.sintaxis.skywars.api.model.Service;
import net.sintaxis.skywars.core.command.SkyWarsMainCommand;
import net.sintaxis.skywars.core.command.SkyWarsSetupCommand;
import net.sintaxis.skywars.core.command.internal.CustomTranslationProvider;
import net.sintaxis.skywars.core.command.internal.part.match.MatchPartFactory;
import net.sintaxis.skywars.core.command.internal.part.user.UserSenderPartFactory;
import net.sintaxis.skywars.core.match.Match;
import net.sintaxis.skywars.core.match.type.MatchType;
import net.sintaxis.skywars.core.user.User;

@InjectAll
public class CommandService
        implements Service {

 private SkyWarsMainCommand skyWarsMainCommand;
 private SkyWarsSetupCommand skyWarsSetupCommand;
 private MatchPartFactory matchPartFactory;
 private UserSenderPartFactory userSenderPartFactory;
 private CustomTranslationProvider customTranslationProvider;

 @Override
 public void start() {
  PartInjector partInjector = PartInjector.create();
  partInjector.install(new DefaultsModule());
  partInjector.install(new BukkitModule());
  partInjector.bindFactory(Match.class, matchPartFactory);
  partInjector.bindFactory(new Key(User.class, Sender.class), userSenderPartFactory);
  partInjector.bindFactory(new Key(MatchType.class), new EnumPartFactory(MatchType.class));

  AnnotatedCommandTreeBuilder treeBuilder = new AnnotatedCommandTreeBuilderImpl(partInjector);

  CommandManager commandManager = new BukkitCommandManager("skywars");

  commandManager.setTranslator(new DefaultTranslator(customTranslationProvider));
  commandManager.registerCommands(treeBuilder.fromClass(skyWarsMainCommand));
  commandManager.registerCommands(treeBuilder.fromClass(skyWarsSetupCommand));
 }
}