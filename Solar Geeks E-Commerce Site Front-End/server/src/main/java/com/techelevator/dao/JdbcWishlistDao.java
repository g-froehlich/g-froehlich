package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Wishlist;
import com.techelevator.model.WishlistItem;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcWishlistDao implements WishlistDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcWishlistDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Wishlist> getWishlists(int userId) {
        List<Wishlist> list = new ArrayList<>();
        String sql = "SELECT * FROM wishlist WHERE user_id = ? ORDER BY wishlist_id";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
            while (results.next()) {
                Wishlist item = mapRowToWishlist(results);
                list.add(item);
            }
        }
        catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return list;
    }

    @Override
    public Wishlist getWishlistByWishListIdAndUserId(int userId, int id) {
        Wishlist wishlist = null;
        String sql = "SELECT w.*, wi.wishlist_item_id, wi.product_id FROM wishlist w LEFT JOIN wishlist_item wi ON (w.wishlist_id = wi.wishlist_id) WHERE w.wishlist_id = ? AND w.user_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id, userId);
            while (results.next()) {
                if (wishlist == null) {
                    wishlist = mapRowToWishlist(results);
                    wishlist.setItems(new ArrayList<>());
                }
                // If there are items in the list, populate the object
                WishlistItem item = mapRowToWishlistItem(results);
                if (item != null) {
                    wishlist.getItems().add(item);
                }
            }
        }
        catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return wishlist;
    }

    @Override
    public Wishlist createWishlist(Wishlist wishlist) {
        Wishlist newWishlist = null;
        String sql = "INSERT INTO wishlist(user_id, name) VALUES(?, ?) RETURNING wishlist_id";
        try {
            int newId = jdbcTemplate.queryForObject(sql, int.class, wishlist.getUserId(), wishlist.getName());
            // TODO: If wishlist.items != null, loop through and add items using the new id for the wishlist
            if (wishlist.getItems() != null) {
                for (WishlistItem item : wishlist.getItems()) {
                    item.setWishlistId(newId);
                    createWishListItem(item);
                }
            }
            wishlist = getWishlistByWishListIdAndUserId(wishlist.getUserId(), newId);
        }
        catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return wishlist;
    }

    @Override
    public int deleteWishlistById(int userId, int wishlistId) {
        int numberOfRows = 0;
        // Make sure the item noted is actually the user's
        String sql =
                "DELETE FROM wishlist_item WHERE wishlist_id = ? AND wishlist_id IN (SELECT wishlist_id FROM wishlist WHERE user_id = ?); " +
                "DELETE FROM wishlist WHERE wishlist_id = ? AND user_id = ?";
        try {
            numberOfRows = jdbcTemplate.update(sql, wishlistId, userId, wishlistId, userId);
        }
        catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return numberOfRows;
    }

    @Override
    public WishlistItem createWishListItem(WishlistItem wishlistItem) {
        WishlistItem newWishlistItem = null;
        String sql = "INSERT INTO wishlist_item(wishlist_id, product_id) VALUES (?, ?) RETURNING wishlist_item_id";
        try {
            int newId = jdbcTemplate.queryForObject(sql, int.class, wishlistItem.getWishlistId(), wishlistItem.getProductId());
            newWishlistItem = getWishlistItemById(newId);
        }
        catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return newWishlistItem;
    }

    @Override
    public int deleteWishListItem(int userId, int wishlistId, int productId) {
        int numberOfRows = 0;
        // Make sure the item noted is actually the user's
        String sql = "DELETE FROM wishlist_item WHERE product_id = ? AND wishlist_id = ? AND wishlist_id IN (SELECT wishlist_id FROM wishlist WHERE user_id = ?)";
        try {
            numberOfRows = jdbcTemplate.update(sql, productId, wishlistId, userId);
        }
        catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return numberOfRows;
    }

    @Override
    public WishlistItem getWishlistItemByWishlistIdAndProductId(int wishlistId, int productId) {
        WishlistItem wishlistItem = null;
        String sql = "SELECT * FROM wishlist_item WHERE wishlist_id = ? AND product_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, wishlistId, productId);
            if (results.next()) {
                wishlistItem = mapRowToWishlistItem(results);
            }
        }
        catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return wishlistItem;
    }

    @Override
    public WishlistItem getWishlistItemById(int wishlistItemId) {
        WishlistItem wishlistItem = null;
        String sql = "SELECT * FROM wishlist_item WHERE wishlist_item_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, wishlistItemId);
            if (results.next()) {
                wishlistItem = mapRowToWishlistItem(results);
            }
        }
        catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return wishlistItem;
    }

    private Wishlist mapRowToWishlist(SqlRowSet rs) {
        Wishlist wishlist = new Wishlist();
        wishlist.setWishlistId(rs.getInt("wishlist_id"));
        wishlist.setUserId(rs.getInt("user_id"));
        wishlist.setName(rs.getString("name"));
        return wishlist;
    }

    private WishlistItem mapRowToWishlistItem(SqlRowSet rs) {
        WishlistItem item = new WishlistItem();
        item.setWishlistId(rs.getInt("wishlist_id"));
        item.setWishlistItemId(rs.getInt("wishlist_item_id"));
        item.setProductId(rs.getInt("product_id"));

        // Since this was a left join, the values for the "right" table (wishlist_item) may be null.
        // Those would show up as 0 for the int fields. So if the wishlist_item_id is 0, that means there's
        // no item. Return null in that case.
        return (item.getWishlistItemId() == 0) ? null : item;
    }
}
