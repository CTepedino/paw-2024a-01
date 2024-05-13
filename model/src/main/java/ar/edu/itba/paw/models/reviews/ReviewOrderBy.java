package ar.edu.itba.paw.models.reviews;

public enum ReviewOrderBy {
    RATING_ASC("rating ASC"),
    RATING_DESC("rating DESC"),
    DATE_DESC("date DESC");

    private final String columnName;

    ReviewOrderBy(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName(){
        return columnName;
    }
}
