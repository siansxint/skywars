package net.sintaxis.skywars.core.event;

import net.sintaxis.skywars.core.match.Match;
import net.sintaxis.skywars.core.user.User;

import org.bukkit.event.HandlerList;

public class MatchJoinEvent
        extends UserEvent {

 private static final HandlerList HANDLER_LIST
         = new HandlerList();

 private final Match match;

 public MatchJoinEvent(User user,
                       Match match
 ) {
  super(user);
  this.match = match;
 }

 public static HandlerList getHandlerList() {
  return HANDLER_LIST;
 }

 public Match getRequestedMatch() {
  return match;
 }

 @Override
 public HandlerList getHandlers() {
  return HANDLER_LIST;
 }
}