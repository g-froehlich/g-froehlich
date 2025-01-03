<template>
  <div class="product-list">
    <div>
      <h1>
        Products
        <loading-spinner id="spinner" v-bind:spin="isLoading" />
      </h1>
      <h2 v-show="!isLoggedIn">Welcome. You may browse anonymously as much as you wish, but you must 
          <router-link to="/login">Login</router-link>
          to add items to your shopping cart.</h2>
    </div>
    <div id="product-search">
      <input type="text" id="search" placeholder="Search products..." v-model="filter" v-on:input="getProducts">
    </div>
    <div id="view-selector">
      <font-awesome-icon v-bind:class="{ 'view-icon': true, active: cardView }" v-on:click="cardView = true"
        icon="fa-solid fa-grip" title="View tiles" />
      <font-awesome-icon v-bind:class="{ 'view-icon': true, active: !cardView }" v-on:click="cardView = false"
        icon="fa-solid fa-table" title="View table" />
    </div>
    <div id="product-card-view" v-show="cardView">
      <product-cards id="cards" :products="products" />
    </div>
    <div id="product-table-view" v-show="!cardView">
      <div class="table-heading">Product</div>
      <div class="table-heading">SKU</div>
      <div class="table-heading">Description</div>
      <div class="table-heading">Price</div>
      <div class="table-heading"></div>
      <template v-for="product in products" v-bind:key="product.productId">
        <div class="table-data">
          <router-link v-bind:to="{ name: 'product-details', params: { id: product.productId } }">
            {{ product.name }}
          </router-link>
        </div>
        <div class="table-data">{{ product.productSku }}</div>
        <div class="table-data">{{ product.description }}</div>
        <div class="table-data">{{ formatCurrency(product.price) }}</div>
        <div class="table-data" id="cart-button">
          <font-awesome-icon class="hover-element" icon="fa-solid fa-cart-plus" v-show="isLoggedIn" v-on:click="addToCart(product)"/>
        </div>
      </template>
    </div>
  </div>
</template>

<script>
import LoadingSpinner from "../components/LoadingSpinner.vue";
import ProductService from "../services/ProductService";
import UtilityService from "../services/UtilityService";
import ProductCards from "../components/ProductCards.vue";
import CartService from "../services/CartService";

export default {
  components: {
    LoadingSpinner,
    ProductCards
  },
  data() {
    return {
      isLoading: false,
      cardView: true,
      products: [],
      filter: '',
    };
  },
  methods: {
    //get a list of products from API
    getProducts() {
      if (this.filter) {
        this.searchProducts();
        return;
      }

      this.isLoading = true;

      ProductService.getAllProducts().then((response) => {
        this.products = response.data;
      }).catch((error) => {
        UtilityService.errorHandler(error);
      }).finally(() => {
        this.isLoading = false;
      });
    },
    searchProducts() {
      this.isLoading = true;

      ProductService.getProductsByName(this.filter).then((response) => {
        this.products = response.data;
      }).catch((error) => {
        UtilityService.errorHandler(error);
      }).finally(() => {
        this.isLoading = false;
      });
    },
    formatCurrency(monetaryAmount) {
      return UtilityService.formatCurrency(monetaryAmount);
    },
    details(id) {
      this.$router.push({ name: "ProductDetailsView", params: { id: id } });
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
  computed: {
    isLoggedIn() {
      return this.$store.state.token.length > 0;
    },
  },
  created() {
    this.getProducts();
    this.searchProducts();
  }
};
</script>

<style scoped>
.hover-element:hover {
  cursor: pointer;
}

#view-selector {
  margin: 10px;
}

#product-table-view {
  display: grid;
  margin: auto;
  width: 100%;
  grid-template-columns: auto auto auto auto auto;
  gap: 10px;
  padding: 10px;
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

.table-data a {
  text-decoration: none;
  color: rgb(124, 25, 25);
}

.table-data a:hover {
  color: rgb(145, 66, 66);
}

#product-search {
  padding-bottom: 10px;
  display: flex;
  justify-content: center;
}

#search {
  width: 80%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 1rem;
  box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.1);
}

#cards {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-evenly;
  gap: 20px;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
}

#spinner {
  color: green;
  margin-left: 10px;
}

.view-icon {
  font-size: 1.5rem;
  margin-right: 7px;
  padding: 5px;
  color: rgb(124, 25, 25);
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.2s, color 0.2s;
}

.view-icon.active {
  background-color: lightgreen;
}

.view-icon:not(.active):hover {
  color: blue;
  background-color: rgba(255, 255, 255, 0.7);
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

@media only screen and (max-width: 425px) {
  #product-table-view {
    gap: 5px;
  }

  #cards {
    flex-direction: column;
    align-items: center;
  }
}
</style>
