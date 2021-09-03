package net.sintaxis.skywars.core;

import me.yushust.inject.Injector;

import net.sintaxis.skywars.api.model.Service;

import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;
import java.util.Set;

public final class SkyWars
        extends JavaPlugin {

 @Inject
 private Set<Service> services;

 @Override
 public void onLoad() {
  Injector.create(new SkyWarsModule(this))
          .injectMembers(this);
 }

 @Override
 public void onEnable() {
  for (Service service : services) {
   service.start();
  }
 }

 @Override
 public void onDisable() {
  for (Service service : services) {
   service.stop();
  }
 }
}