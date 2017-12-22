package ultimategdbot.net.database.dao;

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
	 * @return the number of rows inserted
	 */
	int insert(T obj);

	/**
	 * Updates an entry of the database
	 * 
	 * @param obj - the entity to update
	 * @return the number of rows updated
	 */
	int update(T obj);
	
	/**
	 * Deletes an entry from the database
	 * 
	 * @param obj - the entity to delete
	 * @return the number of rows deleted
	 */
	int delete(T obj);

	/**
	 * Finds an entity by its primary key of type long.
	 * 
	 * @param id - the long ID of the entity to find
	 * @return an instance of the entity found, or null if it couldn't be found.
	 */
	T find(long id);
	
}
