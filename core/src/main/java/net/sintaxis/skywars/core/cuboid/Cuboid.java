package net.sintaxis.skywars.core.cuboid;

import com.fasterxml.jackson.annotation.JsonIgnore;

import net.sintaxis.skywars.core.coordinate.AbbreviatedCoordinates;
import net.sintaxis.skywars.core.cuboid.iterator.CuboidIterator;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.beans.ConstructorProperties;

import java.util.Iterator;

public class Cuboid {

 private final AbbreviatedCoordinates lowerBound;
 private final AbbreviatedCoordinates upperBound;

 @ConstructorProperties({
         "lowerBound",
         "upperBound"
 })
 public Cuboid(AbbreviatedCoordinates lowerBound,
               AbbreviatedCoordinates upperBound) {
  this.lowerBound = lowerBound;
  this.upperBound = upperBound;
 }

 public AbbreviatedCoordinates getLowerBound() {
  return this.lowerBound;
 }

 public AbbreviatedCoordinates getUpperBound() {
  return this.upperBound;
 }

 @JsonIgnore
 public AbbreviatedCoordinates getCenter() {
  int x1 = (int) (this.upperBound.getX() + 1);
  int y1 = (int) (this.upperBound.getY() + 1);
  int z1 = (int) (this.upperBound.getZ() + 1);
  return new AbbreviatedCoordinates(this.lowerBound.getWorldName(), this.lowerBound.getX() + (x1 - this.lowerBound.getX()) / 2.0, this.lowerBound.getY() + (y1 - this.lowerBound.getY()) / 2.0, this.lowerBound.getZ() + (z1 - this.lowerBound.getZ()) / 2.0);
 }

 public boolean contains(int x, int y, int z) {
  return x >= this.lowerBound.getX() && x <= this.upperBound.getX() && y >= this.lowerBound.getY() && y <= this.upperBound.getY() && z >= this.lowerBound.getZ() && z <= this.upperBound.getZ();
 }

 public boolean contains(Location location) {
  if (!this.lowerBound.getWorldName().equals(location.getWorld().getName())) {
   return false;
  }
  return this.contains(location.getBlockX(), location.getBlockY(), location.getBlockZ());
 }

 @JsonIgnore
 public int getSizeX() {
  return (int) ((this.upperBound.getX() - this.lowerBound.getX()) + 1);
 }

 @JsonIgnore
 public int getSizeZ() {
  return (int) ((this.upperBound.getZ() - this.lowerBound.getZ()) + 1);
 }

 @JsonIgnore
 public Iterator<Block> iterator() {
  return new CuboidIterator(this.lowerBound.getWorldName(), (int) this.lowerBound.getX(), (int) this.lowerBound.getY(), (int) this.lowerBound.getZ(), (int) this.upperBound.getX(), (int) this.upperBound.getY(), (int) this.upperBound.getZ());
 }
}