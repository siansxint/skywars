package net.sintaxis.skywars.core.cage.matcher;

import net.sintaxis.skywars.api.cache.ObjectCache;
import net.sintaxis.skywars.core.cage.Cage;
import net.sintaxis.skywars.core.team.Team;
import net.sintaxis.skywars.core.user.User;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.inject.Inject;

import java.util.Optional;

public class CageMatcher {

 @Inject
 private ObjectCache<Cage> cageObjectCache;

 private static final Cage DEFAULT_CAGE = new Cage(
         "default",
         new ItemStack(Material.GLASS),
         new ItemStack(Material.GLASS),
         new ItemStack(Material.GLASS),
         true,
         true
 );

 public Cage findCage(Team team) {

  Cage cage = DEFAULT_CAGE;

  for (User user : team) {

   if (user.getCageName().equalsIgnoreCase("default")) {
    continue;
   }

   Optional<Cage> optionalCage =
           cageObjectCache.find(user.getCageName());

   if (!optionalCage.isPresent()) {
    continue;
   }

   cage = optionalCage.get();
  }

  return cage;
 }
}