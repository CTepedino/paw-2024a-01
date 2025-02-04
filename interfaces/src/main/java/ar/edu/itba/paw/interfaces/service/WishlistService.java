package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.books.WishlistItem;

import java.util.Optional;

public interface WishlistService {

    Optional<WishlistItem> findWishlistItem(long userId, long bookId);
    void addToWishlist(long userId, long bookId);
    void removeFromWishlist(long userId, long bookId);
    PaginatedContent<WishlistItem> getWishlist(long userId, int pageNumber, int pageSize);
}
