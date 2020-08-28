package com.l1yp.types;

import com.l1yp.enums.EnumBase;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Author Lyp
 * @Date   2020-08-27
 * @Email  l1yp@qq.com
 */
public class EnumCodeHandlers<E extends Enum<E>> extends BaseTypeHandler<E> {

    private final Class<E> clazz;
    private E[] elems;
    private Map<Integer, E> codeMap;
    private boolean isEnumBase;
    private final static Set<Integer> numberic = new HashSet<>(Arrays.asList(
            Types.TINYINT,
            Types.SMALLINT,
            Types.INTEGER,
            Types.BIGINT
    ));

    public EnumCodeHandlers(Class<E> clazz) {
        this.clazz = clazz;
        elems = clazz.getEnumConstants();
        if (EnumBase.class.isAssignableFrom(clazz)){
            codeMap = new HashMap<>(Math.max((int) (elems.length / .75f) + 1, 16));
            for (E e : elems) {
                codeMap.put(((EnumBase) e).getCode(), e);
            }
            isEnumBase = true;
        }

    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        if (isEnumBase){
            ps.setInt(i, ((EnumBase) parameter).getCode());
        }else {
            ps.setString(i, parameter.name());
        }
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        if (isEnumBase){
            Integer code = rs.getObject(columnName, Integer.class);
            return code == null ? null : codeMap.get(code);
        } else {
            String name = rs.getString(columnName);
            return Enum.valueOf(this.clazz, name);
        }
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {

        if (isEnumBase) {
            Integer code = rs.getObject(columnIndex, Integer.class);
            return code == null ? null : codeMap.get(code);
        } else {
            String name = rs.getString(columnIndex);
            return Enum.valueOf(this.clazz, name);
        }
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        if (isEnumBase){
            Integer code = cs.getObject(columnIndex, Integer.class);
            return code == null ? null : codeMap.get(code);
        } else {
            String name = cs.getString(columnIndex);
            return Enum.valueOf(this.clazz, name);
        }
    }
}
