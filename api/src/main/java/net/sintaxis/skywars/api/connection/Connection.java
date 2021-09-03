package net.sintaxis.skywars.api.connection;

/**
 * Template to generate connection.
 *
 * @param <T> The connection type
 */

public interface Connection<T> {

 /**
  * Get the connection instance created after connect
  *
  * @return The connection to the database
  */

 T getConnection();

 /**
  * Used to close the connection with the database instance
  */

 void close();
}