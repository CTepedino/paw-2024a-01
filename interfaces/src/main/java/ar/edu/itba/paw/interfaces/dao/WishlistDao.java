package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.books.WishlistItem;

import java.util.List;
import java.util.Optional;

public interface WishlistDao {

    Optional<WishlistItem> findWishlistItem(long userId, long bookId);
    void addToWishlist(long userId, long bookId);
    void removeFromWishlist(long userId, long bookId);
    List<WishlistItem> getWishlist(long userId, int offset, int limit);
    long getWishlistSize(long userId);

}
