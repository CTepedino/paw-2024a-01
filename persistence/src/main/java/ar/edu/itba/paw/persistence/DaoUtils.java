package ar.edu.itba.paw.persistence;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.Map;

public class DaoUtils {

    static final String FIRST_CONDITION = " ";
    static final String AND = " AND ";


    private DaoUtils(){}

    static String prepareSearchString(String search){
        if (search == null) return "%";
        return "%" + search.replaceAll("[%_]", "\\\\$0") + "%";
    }

    static void addQueryCondition(StringBuilder query, String condition, Map<String, Object> params, String paramName, Object param){
        if (param != null){
            query.append(condition);
            params.put(paramName, param);
        }
    }

    static long getRowCount(EntityManager em, String tableName, String conditions, Map<String, Object> params){

        Query query = em.createNativeQuery(
           "SELECT COUNT(*) FROM " + tableName + " " + conditions
        );
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return ((BigInteger) query.getSingleResult()).longValue();
    }

    static long getRowCount(EntityManager em, String tableName){
        return getRowCount(em, tableName, "", Map.of());
    }

}
