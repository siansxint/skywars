package net.sintaxis.skywars.core.cage.spawner;

import net.sintaxis.skywars.core.coordinate.AbbreviatedCoordinates;
import net.sintaxis.skywars.core.cage.Cage;
import net.sintaxis.skywars.core.match.Match;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CageSpawner {

 public void spawnCage(Cage cage,
                       Location where,
                       int area
 ) {
  spawn(cage, where, area);
 }

 private void spawn(Cage cage,
                    Location where,
                    int diameter) {

  if (diameter % 2 == 0) {
   diameter -= 1;
  }

  int minY = where.getBlockY() - 1;
  int maxY = where.getBlockY() + 2;

  for (int y = minY; y <= maxY; y++) {
   where.setY(y);

   if (y == minY || y == maxY) {
    setSolidLevel(cage, where, diameter);
    continue;
   }
   setWallLevel(cage, where, diameter);
  }
 }

 private void setSolidLevel(Cage cage,
                            Location levelCenter,
                            int diameter
 ) {
  int radius = diameter / 2;
  levelCenter = levelCenter.clone();

  ItemStack floor = cage.getFloor();

  if (cage.hasPointedCorners()) {
   if (radius == 1) {
    levelCenter.getBlock().setType(floor.getType());
   } else {
    fill(levelCenter, radius, floor, floor);
   }
  } else if (cage.isOverlappingCorners()) {
   fill(levelCenter, radius, floor, cage.getWalls());
  } else {
   fill(levelCenter, radius, floor, floor);
  }
 }

 private void setWallLevel(Cage cage,
                           Location levelCenter,
                           int diameter
 ) {
  int radius = (diameter / 2) + 1;
  levelCenter = levelCenter.clone();

  ItemStack wall = cage.getWalls();

  if (cage.hasPointedCorners()) {
   fill(levelCenter, radius, new ItemStack(Material.AIR), wall, new ItemStack(Material.AIR));
  } else {
   fill(levelCenter, radius, new ItemStack(Material.AIR), wall);
  }
 }

 private void fill(Location center,
                   int radius,
                   ItemStack centerItem,
                   ItemStack edgesItem
 ) {
  fill(center, radius, centerItem, edgesItem, edgesItem);
 }

 private void fill(Location center,
                   int radius,
                   ItemStack centerItem,
                   ItemStack edgesItem,
                   ItemStack cornerItem
 ) {
  Location currentBlock = center.clone();

  int maxX = center.getBlockX() + radius;
  int maxZ = center.getBlockZ() + radius;
  int minX = center.getBlockX() - radius;
  int minZ = center.getBlockZ() - radius;

  for (int x = minX; x <= maxX; x++) {
   currentBlock.setX(x);

   for (int z = minZ; z <= maxZ; z++) {
    currentBlock.setZ(z);

    if ((x == minX || x == maxX) && (z == minZ || z == maxZ)) {
     currentBlock.getBlock().setType(cornerItem.getType());
     continue;
    }

    if ((x == minX || x == maxX) || (z == minZ || z == maxZ)) {
     currentBlock.getBlock().setType(edgesItem.getType());
     continue;
    }
    currentBlock.getBlock().setType(centerItem.getType());
   }
  }
 }

 public void removeAllCages(Match match,
                            int area
 ) {
  for (AbbreviatedCoordinates where : match.getMapInfo().getSpawnsCoordinates()) {
   spawn(new Cage(
                   "empty",
                   new ItemStack(Material.AIR),
                   new ItemStack(Material.AIR),
                   new ItemStack(Material.AIR),
                   true,
                   true),
           AbbreviatedCoordinates.toLocation(where),
           area);
  }
 }
}