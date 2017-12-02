package ultimategdbot.net.database.dao;

import java.util.List;

/**
 * Classes that manages interactions with database should implement
 * this interface. One implementation of this interface is supposed
 * to manipulate one entity.
 * 
 * @author Alex1304
 *
 * @param <T> - The type of the entity manipulated by the DAO
 */
public interface DAO<T> {
	
	/**
	 * Inserts a new entry into the database
	 * 
	 * @param obj - the entity to insert
	 * @return true if it has been inserted successfully, false otherwise
	 */
	boolean insert(T obj);

	/**
	 * Updates an entry of the database
	 * 
	 * @param obj - the entity to update
	 * @return true if it has been updated successfully, false otherwise
	 */
	boolean update(T obj);
	
	/**
	 * Deletes an entry from the database
	 * 
	 * @param obj - the entity to delete
	 * @return true if it has been deleted successfully, false otherwise
	 */
	boolean delete(T obj);

	/**
	 * Finds an entity by its primary key of type long.
	 * 
	 * @param id - the long ID of the entity to find
	 * @return an instance of the entity found, or null if it couldn't be found.
	 */
	T find(long id);
	
	/**
	 * Finds all entities from the database and stores the result into a list.
	 * 
	 * @return the list of all entity instances found.
	 */
	List<T> findAll();
	
}
