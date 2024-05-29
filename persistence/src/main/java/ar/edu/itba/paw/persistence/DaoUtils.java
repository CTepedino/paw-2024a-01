package ar.edu.itba.paw.persistence;

public class DaoUtils {

    private DaoUtils(){}

    static String escapeSearchString(String search){
        if (search == null) return "";
        return search.replaceAll("[%_]", "\\\\$0");
    }
}
