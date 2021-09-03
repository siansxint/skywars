package net.sintaxis.skywars.core.packet;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PlayerConnection;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public interface Packets {

 static CraftPlayer getCraftedPlayer(Player player) {
  return (CraftPlayer) player;
 }

 static PlayerConnection getPlayerConnection(Player player) {
  return getCraftedPlayer(player).getHandle().playerConnection;
 }

 static void sendPacket(Player player, Packet<?> packet) {
  getPlayerConnection(player).sendPacket(packet);
 }
}