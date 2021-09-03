package net.sintaxis.skywars.core.service;

import com.mongodb.client.MongoClient;

import net.sintaxis.skywars.api.connection.Connection;
import net.sintaxis.skywars.api.model.Service;

import javax.inject.Inject;

public class ConnectionService
        implements Service {

 @Inject
 private Connection<MongoClient> mongoClientConnection;

 @Override
 public void start() {}

 @Override
 public void stop() {
  mongoClientConnection.close();
 }
}