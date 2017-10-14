package ultimategdbot.net.database.dao;

public interface DAO<T> {
	
	boolean insert(T obj);
	boolean update(T obj);
	boolean delete(T obj);
	T find(long id);
	
}
