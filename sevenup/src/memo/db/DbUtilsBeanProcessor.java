/**
 * MyBeanProcessor.java 6:10:37 PM Jan 31, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package memo.db;

import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.lang.StringUtils;

/**
 * ��ݿ��ֶ�ת����bean���Ե����⴦��
 * 
 * @author dixingxing
 * @date Jan 31, 2012
 */
public class DbUtilsBeanProcessor extends BeanProcessor {
	/**
	 * ��ݿ����� -> java������
	 * 
	 * @param column
	 * @return
	 */
	private static String column2Prop(String column) {
		String[] strs = column.split("_");
		StringBuilder conventName = new StringBuilder();
		for (int i = 0; i < strs.length; i++) {
			conventName.append(StringUtils.capitalize(strs[i]));
		}
		return StringUtils.uncapitalize(conventName.toString());
	}

	/**
	 * java���� -> ��ݿ�����
	 * 
	 * @param prop
	 *            �������Ϊ�շ�������֧��l��}���д��ĸ
	 * @return
	 */
	public static String prop2column(String prop) {
		Pattern p = Pattern.compile("([A-Z])");
		Matcher m = p.matcher(prop);

		String result = prop;
		while (m.find()) {
			String s = m.group(1);
			result = result.replaceFirst(s, "_" + s.toLowerCase());
		}

		return result;
	}

	public static void main(String[] args) {
		System.out.println(prop2column("publishTimeAB"));
	}

	@Override
	protected int[] mapColumnsToProperties(ResultSetMetaData rsmd,
			PropertyDescriptor[] props) throws SQLException {

		int cols = rsmd.getColumnCount();
		int[] columnToProperty = new int[cols + 1];
		Arrays.fill(columnToProperty, PROPERTY_NOT_FOUND);

		for (int col = 1; col <= cols; col++) {
			String columnName = rsmd.getColumnLabel(col);
			if (null == columnName || 0 == columnName.length()) {
				columnName = rsmd.getColumnName(col);
			}
			columnName = column2Prop(columnName);
			for (int i = 0; i < props.length; i++) {

				if (columnName.equalsIgnoreCase(props[i].getName())) {
					columnToProperty[col] = i;
					break;
				}
			}
		}

		return columnToProperty;
	}

	@Override
	protected Object processColumn(ResultSet rs, int index, Class<?> propType)
			throws SQLException {
		if (!propType.isPrimitive() && rs.getObject(index) == null) {
			return null;
		}

		if (propType.equals(String.class)) {
			return rs.getString(index);

		} else if (propType.equals(Integer.TYPE)
				|| propType.equals(Integer.class)) {
			return Integer.valueOf(rs.getInt(index));

		} else if (propType.equals(Boolean.TYPE)
				|| propType.equals(Boolean.class)) {
			return Boolean.valueOf(rs.getBoolean(index));

		} else if (propType.equals(Long.TYPE) || propType.equals(Long.class)) {
			return Long.valueOf(rs.getLong(index));

		} else if (propType.equals(Double.TYPE)
				|| propType.equals(Double.class)) {
			return Double.valueOf(rs.getDouble(index));

		} else if (propType.equals(Float.TYPE) || propType.equals(Float.class)) {
			return Float.valueOf(rs.getFloat(index));

		} else if (propType.equals(Short.TYPE) || propType.equals(Short.class)) {
			return Short.valueOf(rs.getShort(index));

		} else if (propType.equals(Byte.TYPE) || propType.equals(Byte.class)) {
			return Byte.valueOf(rs.getByte(index));

		} else if (propType.equals(Timestamp.class)) {
			return rs.getTimestamp(index);
		}
		// ���date����
		else if (propType.equals(Date.class)) {
			return new Date(rs.getTimestamp(index).getTime());
		} else {
			return rs.getObject(index);
		}
	}

}
