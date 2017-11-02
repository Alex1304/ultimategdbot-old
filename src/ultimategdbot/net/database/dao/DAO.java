package ultimategdbot.net.database.dao;

import java.util.List;

public interface DAO<T> {
	
	boolean insert(T obj);
	boolean update(T obj);
	boolean delete(T obj);
	T find(long id);
	List<T> findAll();
	
}
