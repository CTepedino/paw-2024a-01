package ar.edu.itba.paw.models.books;

public enum BookGenre {
    FICTION("Fiction"),
    NON_FICTION("Non fiction"),
    MYSTERY("Mystery"),
    THRILLER("Thriller"),
    ROMANCE("Romance"),
    SCIENCE_FICTION("Science fiction"),
    FANTASY("Fantasy"),
    HORROR("Horror"),
    HISTORICAL_FICTION("Historical fiction"),
    BIOGRAPHY("Biography"),
    SELF_HELP("Self help"),
    YOUNG_ADULT("Young adult");

    private final String displayName;

    BookGenre(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
