<template>
    <div class="shopping-cart">
        <div>
            <h1>
                Cart
                <loading-spinner id="spinner" v-bind:spin="isLoading" />
            </h1>
        </div>
        <div id="clear-cart">
            <button @click="clearCart">Clear All Items</button>
        </div>
        <div id="cart-table-view">
            <div class="table-heading">Product Name</div>
            <div class="table-heading">Individual Price</div>
            <div class="table-heading">Quantity</div>
            <div class="table-heading">Price of Quantity</div>
            <div class="table-heading"></div>
            <template v-for="item in cart.items" v-bind:key="item.productId">
                <div class="table-data">{{ item.product.name }}</div>
                <div class="table-data">{{ formatCurrency(item.product.price) }}</div>
                <div class="table-data">{{ item.quantity }}</div>
                <div class="table-data">{{ formatCurrency(priceOfQuantity(item.quantity, item.product.price)) }}</div>
                <div class="table-data">
                    <font-awesome-icon class="hover-element" icon="fa-solid fa-trash-can" v-on:click="removeItem(item, item.cartItemId)" />
                </div>
            </template>
        </div>
        <div id="totals">
            <div id="subtotal">Subtotal: {{ formatCurrency(cart.itemSubtotal) }}</div>
            <div id="tax">Tax: {{ formatCurrency(cart.tax) }}</div>
            <div id="total">Total: {{ formatCurrency(cart.total) }}</div>
        </div>
    </div>
</template>

<script>
import LoadingSpinner from "../components/LoadingSpinner.vue";
import ProductService from "../services/ProductService";
import UtilityService from "../services/UtilityService";
import CartService from "../services/CartService";

export default {
    components: {
        LoadingSpinner,
    },
    data() {
        return {
            isLoading: false,
            cart: [],
        }
    },
    methods: {
        getCart() {
            this.isLoading = true;

            CartService.getCart().then((response) => {
                this.cart = response.data;
            }).catch((error) => {
                UtilityService.errorHandler(error);
            }).finally(() => {
                this.isLoading = false;
            })
        },
        removeItem(item, id) {
            this.isLoading = true;

            CartService.removeItemFromCart(item, id).then((response) => {
                this.getCart();
            }).catch((error) => {
                UtilityService.errorHandler(error);
            }).finally(() => {
                this.isLoading = false;
            })
        },
        clearCart() {
            this.isLoading = true;

            CartService.clearCart().then((response) => {
                this.getCart();
            }).catch((error) => {
                UtilityService.errorHandler(error);
            }).finally(() => {
                this.isLoading = false;
            })
        },
        formatCurrency(monetaryAmount) {
            return UtilityService.formatCurrency(monetaryAmount);
        },
        priceOfQuantity(quantity, price) {
            return quantity * price;
        },
    },
    computed: {
        
    },
    created() {
        this.getCart();
    }
}
</script>

<style scoped>
.hover-element:hover {
  cursor: pointer;
}

#cart-table-view {
  display: grid;
  margin: auto;
  width: 100%;
  grid-template-columns: auto auto auto auto auto;
  gap: 10px;
  padding: 20px;
  background-color: antiquewhite;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}


.table-heading {
  font-weight: bold;
  text-align: center;
  padding: 10px;
  background-color: rgb(124, 25, 25);
  color: white;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.table-heading:nth-child(5) {
  background-color: antiquewhite;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.0);
}

.table-data {
  text-align: center;
  padding: 8px;
  font-size: 1rem;
  color: rgb(124, 25, 25);
}

.table-data:hover {
  color: rgb(145, 66, 66);
}

#totals {
  margin-top: 20px;
  padding: 20px;
  text-align: right;
  background-color: antiquewhite;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

#totals div {
  margin: 5px 0;
  font-size: 1.2rem;
  font-weight: bold;
  color: rgb(124, 25, 25);
}

#spinner {
  color: green;
  margin-left: 10px;
}

#clear-cart button {
  display: block;
  margin: 10px auto;
  padding: 10px 20px;
  background-color: rgb(124, 25, 25);
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 1rem;
  font-weight: bold;
  transition: background-color 0.2s, transform 0.2s;
}

#clear-cart button:hover {
  background-color: rgb(145, 66, 66);
  transform: scale(1.05);
}

@media only screen and (max-width: 425px) {

  #clear-cart button {
    width: 100%;
  }
}
</style>
