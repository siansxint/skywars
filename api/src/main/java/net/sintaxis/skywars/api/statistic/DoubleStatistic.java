package net.sintaxis.skywars.api.statistic;

public class DoubleStatistic {

 private double quantity;

 public void add(double value) {
  quantity += value;
 }

 public double get() {
  return quantity;
 }

 public boolean has(double value) {
  return quantity >= value;
 }

 public void set(double value) {
  this.quantity = value;
 }
}