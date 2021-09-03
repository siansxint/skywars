package net.sintaxis.skywars.core.connection;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoDriverInformation;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import net.sintaxis.codec.MongoJacksonCodecProvider;
import net.sintaxis.skywars.api.connection.Connection;
import net.sintaxis.skywars.core.file.YamlFile;

import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;

import javax.inject.Inject;

public class MongoConnection
        implements Connection<MongoClient> {

 private final MongoClient client;

 public MongoConnection(YamlFile config,
                        ObjectMapper mapper
 ) {
  CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
          CodecRegistries.fromProviders(new MongoJacksonCodecProvider(mapper)));

  MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
          .codecRegistry(codecRegistry)
          .applyConnectionString(new ConnectionString(config.getString("mongo.uri")))
          .build();
  MongoDriverInformation mongoDriverInformation = MongoDriverInformation
          .builder()
          .driverName("sync")
          .build();

  this.client = MongoClients.create(
          mongoClientSettings,
          mongoDriverInformation
  );
 }

 @Override
 public MongoClient getConnection() {
  return client;
 }

 @Override
 public void close() {
  if (client != null) {
   client.close();
  }
 }
}