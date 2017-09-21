package ultimategdbot.net.database.dao;

public interface DAO<T> {
	
	public void insert(T obj);
	public void update(T obj);
	public void delete(T obj);
	public T find(long id);
	
}
