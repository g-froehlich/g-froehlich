import axios from "axios";

export default {

    getAllProducts() {
        return axios.get(`/products`);
    },

    getProductsBySKU(sku) {
        return axios.get(`/products?sku=${sku}`);
    },

    getProductsByName(productName) {
        return axios.get(`/products?name=${productName}`);
    },

    getProductById(id) {
        return axios.get(`/products/${id}`);
    }
}