package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.WishlistDao;
import ar.edu.itba.paw.models.books.WishlistItem;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class WishlistJpaDao implements WishlistDao {


    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<WishlistItem> findWishlistItem(long userId, long bookId) {
        TypedQuery<WishlistItem> query = em.createQuery("FROM WishlistItem w WHERE w.userId = :userId AND w.bookId = :bookId", WishlistItem.class);
        query.setParameter("userId", userId);
        query.setParameter("bookId", bookId);

        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public void addToWishlist(long userId, long bookId){
        WishlistItem wishlistItem = new WishlistItem(userId, bookId);
        em.persist(wishlistItem);
    }

    @Override
    public void removeFromWishlist(long userId, long bookId){
        Query deleteQuery = em.createQuery("DELETE FROM WishlistItem w WHERE w.userId = :userId AND w.bookId = :bookId");
        deleteQuery.setParameter("userId", userId);
        deleteQuery.setParameter("bookId", bookId);
        deleteQuery.executeUpdate();
    }

    @Override
    public List<WishlistItem> getWishlist(long userId, int offset, int limit){
        Query nativeQuery = em.createNativeQuery("SELECT id FROM wishlist WHERE user_id = :userId");
        nativeQuery.setParameter("userId", userId);

        TypedQuery<WishlistItem> query = em.createQuery("FROM WishlistItem w WHERE w.id IN :idList", WishlistItem.class);

        return DaoUtils.paginatedQuery(em, nativeQuery, query, offset, limit);
    }

    @Override
    public long getWishlistSize(long userId) {
        return DaoUtils.getRowCount(em, "WishlistItem w", "w.id","WHERE w.userId = :userId", Map.of("userId", userId));
    }

}
