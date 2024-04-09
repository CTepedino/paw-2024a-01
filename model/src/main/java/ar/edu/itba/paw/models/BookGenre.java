package ar.edu.itba.paw.models;

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

    private final String genre;

    BookGenre(String genre){
        this.genre = genre;
    }

    @Override
    public String toString(){
        return genre;
    }
}
