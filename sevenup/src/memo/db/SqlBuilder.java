/**
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package memo.db;


import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Date;

import memo.db.annotation.Column;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import memo.db.annotation.Table;
import memo.db.annotation.Transient;

/**
 * ���pojo����SqlHolder <br />
 * ���oracle��sql
 * 
 * @see SqlHolder
 * @author dixingxing
 * @date Feb 6, 2012
 */
public class SqlBuilder {
//	private final static Logger logger = Logger.getLogger(SqlBuilder.class);
	private final static String ID = "id";

	/**
	 * ����insert
	 * 
	 * @param po
	 * @return
	 */
	public static SqlHolder buildInsert(Object po) {
		SqlHolder holder = new SqlHolder();
		Field[] fields = ReflectUtils.getVariableFields(po.getClass());

		StringBuilder columns = new StringBuilder();
		StringBuilder values = new StringBuilder();

		for (Field f : fields) {
			if (isTransient(f)) {
				continue;
			}
			holder.addParam(convert(ReflectUtils.get(po, f)));
			columns.append(columnName(f)).append(",");
			values.append("?").append(",");

		}
		deleteLastComma(columns);
		deleteLastComma(values);

		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ").append(tableName(po)).append(" (");
		sql.append(columns).append(") ");
		sql.append(" VALUES(").append(values).append(") ");
		holder.setSql(sql.toString());
//		logger.debug(holder);
		return holder;

	}

	/**
	 * ������� where id= po.getId();
	 * 
	 * 
	 * @param po
	 * @return
	 */
	public static SqlHolder buildUpdate(Object po) {
		return buildUpdate(po, "id=" + ReflectUtils.getValueByFieldName(po, ID));
	}

	/**
	 * ��sql��װ�ɷ�ҳ��sql
	 * 
	 * @param sql
	 *            ����Ĳ�ѯ���
	 * @param start
	 * @param end
	 * @return
	 */
	public static String pageSql(String sql, int start, int end) {
		StringBuilder pageSql = new StringBuilder();
//		if (Config.isMysql()) {
//			pageSql.append(sql).append(" limit ").append(start).append(",")
//					.append(end);
//		} else if (Config.isOracle()) {
//			pageSql
//					.append("select * from (select tmp_tb.*,ROWNUM row_id from (");
//			pageSql.append(sql);
//			pageSql.append(")  tmp_tb where ROWNUM<=");
//			pageSql.append(end);
//			pageSql.append(") where row_id>");
//			pageSql.append(start);
//		}

		return pageSql.toString();
	}

	/**
	 * 
	 * ��sqlת���ɲ�ѯ�����sql
	 * 
	 * @param sql
	 *            ����Ĳ�ѯ���
	 * @return
	 */
	public static String countSql(String sql) {
		if (StringUtils.isBlank(sql)) {
			return sql;
		}
		return sql.replaceFirst("select .* from", "select count(1) from");
	}

	/**
	 * ����update
	 * 
	 * @param po
	 * @param where
	 *            ������Ϊ��
	 * @return
	 */
	private static SqlHolder buildUpdate(Object po, String where) {
		SqlHolder holder = new SqlHolder();
		Field[] fields = ReflectUtils.getVariableFields(po.getClass());

		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ").append(tableName(po)).append(" SET ");

		for (Field f : fields) {
			if (isTransient(f) || !isUpdatable(f)) {
				continue;
			}
			holder.addParam(convert(ReflectUtils.get(po, f)));
			sql.append(columnName(f)).append("=?").append(",");

		}
		deleteLastComma(sql);
		sql.append(" WHERE ").append(where);
		holder.setSql(sql.toString());
//		logger.debug(holder);
		return holder;

	}

	/**
	 * ɾ������Ǹ�,��
	 * 
	 * @param sb
	 */
	private static void deleteLastComma(StringBuilder sb) {
		if (sb.lastIndexOf(",") == sb.length() - 1) {
			sb.deleteCharAt(sb.length() - 1);
		}
	}

	/**
	 * ��ȡ����<br/>
	 * MyBeanProcessor�ж����˲�ѯʱ����ݿ��ֶ�ת -> po���� �Ĺ���,<br />
	 * �˴�po���� -> ��ݿ��ֶ� �Ĺ����ǰ�汣��һ��
	 * 
	 * @see DbUtilsBeanProcessor#prop2column(String)
	 * @param f
	 * @return
	 */
	private static String columnName(Field f) {
		return DbUtilsBeanProcessor.prop2column(f.getName());
	}

	/**
	 * 
	 * �����Ƿ�����޸�
	 * 
	 * @param obj
	 * @return
	 */
	private static boolean isUpdatable(Field f) {
		// id �����޸�
		if (ID.equals(f.getName())) {
			return false;
		}
		Column c = f.getAnnotation(Column.class);
		return c == null ? true : c.updatable();
	}

	/**
	 * 
	 * �����Ƿ���Ҫ�־û�
	 * 
	 * @param obj
	 * @return
	 */
	private static boolean isTransient(Field f) {
		Transient t = f.getAnnotation(Transient.class);
		if (t != null && t.value() == true) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * ��ȡ����
	 * 
	 * @param obj
	 * @return
	 */
	private static String tableName(Object obj) {
		String tableName = obj.getClass().getSimpleName();
		Table table = obj.getClass().getAnnotation(Table.class);
		if (table != null && StringUtils.isNotEmpty(table.name())) {
			tableName = table.name();
		}
		return tableName;
	}

	/**
	 * 
	 * java����ת������ݿ�����
	 * 
	 * @param o
	 * @return
	 */
	private static Object convert(Object o) {
		if (o == null) {
			return null;
		}
		if (o instanceof Date) {
			Date date = (Date) o;
			Timestamp t = new Timestamp(date.getTime());
			return t;
		}
		return o;
	}

	// public static void main(String[] args) {
	// Ad ad = new Ad();
	// ad.setId(101L);
	// ad.setName("123");
	// ad.setPosition(1L);
	// ad.setState(1L);
	// ad.setPublishTime(new Date());
	// ad.setPublishMan("publishtest");
	// ad.setUpdateTime(new Date());
	// ad.setUpdateMan("updatetest");
	// ad.setDetail("detail");
	// ad.setProviderId(100001790735L);
	// buildUpdate(ad, null);
	// }

}
