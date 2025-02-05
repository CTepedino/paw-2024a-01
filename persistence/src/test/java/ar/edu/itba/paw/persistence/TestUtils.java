package ar.edu.itba.paw.persistence;

import javax.persistence.EntityManager;
import java.math.BigInteger;

class TestUtils {

    private TestUtils(){}

    static long getRowCount(EntityManager em, String query){
        return ((BigInteger) em.createNativeQuery("SELECT COUNT(*) " + query).getSingleResult()).longValue();
    }
}
