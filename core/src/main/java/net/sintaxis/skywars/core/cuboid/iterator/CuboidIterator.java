package net.sintaxis.skywars.core.cuboid.iterator;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.Iterator;

public class CuboidIterator
        implements Iterator<Block> {

 private final String worldName;
 private final int baseX;
 private final int baseY;
 private final int baseZ;
 private final int sizeX;
 private final int sizeY;
 private final int sizeZ;
 private int x, y, z;

 public CuboidIterator(String worldName,
                       int x1,
                       int y1,
                       int z1,
                       int x2,
                       int y2,
                       int z2) {
  this.worldName = worldName;
  this.baseX = x1;
  this.baseY = y1;
  this.baseZ = z1;
  this.sizeX = Math.abs(x2 - x1) + 1;
  this.sizeY = Math.abs(y2 - y1) + 1;
  this.sizeZ = Math.abs(z2 - z1) + 1;
  this.x = this.y = this.z = 0;
 }

 public boolean hasNext() {
  return x < sizeX && y < sizeY && z < sizeZ;
 }

 public Block next() {
  World world = Bukkit.getWorld(worldName);
  Block block = world.getBlockAt(baseX + x, baseY + y, baseZ + z);
  if (++x >= sizeX) {
   x = 0;
   if (++y >= sizeY) {
    y = 0;
    ++z;
   }
  }
  return block;
 }

 public void remove() {
 }
}