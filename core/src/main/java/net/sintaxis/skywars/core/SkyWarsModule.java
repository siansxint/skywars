package net.sintaxis.skywars.core;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.mongodb.client.MongoClient;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

import me.yushust.inject.AbstractModule;
import me.yushust.inject.key.TypeReference;
import me.yushust.message.MessageHandler;

import net.sintaxis.skywars.api.model.Service;
import net.sintaxis.skywars.api.cache.ObjectCache;
import net.sintaxis.skywars.api.cache.local.LocalObjectCache;
import net.sintaxis.skywars.api.connection.Connection;
import net.sintaxis.skywars.core.cage.Cage;
import net.sintaxis.skywars.core.chest.SkyChest;
import net.sintaxis.skywars.core.connection.MongoConnection;
import net.sintaxis.skywars.core.file.YamlFile;
import net.sintaxis.skywars.core.logic.distributor.CountdownMatchDistributor;
import net.sintaxis.skywars.core.mapper.ObjectMapperProvider;
import net.sintaxis.skywars.core.match.Match;
import net.sintaxis.skywars.core.match.event.provider.MatchEventProvider;
import net.sintaxis.skywars.core.placeholder.PlaceholderModule;
import net.sintaxis.skywars.core.processor.ProcessorModule;
import net.sintaxis.skywars.core.scoreboard.registry.ScoreboardRegistry;
import net.sintaxis.skywars.core.service.CommandService;
import net.sintaxis.skywars.core.service.ConnectionService;
import net.sintaxis.skywars.core.service.ListenerService;
import net.sintaxis.skywars.core.service.MatchService;
import net.sintaxis.skywars.core.service.PlaceholderService;
import net.sintaxis.skywars.core.service.SchedulerService;
import net.sintaxis.skywars.core.service.SkyChestService;
import net.sintaxis.skywars.core.service.WorkersService;
import net.sintaxis.skywars.core.storage.StorageModule;
import net.sintaxis.skywars.core.translation.MessageHandlerProvider;
import net.sintaxis.skywars.core.user.User;
import net.sintaxis.skywars.core.world.WorldHandler;
import net.sintaxis.skywars.core.world.provider.WorldHandlerProvider;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SkyWarsModule
        extends AbstractModule {

 private final SkyWars skyWars;

 public SkyWarsModule(SkyWars skyWars) {
  this.skyWars = skyWars;
 }

 @Override
 protected void configure() {
  YamlFile config = new YamlFile(skyWars, "config");
  bind(YamlFile.class)
          .toInstance(config);
  bind(YamlFile.class)
          .named("scoreboards")
          .toInstance(new YamlFile(skyWars, "scoreboards"));

  bind(new TypeReference<ObjectCache<User>>() {})
          .toInstance(new LocalObjectCache<>());
  bind(new TypeReference<ObjectCache<Match>>() {})
          .toInstance(new LocalObjectCache<>());
  bind(new TypeReference<ObjectCache<Match>>() {})
          .named("match-distributor-cache")
          .toInstance(new LocalObjectCache<>());
  bind(new TypeReference<ObjectCache<Cage>>() {})
          .toInstance(new LocalObjectCache<>());

  ObjectCache<SkyChest> chestObjectCache
          = new LocalObjectCache<>();
  bind(new TypeReference<ObjectCache<SkyChest>>() {})
          .toInstance(chestObjectCache);

  ObjectMapper mapper = new ObjectMapperProvider(
          new MatchEventProvider(skyWars, chestObjectCache)
  ).get();
  bind(ObjectMapper.class)
          .toInstance(mapper);

  Connection<MongoClient> mongoClientConnection
          = new MongoConnection(config, mapper);
  bind(new TypeReference<Connection<MongoClient>>() {})
          .toInstance(mongoClientConnection);

  multibind(Service.class)
          .asCollection(Set.class, HashSet::new)
          .to(ConnectionService.class)
          .to(MatchService.class)
          .to(WorkersService.class)
          .to(ListenerService.class)
          .to(CommandService.class)
          .to(PlaceholderService.class)
          .to(SchedulerService.class)
          .to(SkyChestService.class)
          .singleton();

  ExecutorService service = Executors.newCachedThreadPool();
  bind(ExecutorService.class)
          .toInstance(service);

  install(new StorageModule(mongoClientConnection, service));

  bind(MessageHandler.class)
          .toProvider(MessageHandlerProvider.class)
          .singleton();

  install(new ProcessorModule());

  bind(CountdownMatchDistributor.class)
          .singleton();

  bind(ScoreboardRegistry.class)
          .singleton();

  install(new PlaceholderModule());

  bind(WorldHandler.class)
          .toProvider(WorldHandlerProvider.class)
          .singleton();

  bind(WorldEditPlugin.class)
          .toProvider(() -> (WorldEditPlugin) skyWars.getServer().getPluginManager().getPlugin("WorldEdit"));

  bind(SkyWars.class).toInstance(skyWars);
 }
}