package net.sintaxis.skywars.api.model;

public interface Service {

 void start();

 default void stop() {}
}