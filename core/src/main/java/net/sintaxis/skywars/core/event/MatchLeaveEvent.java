package net.sintaxis.skywars.core.event;

import net.sintaxis.skywars.core.user.User;

import org.bukkit.event.HandlerList;

public class MatchLeaveEvent
        extends UserEvent {

 private static final HandlerList HANDLER_LIST
         = new HandlerList();

 private final Cause cause;

 public MatchLeaveEvent(User user,
                        Cause cause
 ) {
  super(user);
  this.cause = cause;
 }

 public static HandlerList getHandlerList() {
  return HANDLER_LIST;
 }

 public Cause getCause() {
  return cause;
 }

 @Override
 public HandlerList getHandlers() {
  return HANDLER_LIST;
 }

 public enum Cause {
  DISCONNECT,
  COMMAND,
  END_OF_MATCH
 }
}