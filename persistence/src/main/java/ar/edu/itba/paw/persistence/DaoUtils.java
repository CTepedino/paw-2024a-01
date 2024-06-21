package ar.edu.itba.paw.persistence;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DaoUtils {

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

    static long getRowCount(EntityManager em,String tableName, String countedRow, String conditions, Map<String, Object> params){

        Query query = em.createQuery(
           "SELECT COUNT(DISTINCT " + countedRow + " ) FROM " + tableName + " " + conditions
        );
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return (Long) query.getSingleResult();
    }

    static long getRowCount(EntityManager em, String tableName, String countedRow){
        return getRowCount(em, tableName, countedRow,"", Map.of());
    }


    static <T> List<T> paginatedQuery(EntityManager em, Query nativeQuery, TypedQuery<T> query, int offset, int limit){

        nativeQuery.setFirstResult(offset);
        nativeQuery.setMaxResults(limit);


        @SuppressWarnings("unchecked")
        final List<Long> idList = (List<Long>) nativeQuery.getResultStream().map(n -> (Long)((Number)n).longValue()).collect(Collectors.toList());

        if (idList.isEmpty()){
            return Collections.emptyList();
        }

        query.setParameter("idList", idList);
        return query.getResultList();
    }
}
