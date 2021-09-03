package net.sintaxis.skywars.api.cache.local;

import net.sintaxis.skywars.api.cache.ObjectCache;
import net.sintaxis.skywars.api.model.Savable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LocalObjectCache<O extends Savable>
        implements ObjectCache<O> {

 private final Map<String, O> cachedMap;

 public LocalObjectCache() {
  cachedMap = new ConcurrentHashMap<>();
 }

 @Override
 public Map<String, O> get() {
  return cachedMap;
 }
}