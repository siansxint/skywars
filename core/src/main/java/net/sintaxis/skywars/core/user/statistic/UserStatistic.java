package net.sintaxis.skywars.core.user.statistic;

import net.sintaxis.skywars.api.statistic.IntegerStatistic;

public class UserStatistic {

 private final IntegerStatistic kills;
 private final IntegerStatistic deaths;
 private final IntegerStatistic wins;
 private final IntegerStatistic losses;

 public UserStatistic() {
  this.kills = new IntegerStatistic();
  this.deaths = new IntegerStatistic();
  this.wins = new IntegerStatistic();
  this.losses = new IntegerStatistic();
 }

 public IntegerStatistic getKills() {
  return kills;
 }

 public IntegerStatistic getDeaths() {
  return deaths;
 }

 public IntegerStatistic getWins() {
  return wins;
 }

 public IntegerStatistic getLosses() {
  return losses;
 }
}