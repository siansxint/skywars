package net.sintaxis.skywars.core.event;

import net.sintaxis.skywars.core.match.Match;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MatchEndEvent
        extends Event
        implements Cancellable {

 private static final HandlerList HANDLER_LIST
         = new HandlerList();

 private final Match match;
 private boolean cancelled;

 public MatchEndEvent(Match match) {
  this.match = match;
 }

 public static HandlerList getHandlerList() {
  return HANDLER_LIST;
 }

 public Match getMatch() {
  return match;
 }

 @Override
 public HandlerList getHandlers() {
  return HANDLER_LIST;
 }

 @Override
 public boolean isCancelled() {
  return cancelled;
 }

 @Override
 public void setCancelled(boolean cancelled) {
  this.cancelled = cancelled;
 }
}