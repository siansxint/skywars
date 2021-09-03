package net.sintaxis.skywars.core.coordinate;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.beans.ConstructorProperties;

public class AbbreviatedCoordinates {

 private final String worldName;
 private final double x;
 private final double y;
 private final double z;

 @ConstructorProperties({
         "worldName",
         "x",
         "y",
         "z"
 })
 public AbbreviatedCoordinates(String worldName,
                               double x,
                               double y,
                               double z
 ) {
  this.worldName = worldName;
  this.x = x;
  this.y = y;
  this.z = z;
 }

 public static Location toLocation(AbbreviatedCoordinates coordinates) {
  return new Location(Bukkit.getWorld(coordinates.getWorldName()), coordinates.getX(), coordinates.getY(), coordinates.getZ());
 }

 public static AbbreviatedCoordinates fromLocation(Location location) {
  return new AbbreviatedCoordinates(location.getWorld().getName(), location.getX(), location.getY(), location.getZ());
 }

 public String getWorldName() {
  return worldName;
 }

 public double getX() {
  return x;
 }

 public double getY() {
  return y;
 }

 public double getZ() {
  return z;
 }

 @Override
 public String toString() {
  return String.format("%s:%s:%s:%s", worldName, x, y, z);
 }
}