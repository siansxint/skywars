package net.sintaxis.skywars.api.statistic;

public class IntegerStatistic {

 private int quantity;

 public void add(int value) {
  quantity += value;
 }

 public int get() {
  return quantity;
 }

 public boolean has(int value) {
  return quantity >= value;
 }

 public void set(int value) {
  this.quantity = value;
 }
}