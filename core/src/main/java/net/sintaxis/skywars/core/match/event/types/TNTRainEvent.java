package net.sintaxis.skywars.core.match.event.types;

import com.fasterxml.jackson.annotation.JsonIgnore;

import net.sintaxis.skywars.core.SkyWars;
import net.sintaxis.skywars.core.match.Match;
import net.sintaxis.skywars.core.user.finder.UserFinder;
import net.sintaxis.skywars.core.util.CountdownTimerUtil;
import net.sintaxis.skywars.core.match.event.MatchEvent;
import net.sintaxis.skywars.core.user.state.UserState;

import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;

public class TNTRainEvent
        implements MatchEvent {

 @JsonIgnore
 private final int defaultTimeUntilNextEvent;
 @JsonIgnore
 private final UserFinder userFinder;
 @JsonIgnore
 private final SkyWars skyWars;
 private int timeUntilNextEvent;

 public TNTRainEvent(int timeUntilNextEvent,
                     UserFinder userFinder,
                     SkyWars skyWars
 ) {
  this.timeUntilNextEvent = timeUntilNextEvent;
  this.defaultTimeUntilNextEvent = timeUntilNextEvent;
  this.userFinder = userFinder;
  this.skyWars = skyWars;
 }

 @Override
 public int getTimeUntilNextEvent() {
  return timeUntilNextEvent;
 }

 @Override
 public EventType getType() {
  return EventType.TNT_RAIN;
 }

 @Override
 public void tick() {
  timeUntilNextEvent--;
 }

 @Override
 public void start(Match match) {
  CountdownTimerUtil countdownTimerUtil = new CountdownTimerUtil(
          skyWars,
          10,
          (ignored) -> {
           for (Player player : userFinder.getMatchPlayersByFilter(match, user -> user.getTemporally().getRole() == UserState.ALIVE)) {
            TNTPrimed tntPrimed = player.getLocation().getWorld().spawn(player.getLocation().add(0, 10, 0), TNTPrimed.class);
            tntPrimed.setFuseTicks(40);
           }
          }
  );
  countdownTimerUtil.scheduleTimer();
 }

 @Override
 public void resetTime() {
  this.timeUntilNextEvent = defaultTimeUntilNextEvent;
 }
}