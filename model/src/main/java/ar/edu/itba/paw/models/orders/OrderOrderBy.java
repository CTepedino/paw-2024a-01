package ar.edu.itba.paw.models.orders;

//TODO: think of a better name
public enum OrderOrderBy {
    DATE_ASC("date ASC"),
    DATE_DESC("date DESC");

    private String columnName;

    OrderOrderBy(String columnName){
        this.columnName = columnName;
    }

    public String getColumnName(){
        return columnName;
    }
}
