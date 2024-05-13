package ar.edu.itba.paw.persistence;

import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;

public class DaoUtils {
    private DaoUtils(){};



    static long getRowCount(JdbcTemplate jdbcTemplate, String tableName, String conditions, Object ... params){
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM " + tableName + " " + conditions,
                Integer.class,
                params
        );
        return count!=null? count: 0; //para evitar que intellij tire un warning
    }

    static long getRowCount(JdbcTemplate jdbcTemplate, String tableName){
        return getRowCount(jdbcTemplate, tableName, "");
    }

    static void addQueryCondition(StringBuilder query, List<Object> paramList, String condition, Object param){
        if (param != null){
            paramList.add(param);
            query.append(condition);
        }
    }

    static String escapeSearchString(String search){
        return search.replaceAll("[%_]", "\\\\$0");
    }
}
