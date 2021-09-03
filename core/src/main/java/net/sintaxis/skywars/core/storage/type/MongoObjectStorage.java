package net.sintaxis.skywars.core.storage.type;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;

import net.sintaxis.skywars.api.connection.Connection;
import net.sintaxis.skywars.api.model.Savable;
import net.sintaxis.skywars.api.storage.ObjectStorage;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class MongoObjectStorage<O extends Savable>
        implements ObjectStorage<O> {

 private final MongoCollection<O> mongoCollection;

 private final ExecutorService service;

 private static final ReplaceOptions OPTIONS
         = new ReplaceOptions().upsert(true);

 public MongoObjectStorage(Class<O> clazz,
                           Connection<MongoClient> mongoClientConnection,
                           ExecutorService service
 ) {
  this.mongoCollection = mongoClientConnection
          .getConnection()
          .getDatabase("skywars")
          .getCollection(clazz.getSimpleName().toLowerCase(), clazz);
  this.service = service;
 }

 @Override
 public Optional<O> findOneSync(String id) {
  return Optional.ofNullable(mongoCollection.find(Filters.eq("_id", id)).first());
 }

 @Override
 public CompletableFuture<Optional<O>> findOne(String id) {
  return CompletableFuture.supplyAsync(() -> findOneSync(id), service);
 }

 @Override
 public Set<O> getAllSync() {
  return mongoCollection.find().into(new LinkedHashSet<>());
 }

 @Override
 public CompletableFuture<Set<O>> getAll() {
  return CompletableFuture.supplyAsync(this::getAllSync, service);
 }

 @Override
 public CompletableFuture<Void> save(O object) {
  return CompletableFuture.runAsync(() -> mongoCollection.replaceOne(
          Filters.eq("_id", object.getId()
          ), object, OPTIONS), service
  );
 }

 @Override
 public CompletableFuture<Void> delete(String id) {
  return CompletableFuture.runAsync(() -> mongoCollection.deleteOne(
          Filters.eq("_id", id)
  ), service);
 }
}