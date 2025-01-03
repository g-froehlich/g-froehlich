<template>
    <div class="product-details-view">
        <div id="heading">
            <h1>
                Product: {{ product.name }}
                <loading-spinner id="spinner" v-bind:spin="isLoading" />
            </h1>
            <h3>SKU Number: {{ product.productSku }}</h3>
            <img class="image" v-bind:src="product.imageName">
        </div>
        <div class="description">
            {{ product.description }}
        </div>
        <div class="price">
            {{ formatCurrency(product.price) }}
        </div>
        <div class="cart-button">
            <button v-show="isLoggedIn" @click="addToCart(this.product)">Add to Cart</button>
        </div>
    </div>
</template>

<script>
import UtilityService from '../services/UtilityService';
import ProductService from '../services/ProductService';
import LoadingSpinner from '../components/LoadingSpinner.vue';
import CartService from '../services/CartService';

export default {
    components: {
        LoadingSpinner,

    },
    name: "ProductDetailsView",
    data() {
        return {
            product: {},
            isLoading: false,
        }
    },
    methods: {
        formatCurrency(monetaryAmount) {
            return UtilityService.formatCurrency(monetaryAmount);
        },
        getProduct(id) {
            console.log(id)

            this.isLoading = true;

            ProductService.getProductById(id).then((response) => {
                this.product = response.data;
            }).catch((error) => {
                UtilityService.errorHandler(error);
            }).finally(() => {
                this.isLoading = false;
            });
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
            })
        }
    },
    created() {
        this.getProduct(this.$route.params.id)
    },
    computed: {
    isLoggedIn() {
      return this.$store.state.token.length > 0;
    },
  },
}
</script>

<style scoped>
#spinner {
  color: green;
  margin-left: 10px;
}

.product-details-view {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  background-color: antiquewhite;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  max-width: 800px;
  margin: 20px auto;
}

#heading {
  text-align: center;
  margin-bottom: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

#heading h1 {
  font-size: 2rem;
  color: rgb(124, 25, 25);
  margin-bottom: 10px;
}

#heading h3 {
  font-size: 1.2rem;
  color: #333;
  margin-bottom: 20px;
}

#heading .image {
max-width: 80%;
  max-height: 500px;
  height: auto;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-top: 20px;
  object-fit: contain;
}

.description {
  font-size: 1rem;
  color: #333;
  margin: 20px 0;
  text-align: center;
  line-height: 1.5;
}

.price {
  font-size: 1.5rem;
  font-weight: bold;
  color: rgb(124, 25, 25);
  margin: 20px 0;
}

.cart-button button {
  padding: 10px 20px;
  background-color: rgb(124, 25, 25);
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.2s, transform 0.2s;
}

.cart-button button:hover {
  background-color: rgb(145, 66, 66);
  transform: scale(1.05);
}

@media only screen and (max-width: 425px) {
  .product-details-view {
    padding: 10px;
  }

  #heading h1 {
    font-size: 1.5rem;
  }

  #heading h3 {
    font-size: 1rem;
  }

  .price {
    font-size: 1.2rem;
  }

  .cart-button button {
    width: 100%;
  }
}
</style>
