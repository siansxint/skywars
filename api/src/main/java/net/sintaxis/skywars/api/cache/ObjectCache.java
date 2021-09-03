package net.sintaxis.skywars.api.cache;

import java.util.Map;
import java.util.Optional;

/**
 * A temporal cache that uses a {@link Map} as temporally storage.
 *
 * @param <V> The type of value stored in this {@link ObjectCache}.
 */

public interface ObjectCache<V> {

 /**
  * Provides the {@link Map} repository of this {@link ObjectCache}.
  *
  * @return The repository {@link Map}.
  */
 Map<String, V> get();

 /**
  * Searches for an {@link V} keyed with the given id, and returns an {@link Optional} value.
  *
  * @param id The {@link V}'s key.
  * @return An {@link Optional} value of the {@link V}.
  * @see "{@link Map#get(Object)}"
  */
 default Optional<V> find(String id) {
  return Optional.ofNullable(get().get(id));
 }

 /**
  * Adds a {@link V} keyed with the given id to the {@link Map} repository.
  *
  * @param id The {@link V}'s key.
  * @param v  The {@link V} keyed with the given id.
  * @see "{@link Map#put(Object, Object)}"
  */
 default void add(String id, V v) {
  get().put(id, v);
 }

 /**
  * Removes a {@link V} keyed with the given id from the {@link Map} repository.
  *
  * @param id The {@link V}'s key.
  * @see "{@link Map#remove(Object)}"
  */
 default void remove(String id) {
  get().remove(id);
 }

 /**
  * Checks if there's a {@link V} with the given id into the {@link Map} repository.
  *
  * @param id The {@link V}'s key.
  * @return True if present, otherwise false.
  * @see "{@link Map#containsKey(Object)}"
  */
 default boolean exists(String id) {
  return get().containsKey(id);
 }

 /**
  * Clears the {@link Map} repository.
  *
  * @see "{@link Map#clear()}"
  */
 default void clear() {
  get().clear();
 }
}