package cms.cms.common;

public enum Pages {

//    Movie
    MOVIE_INDEX("movie/index"),
    MOVIE_FORM("movie/form"),
    MOVIE_CREATE("movie/create"),
    MOVIE_UPDATE("movie/update"),

//    Actor
    ACTOR_INDEX("movie/actor/index"),
    ACTOR_FORM("movie/actor/form"),

//    Home
    HOME("home/index"),

//    Banner
    BANNER_INDEX("movie/banner/index"),
    BANNER_FORM("movie/banner/form"),

//    Episode
    EPISODE_INDEX("movie/episode/index"),
    EPISODE_FORM("movie/episode/form"),

//    Favorite
    FAVORITE_INDEX("movie/favorite/index"),

//    User
    USER_INDEX("user/index"),
    USER_FORM("user/form"),

//    Keyword
    SEARCHKEYWORD_INDEX("movie/searchkeyword/index"),
    SEARCHKEYWORD_FORM("movie/searchkeyword/form"),


//    Category
    CATEGORY_INDEX("movie/category/index"),
    CATEGORY_FORM("movie/category/form");

    private String uri;

    Pages(String uri) {
        this.uri = uri;
    }

    public String uri() {
        return this.uri;
    }
}
