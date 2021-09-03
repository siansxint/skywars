package net.sintaxis.skywars.core.match.info.timing;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.beans.ConstructorProperties;

public class TimedMatchInfo {

 private final int defaultWaitingTime;
 private final int defaultStartingTime;
 private int waitingTime;
 private int startingTime;

 @ConstructorProperties({
         "waitingTime",
         "startingTime"
 })
 public TimedMatchInfo(int waitingTime,
                       int startingTime
 ) {
  this.waitingTime = waitingTime;
  this.defaultWaitingTime = waitingTime;
  this.startingTime = startingTime;
  this.defaultStartingTime = startingTime;
 }

 public int getWaitingTime() {
  return waitingTime;
 }

 public void setWaitingTime(int waitingTime) {
  this.waitingTime = waitingTime;
 }

 @JsonIgnore
 public int getDefaultWaitingTime() {
  return defaultWaitingTime;
 }

 public int getStartingTime() {
  return startingTime;
 }

 public void setStartingTime(int startingTime) {
  this.startingTime = startingTime;
 }

 @JsonIgnore
 public int getDefaultStartingTime() {
  return defaultStartingTime;
 }
}