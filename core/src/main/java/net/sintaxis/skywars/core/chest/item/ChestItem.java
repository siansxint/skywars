package net.sintaxis.skywars.core.chest.item;

import org.bukkit.inventory.ItemStack;

public class ChestItem {

 private final int probability;
 private final int minAmount;
 private final int maxAmount;
 private final ItemStack item;

 public ChestItem(int probability,
                  int minAmount,
                  int maxAmount,
                  ItemStack item
 ) {
  this.probability = probability;
  this.minAmount = minAmount;
  this.maxAmount = maxAmount;
  this.item = item;
 }

 public int getProbability() {
  return probability;
 }

 public int getMinAmount() {
  return minAmount;
 }

 public int getMaxAmount() {
  return maxAmount;
 }

 public ItemStack getItem() {
  return item;
 }
}