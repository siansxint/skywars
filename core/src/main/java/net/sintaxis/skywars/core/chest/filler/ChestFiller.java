package net.sintaxis.skywars.core.chest.filler;

import net.sintaxis.skywars.core.coordinate.AbbreviatedCoordinates;
import net.sintaxis.skywars.core.chest.SkyChest;
import net.sintaxis.skywars.core.chest.item.ChestItem;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class ChestFiller {

 private static final Random RANDOM
         = new Random();

 public void fillChest(AbbreviatedCoordinates coordinates,
                       SkyChest skyChest
 ) {
  Block block = AbbreviatedCoordinates.toLocation(coordinates).getBlock();
  BlockState state = block.getState();

  if (!(state instanceof Chest)) {
   return;
  }

  Chest chest = (Chest) state;
  Inventory inventory = chest.getInventory();

  int[] freeSlots = new int[inventory.getSize()];
  for (int i = 0; i < freeSlots.length; i++) {
   freeSlots[i] = i;
  }

  int notUsedFreeSlots = freeSlots.length;

  for (ChestItem item : skyChest.getContents()) {

   if (notUsedFreeSlots <= 0) {
    break;
   } else if (RANDOM.nextInt(100) > item.getProbability()) {
    continue;
   }

   int slotIndex = RANDOM.nextInt(notUsedFreeSlots);
   int slot = freeSlots[slotIndex];
   freeSlots[slotIndex] = freeSlots[--notUsedFreeSlots];

   int amount = 1;
   if (item.getMinAmount() != item.getMaxAmount() && item.getMinAmount() >= 1 && item.getMaxAmount() > 1) {
    amount = RANDOM.nextInt(item.getMaxAmount() - item.getMinAmount()) + item.getMinAmount();
   }

   ItemStack itemStack = item.getItem().clone();
   itemStack.setAmount(amount);

   inventory.setItem(slot, itemStack);
  }

  state.update();
 }
}