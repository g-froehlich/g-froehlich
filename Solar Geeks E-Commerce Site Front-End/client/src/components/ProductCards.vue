<template>
    <section id="product-cards">
        <article class="product-card" v-for="product in products" v-bind:key="product.productId">
            <div class="product-image">
                <img class="image" v-bind:src="product.imageName">
            </div>
            <div class="product-name">
                <router-link v-bind:to="{ name: 'product-details', params: { id: product.productId } }">
                    {{ product.name }}
                </router-link>
            </div>
            <div class="price">
                {{ formatCurrency(product.price) }}
            </div>
            <div id="cart-button">
                <font-awesome-icon class="hover-element" icon="fa-solid fa-cart-plus" v-show="isLoggedIn" v-on:click="addToCart(product)"/>
            </div>
        </article>
    </section>
</template>

<script>
import UtilityService from '../services/UtilityService';
import CartService from '../services/CartService';

export default {
    props: ['products'],
    methods: {
        formatCurrency(monetaryAmount) {
            return UtilityService.formatCurrency(monetaryAmount)
        },
        addToCart(product) {
            const itemToAdd = {
                ...product,
                quantity: 1
            }

            this.isLoading = true;

            CartService.addItemToCart(itemToAdd).then((response) => {
                alert("Item has been added to cart.")
            }).catch((error) => {
                UtilityService.errorHandler(error);
            }).finally(() => {
                this.isLoading = false;
            });
        }
    },
    computed: {
    isLoggedIn() {
      return this.$store.state.token.length > 0;
    },
  },
}

</script>

<style scoped>
.product-image {
    grid-area: image;
    display: flex;
    justify-content: center;
    align-items: center;
    margin-bottom: 8px;
}

.product-image img {
    max-width: 100%;
    max-height: 150px;
    border-radius: 4px;
}

.product-name {
    grid-area: name;
    text-align: center;
    font-size: 1rem;
    font-weight: bold;
    margin: 8px 0;
}

.product-name a {
    text-decoration: none;
    color: rgb(124, 25, 25);
    transition: color 0.2s;
}

.product-name a:hover {
    color: rgb(145, 66, 66);
} 

.price {
    grid-area: price;
    text-align: center;
    font-size: 1.2rem;
    color: rgb(124, 25, 25);
    font-weight: bold;
    margin-bottom: 8px;
}

#cart-button {
    grid-area: cart;
    display: flex;
    justify-content: right;
}

#cart-button .hover-element {
    font-size: 1.5rem;
    color: rgb(124, 25, 25);
    cursor: pointer;
    transition: color 0.2s, transform 0.2s;
}

#cart-button .hover-element:hover {
    color: rgb(145, 66, 66);
    transform: scale(1.1);
}

.product-card {
    border-radius: 4px;
    display: grid;
    border: 1px solid black;
    margin: 10px;
    padding: 16px;
    background-color: antiquewhite;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    grid-template-columns: 1fr;
    grid-template-rows: auto auto auto auto;
    grid-template-areas:
        "image"
        "name"
        "price"
        "cart";
    transition: transform 0.2s, box-shadow 0.2s;
}

#product-cards {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    gap: 20px;
    padding: 20px;
    background-color: #f9f9f9;
    border-radius: 8px;
}
</style>