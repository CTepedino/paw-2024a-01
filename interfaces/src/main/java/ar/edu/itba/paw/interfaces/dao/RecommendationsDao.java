package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.books.Recommendation;

import java.util.List;
import java.util.Optional;

public interface RecommendationsDao {


    Optional<Recommendation> getRecommendation(long userId, long bookId);
    void recommend(long userId, long booKId);
    void removeRecommendation(long userId, long bookId);
    List<Recommendation> getRecommendationsForBook(long userId, int offset, int limit);
    long getRecommendationsSize(long userId);
}
