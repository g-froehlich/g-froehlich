<template>
  <div id="cart-app">
    <header>
      <header-image />
    </header>
    <nav>
        <router-link v-bind:to="{ name: 'products' }">Home</router-link> |
        <router-link v-bind:to="{ name: 'cart' }">Cart</router-link> |
        <router-link v-bind:to="{ name: 'logout' }" v-if="$store.state.token">
          Logout
        </router-link>
        <router-link v-bind:to="{ name: 'login' }" v-else>Login</router-link>
    </nav>
    <main>
      <router-view />
    </main>
    <footer>
      <p>&copy; 2024 Solar Geeks LLC. All rights reserved.</p>
    </footer>
  </div>
</template>

<script>
import HeaderImage from './components/HeaderImage.vue';

export default {
  components: {
    HeaderImage,
  }
}
</script>

<style scoped>
html, body {
  margin: 0;
  padding: 0;
  width: 100%;
  height: 100%;
  font-family: Arial, sans-serif;
  background-color: #f9f9f9;
  overflow-x: hidden;
  box-sizing: border-box;
}

header {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  grid-area: header;
  background-color: antiquewhite;
  padding: 10px 0;
  border-radius: 0 0 8px 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  z-index: 1000;
}

nav {
  grid-area: nav;
  margin: 0;
  padding: 120px 20px 10px;
  text-align: center;
  background-color: antiquewhite;
}

nav a {
  text-decoration: none;
  color: rgb(124, 25, 25);
  font-weight: bold;
  margin: 0 10px;
  transition: color 0.2s;
}

nav a:hover {
  color: rgb(145, 66, 66);
}

main {
  grid-area: main;
  padding: 10px 20px 60px;
  background-color: #f9f9f9;
  width: 100%;
  margin: 0;
  box-sizing: border-box;
}

footer {
  grid-area: footer;
  padding: 10px 20px;
  position: fixed;
  bottom: 0;
  display: flex;
  width: 100%;
  justify-content: center;
  align-items: center;
  background-color: antiquewhite;
  left: 0;
  border-radius: 8px 8px 0 0;
  box-shadow: 0 -4px 6px rgba(0, 0, 0, 0.1);
}

footer > p {
  margin: 0;
  color: rgb(124, 25, 25);
  font-size: 0.9rem;
}

#cart-app {
  display: grid;
  height: 100vh;
  width: 100vw;
  margin: 0;
  padding: 0;
  grid-template-rows: auto auto 1fr auto;
  grid-template-areas:
      "header"
      "nav"
      "main"
      "footer";
  background-color: #f9f9f9;
}

@media only screen and (min-width: 1024px) {
  main {
    max-width: 1024px;
    margin: 0 auto;
  }
}

@media only screen and (max-width: 425px) {
  nav a {
    margin: 5px 0;
  }

  header {
    padding: 10px;
  }

  footer {
    padding: 10px;
  }
}
</style>
