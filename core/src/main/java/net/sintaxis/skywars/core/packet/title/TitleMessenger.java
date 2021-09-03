package net.sintaxis.skywars.core.packet.title;

import me.clip.placeholderapi.PlaceholderAPI;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;

import net.sintaxis.skywars.core.packet.Packets;

import org.bukkit.entity.Player;

public class TitleMessenger {

 public void sendTitle(Player player,
                       String title,
                       int enter, int in, int out
 ) {
  PacketPlayOutTitle packetPlayOutTitle = new PacketPlayOutTitle(
          PacketPlayOutTitle.EnumTitleAction.TITLE,
          IChatBaseComponent.ChatSerializer.a(
                  ComponentSerializer.toString(
                          TextComponent.fromLegacyText(PlaceholderAPI.setPlaceholders(player, title))
                  )
          ), enter, in, out);

  Packets.sendPacket(player, packetPlayOutTitle);
 }

 public void sendSubTitle(Player player,
                          String subTitle,
                          int enter, int in, int out
 ) {
  PacketPlayOutTitle packetPlayOutTitle = new PacketPlayOutTitle(
          PacketPlayOutTitle.EnumTitleAction.SUBTITLE,
          IChatBaseComponent.ChatSerializer.a(
                  ComponentSerializer.toString(
                          TextComponent.fromLegacyText(PlaceholderAPI.setPlaceholders(player, subTitle))
                  )
          ), enter, in, out);

  Packets.sendPacket(player, packetPlayOutTitle);
 }

 public void sendBoth(Player player,
                      String title,
                      String subTitle,
                      int enter, int in, int out
 ) {
  sendTitle(player, title, enter, in, out);
  sendSubTitle(player, subTitle, enter, in, out);
 }
}