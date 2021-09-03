package net.sintaxis.skywars.core.cage;

import net.sintaxis.skywars.api.model.Savable;
import org.bukkit.inventory.ItemStack;

public class Cage
        implements Savable {

 private final String name;

 private final ItemStack ceiling;
 private final ItemStack walls;
 private final ItemStack floor;

 private final boolean overlappingCorners;
 private final boolean pointingCorners;

 public Cage(String name,
             ItemStack ceiling,
             ItemStack walls,
             ItemStack floor,
             boolean overlappingCorners,
             boolean pointingCorners
 ) {
  this.name = name;
  this.ceiling = ceiling;
  this.walls = walls;
  this.floor = floor;
  this.overlappingCorners = overlappingCorners;
  this.pointingCorners = pointingCorners;
 }

 @Override
 public String getId() {
  return name;
 }

 public ItemStack getCeiling() {
  return ceiling;
 }

 public ItemStack getWalls() {
  return walls;
 }

 public ItemStack getFloor() {
  return floor;
 }

 public boolean isOverlappingCorners() {
  return overlappingCorners;
 }

 public boolean hasPointedCorners() {
  return pointingCorners;
 }
}