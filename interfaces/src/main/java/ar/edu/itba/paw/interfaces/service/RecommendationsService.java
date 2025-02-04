package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.books.Recommendation;

import java.util.Optional;

public interface RecommendationsService {

    PaginatedContent<Recommendation> getRecommendations(long userId, int page, int size);
    Optional<Recommendation> findRecommendation(long userId, long bookId);
    void recommend(long userId, long bookId);
    void removeRecommendation(long userId, long bookId);
}
