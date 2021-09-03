package net.sintaxis.skywars.api.processor;

/**
 * A simple functional interface representing the process of the generic object
 *
 * @param <O> The object to process
 */

@FunctionalInterface
public interface Processor<O> {

 /**
  * The unique method used to process the object
  *
  * @param object The object to process
  */

 void process(O object);
}