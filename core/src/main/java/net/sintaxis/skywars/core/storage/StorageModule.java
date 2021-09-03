package net.sintaxis.skywars.core.storage;

import com.mongodb.client.MongoClient;

import me.yushust.inject.AbstractModule;
import me.yushust.inject.key.TypeReference;

import net.sintaxis.skywars.api.connection.Connection;
import net.sintaxis.skywars.api.storage.ObjectStorage;
import net.sintaxis.skywars.core.match.Match;
import net.sintaxis.skywars.core.storage.type.MongoObjectStorage;
import net.sintaxis.skywars.core.user.User;

import java.util.concurrent.ExecutorService;

public class StorageModule
        extends AbstractModule {

 private final Connection<MongoClient> mongoClientConnection;
 private final ExecutorService service;

 public StorageModule(Connection<MongoClient> mongoClientConnection,
                      ExecutorService service
 ) {
  this.mongoClientConnection = mongoClientConnection;
  this.service = service;
 }

 @Override
 protected void configure() {
  bind(new TypeReference<ObjectStorage<User>>() {})
          .toInstance(new MongoObjectStorage<>(User.class, mongoClientConnection, service));

  bind(new TypeReference<ObjectStorage<Match>>() {})
          .toInstance(new MongoObjectStorage<>(Match.class, mongoClientConnection, service));
 }
}