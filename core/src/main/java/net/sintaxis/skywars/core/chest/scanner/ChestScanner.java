package net.sintaxis.skywars.core.chest.scanner;

import net.sintaxis.skywars.core.coordinate.AbbreviatedCoordinates;
import net.sintaxis.skywars.core.match.Match;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;

import java.util.Iterator;

public class ChestScanner {

 /*
 I know that is not efficient, but is not an operation that will
 use frequently, only in the setup
  */
 public void scanAndSetupMatchChests(Match match) {

  match.getChests().clear();

  Iterator<Block> blockIterator = match.getZone().iterator();

  while (blockIterator.hasNext()) {
   Block block = blockIterator.next();

   BlockState state = block.getState();

   if (!(state instanceof Chest)) {
    continue;
   }

   match.getChests().add(new AbbreviatedCoordinates(
           block.getWorld().getName(),
           block.getX(),
           block.getY(),
           block.getZ()));
  }
 }
}