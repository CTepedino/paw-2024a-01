package ar.edu.itba.paw.models.books;

public enum BookGenre {
    FICTION("Fiction", "menu_book"),
    NON_FICTION("Non fiction", "newspaper"),
    MYSTERY("Mystery", "search"),
    THRILLER("Thriller", "directions_run"),
    ROMANCE("Romance", "favorite"),
    SCIENCE_FICTION("Science fiction", "psychology_alt"),
    FANTASY("Fantasy", "auto_fix_high"),
    HORROR("Horror", "mood_bad"),
    HISTORICAL_FICTION("Historical fiction", "history_edu"),
    BIOGRAPHY("Biography", "person"),
    SELF_HELP("Self help", "healing"),
    YOUNG_ADULT("Young adult", "local_play"),
    DYSTOPIAN("Dystopian", "public"),
    ACTION_ADVENTURE("Action adventure", "directions_run"),
    SUSPENSE("Suspense", "person_search"),
    CONTEMPORARY_FICTION("Contemporary fiction", "book"),
    LITERARY_FICTION("Literary fiction", "menu_book"),
    MAGIC_REALISM("Magic realism", "history_edu"),
    SHORT_STORY("Short story", "menu_book"),
    NEW_ADULT("New Adult", "diversity_3"),
    CHILDREN("Children", "child_care"),
    MEMOIR_AUTOBIOGRAPHY("Memoir & autobiography", "person"),
    FOOD_DRINK("Food & drink", "restaurant"),
    GRAPHIC_NOVEL("Graphic novel", "dashboard"),
    ART_PHOTOGRAPHY("Art & Photography", "photo_camera"),
    HISTORY("History", "history_edu"),
    TRAVEL("Travel", "flight"),
    TRUE_CRIME("True Crime", "fingerprint"),
    HUMOR("Humor", "mood"),
    ESSAY("Essay", "description"),
    GUIDE("Guide", "list"),
    RELIGION("Religion", "church"),
    SPIRITUALITY("Spirituality", "self_improvement"),
    HUMANITIES_SOCIAL_SCIENCES("Humanities & Social-sciences", "groups"),
    PARENTING_FAMILIES("Parenting & Families", "family_restroom"),
    SCIENCE_TECHNOLOGY("Science & Technology", "biotech");

    private final String displayName;

    private final String iconName;

    BookGenre(String displayName, String iconName){
        this.displayName = displayName;
        this.iconName = iconName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIconName() {
        return iconName;
    }
}
