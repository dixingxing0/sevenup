/**
 * BaseDao.java 7:24:12 PM Jan 17, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package memo.db;

import java.lang.reflect.ParameterizedType;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.log4j.Logger;


/**
 * dbutils dao ����
 * 
 * @author dixingxing
 * @date Jan 17, 2012
 */
public class Dao<T> {
	private final static Logger logger = Logger.getLogger(Dao.class);
	private final static String ERROR = "ִ��sql���";
	private static QueryRunner runner;

	protected final static DataSource ds;

	static {
		ds = initDataSource();
		runner = new QueryRunner(ds);
	}

	/**
	 * ��ʼ��dhcp���Դ
	 * 
	 * @return
	 */
	private static synchronized DataSource initDataSource() {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName("org.sqlite.JDBC");
//		ds.setUsername(Config.jdbc_user_name);
//		ds.setPassword(Config.jdbc_password);
		ds.setUrl("jdbc:sqlite://E:/swing/memo/test.db");
		// ds.setDriverClassName("oracle.jdbc.driver.OracleDriver");
		// ds.setUsername("newhc");
		// ds.setPassword("bfdds06fd");
		// ds.setUrl("jdbc:oracle:thin:@192.168.20.203:1521:mktdb4");
		return ds;
	}

	private final static ScalarHandler scaleHandler = new ScalarHandler() {
		@Override
		public Object handle(ResultSet rs) throws SQLException {
			Object obj = super.handle(rs);
			if (obj instanceof BigInteger)
				return ((BigInteger) obj).longValue();

			return obj;
		}
	};

	/**
	 * 
	 * ʹ���Զ���� MyBeanProcessor
	 * 
	 * @see DbUtilsBeanProcessor
	 * @param clazz
	 * @return
	 */
	private BeanListHandler<T> getBeanListHandler() {
		return new BeanListHandler<T>(poClass(), new BasicRowProcessor(
				new DbUtilsBeanProcessor()));
	}

	/**
	 * ʹ���Զ���� MyBeanProcessor
	 * 
	 * @see DbUtilsBeanProcessor
	 * @param clazz
	 * @return
	 */
	private BeanHandler<T> getBeanHandler() {
		return new BeanHandler<T>(poClass(), new BasicRowProcessor(
				new DbUtilsBeanProcessor()));
	}

	/**
	 * ��ȡ�������ж���ķ���
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Class<T> poClass() {
		// ʹ��cglib���?��ȡʵ������ΪgetSuperclass()
		return (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	/**
	 * ��Ĭ�ϵ����Դ�л�ȡһ����ݿ�l��,����setAutoCommit(false)
	 * 
	 * @return
	 */
	public static Connection getConn() {

		try {
			Connection conn = ds.getConnection();
//			conn.setAutoCommit(false);
			return conn;
		} catch (Exception e) {
			throw new RuntimeException("��ȡ��ݿ�l��ʧ��", e);
		}
	}

	/**
	 * 
	 * ��ѯ�����б�
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public List<T> queryList(String sql, Object... params) {
		logger.debug(new SqlHolder(sql, params));
		try {
			return (List<T>) runner.query(sql, getBeanListHandler(), params);
		} catch (SQLException e) {
			logger.error(ERROR, e);
			throw new RuntimeException(ERROR, e);
		}
	}

	/**
	 * ��ѯ���ص������
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public T query(String sql, Object... params) {
		logger.debug(new SqlHolder(sql, params));

		try {
			return (T) runner.query(sql, getBeanHandler(), params);
		} catch (SQLException e) {
			logger.error(ERROR, e);
			throw new RuntimeException(ERROR, e);
		}
	}

	/**
	 * ��ѯlong�����
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public Long queryLong(String sql, Object... params) {
		logger.debug(new SqlHolder(sql, params));
		try {
			Number n = (Number) runner.query(sql, scaleHandler, params);
			return n.longValue();
		} catch (SQLException e) {
			logger.error(ERROR, e);
			throw new RuntimeException(ERROR, e);
		}
	}

	/**
	 * ��ѯint�����
	 * 
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public Integer queryInt(String sql, Object... params) {
		logger.debug(new SqlHolder(sql, params));
		try {
			Number n = (Number) runner.query(sql, scaleHandler, params);
			return n.intValue();
		} catch (SQLException e) {
			logger.error(ERROR, e);
			throw new RuntimeException(ERROR, e);
		}
	}

	/**
	 * ִ��INSERT/UPDATE/DELETE���
	 * 
	 * @param conn
	 * @param sql
	 * @param params
	 * @return
	 */
	public int update(Connection conn, String sql, Object... params) {
		logger.debug(new SqlHolder(sql, params));
		try {
			return runner.update(conn, sql, params);
		} catch (SQLException e) {
			logger.error(ERROR, e);
			throw new RuntimeException(ERROR, e);
		}
	}

    public int update( String sql, Object... params) {
		logger.debug(new SqlHolder(sql, params));
		try {
			return runner.update(getConn(), sql, params);
		} catch (SQLException e) {
			logger.error(ERROR, e);
			throw new RuntimeException(ERROR, e);
		}
	}

	/**
	 * update
	 * 
	 * 
	 * @param conn
	 * @param po
	 * @return
	 */
	public int update(Connection conn, Object po) {
		SqlHolder holder = SqlBuilder.buildUpdate(po);
		return update(conn, holder.getSql(), holder.getParams());
	}

    /**
	 * update
	 *
	 *
	 * @param conn
	 * @param po
	 * @return
	 */
	public int update() {
		SqlHolder holder = SqlBuilder.buildUpdate(this);
		return update(getConn(), holder.getSql(), holder.getParams());
	}

	/**
	 * insert
	 * 
	 * @param conn
	 * @param po
	 * @return
	 */
	public int insert(Connection conn, Object po) {
		SqlHolder holder = SqlBuilder.buildInsert(po);
		return update(conn, holder.getSql(), holder.getParams());
	}
	/**
	 * insert
	 *
	 * @param conn
	 * @param po
	 * @return
	 */
	public int insert() {
		SqlHolder holder = SqlBuilder.buildInsert(this);
		return update(getConn(), holder.getSql(), holder.getParams());
	}
	/**
	 * 
	 * ��ѯ�б�
	 * 
	 * @param sql
	 * @return Map<String, Object>
	 */
	public List<Map<String, Object>> queryMapList(String sql) {
		logger.debug(sql);
		try {
			List<Map<String, Object>> results = (List<Map<String, Object>>) runner
					.query(sql, new MapListHandler());
			return results;
		} catch (SQLException e) {
			logger.error(ERROR, e);
			throw new RuntimeException(ERROR, e);
		}
	}

	public Page<T> queryPage(String sql, int currentPage, int pageSize) {
		Page<T> page = new Page<T>(sql, currentPage, pageSize);
		page.setTotalResult(queryInt(page.getCountSql()));
		page.setResult(queryList(page.getPageSql()));
		return page;
	}

	public static void rollbackAndClose(Connection conn) {
		logger.debug("�ع����񲢹ر�l��");
		DbUtils.rollbackAndCloseQuietly(conn);
	}

	public static void commitAndClose(Connection conn) {
		logger.debug("�ύ���񲢹ر�l��");
		DbUtils.commitAndCloseQuietly(conn);
	}


    
}
