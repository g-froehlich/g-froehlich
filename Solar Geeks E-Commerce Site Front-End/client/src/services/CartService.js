import axios from "axios";

export default {

    getCart() {
        return axios.get(`/cart`);
    },

    addItemToCart(item) {
        return axios.post(`/cart/items`, item);
    },

    removeItemFromCart(item, id) {
        return axios.delete(`cart/items/${id}`, item);
    },

    clearCart() {
        return axios.delete(`cart`);
    }
}