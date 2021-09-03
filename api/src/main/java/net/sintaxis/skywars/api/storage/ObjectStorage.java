package net.sintaxis.skywars.api.storage;

import net.sintaxis.skywars.api.model.Savable;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface ObjectStorage<O extends Savable> {

 /**
  * Find an object in the database repository with their id
  *
  * @param id The id of the object to find
  * @return An optional sync response of the object
  */

 Optional<O> findOneSync(String id);

 /**
  * Find an object in the database repository with their id
  *
  * @param id The id of the object to find
  * @return An optional async response of the object
  */

 CompletableFuture<Optional<O>> findOne(String id);

 /**
  * Get all the objects stored in the database repository collection
  *
  * @return All the objects found at the repository collection
  */

 Set<O> getAllSync();

 /**
  * Get all the objects stored in the database repository collection
  *
  * @return An async response with all the objects found at the repository collection
  */

 CompletableFuture<Set<O>> getAll();

 /**
  * Save an object with the same type of the generic type and save in the database
  *
  * @param object The object to save
  * @return An async void response indicating that was saved
  */

 CompletableFuture<Void> save(O object);

 /**
  * Delete an object of the database
  *
  * @param id The id of the object to delete
  * @return An async void response indicating that was deleted
  */

 CompletableFuture<Void> delete(String id);
}