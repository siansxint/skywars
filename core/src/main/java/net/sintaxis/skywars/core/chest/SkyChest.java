package net.sintaxis.skywars.core.chest;

import net.sintaxis.skywars.api.model.Savable;
import net.sintaxis.skywars.core.chest.item.ChestItem;

import java.util.Set;

public class SkyChest
        implements Savable {

 private final String type;
 private final Set<ChestItem> contents;

 public SkyChest(String type,
                 Set<ChestItem> contents
 ) {
  this.type = type;
  this.contents = contents;
 }

 @Override
 public String getId() {
  return type;
 }

 public Set<ChestItem> getContents() {
  return contents;
 }
}