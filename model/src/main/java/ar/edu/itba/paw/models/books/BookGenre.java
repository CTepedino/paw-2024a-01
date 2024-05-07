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
    YOUNG_ADULT("Young adult"),
    DYSTOPIAN("Dystopian"),
    ACTION_ADVENTURE("Action adventure"),
    SUSPENSE("Suspense"),
    CONTEMPORARY_FICTION("Contemporary fiction"),
    LITERARY_FICTION("Literary fiction"),
    MAGIC_REALISM("Magic realism"),
    SHORT_STORY("Short story"),
    NEW_ADULT("New Adult"),
    CHILDREN("Children"),
    MEMOIR_AUTOBIOGRAPHY("Memoir & autobiography"),
    FOOD_DRINK("Food & drink"),
    GRAPHIC_NOVEL("Graphic novel"),
    ART_PHOTOGRAPHY("Art & Photography"),
    HISTORY("History"),
    TRAVEL("Travel"),
    TRUE_CRIME("True Crime"),
    HUMOR("Humor"),
    ESSAY("Essay"),
    GUIDE("Guide"),
    RELIGION("Religion"),
    SPIRITUALITY("Spirituality"),
    HUMANITIES_SOCIAL_SCIENCES("Humanities & Social-sciences"),
    PARENTING_FAMILIES("Parenting & Families"),
    SCIENCE_TECHNOLOGY("Science & Technology");

    private final String displayName;

    BookGenre(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
