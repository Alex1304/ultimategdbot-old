package ultimategdbot.net.database.dao;

import ultimategdbot.net.database.util.SQLCountQueryExecutor;
import ultimategdbot.net.database.util.SQLQueryExecutor;
import ultimategdbot.net.database.util.SQLUpdateExecutor;

/**
 * DAO for relational databases using JDBC Provides additional default methods,
 * nothing else to implement.
 * 
 * @author Alex1304
 *
 */
public interface RelationalDAO<T> extends DAO<T>, SQLUpdateExecutor, SQLQueryExecutor<T>,
		SQLCountQueryExecutor {
}