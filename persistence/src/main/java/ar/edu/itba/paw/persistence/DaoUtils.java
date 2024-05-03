package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.files.File;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class DaoUtils {
    private DaoUtils(){};

    static int getRowCount(JdbcTemplate jdbcTemplate, String tableName, String conditions, Object ... params){
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM " + tableName + " " + conditions,
                Integer.class,
                params
        );
        return count!=null? count: 0; //para evitar que intellij tire un warning
    }

    static int getRowCount(JdbcTemplate jdbcTemplate, String tableName){
        return getRowCount(jdbcTemplate, tableName, "");
    }

    static void addQueryCondition(StringBuilder query, List<Object> paramList, String condition, Object param){
        if (param != null){
            paramList.add(param);
            query.append(condition);
        }
    }

}
