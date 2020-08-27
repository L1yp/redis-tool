package com.l1yp.types;

import com.l1yp.enums.BaseEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Lyp
 * @Date   2020-08-27
 * @Email  l1yp@qq.com
 */
public class EnumCodeHandlers<E extends Enum<E> & BaseEnum> extends BaseTypeHandler<E> {

    private final Class<E> clazz;
    private final Map<Integer, E> elems;

    public EnumCodeHandlers(Class<E> clazz) {
        this.clazz = clazz;
        E[] cs = clazz.getEnumConstants();
        elems = new HashMap<>(Math.max((int) (cs.length / .75f) + 1, 16));
        for (E e : cs) {
            elems.put(e.getCode(), e);
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getCode());
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Integer code = rs.getObject(columnName, Integer.class);
        return code == null ? null : elems.get(code);
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Integer code = rs.getObject(columnIndex, Integer.class);
        return code == null ? null : elems.get(code);
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Integer code = cs.getObject(columnIndex, Integer.class);
        return code == null ? null : elems.get(code);
    }
}
