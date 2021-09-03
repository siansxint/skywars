package net.sintaxis.skywars.core.event;

import net.sintaxis.skywars.core.user.User;

import org.bukkit.event.Event;

public abstract class UserEvent
        extends Event {

 private final User user;

 public UserEvent(User user) {
  this.user = user;
 }

 public User getUser() {
  return user;
 }
}