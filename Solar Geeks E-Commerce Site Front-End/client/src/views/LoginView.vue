<template>
  <div id="login">
    <form v-on:submit.prevent="login">
      <h1>Please Sign In</h1>
      <div id="fields">
        <label for="username">Username</label>
        <input
          type="text"
          id="username"
          placeholder="Username"
          v-model="user.username"
          required
          autofocus
        />
        <label for="password">Password</label>
        <input
          type="password"
          id="password"
          placeholder="Password"
          v-model="user.password"
          required
        />
        <div><button type="submit">Sign in</button></div>
      </div>
      <hr/>
      Need an account? <router-link v-bind:to="{ name: 'register' }">Register!</router-link>
    </form>
  </div>
</template>

<script>
import authService from "../services/AuthService";

export default {
  data() {
    return {
      user: {
        username: "",
        password: "",
      },
    };
  },
  methods: {
    login() {
      authService
        .login(this.user)
        .then((response) => {
          if (response.status == 200) {
            this.$store.commit("SET_AUTH_TOKEN", response.data.token);
            this.$store.commit("SET_USER", response.data.user);
            this.$router.push("/");
          }
        })
        .catch((error) => {
          const response = error.response;
          if (!response) {
            alert(error);
          } else if (response.status === 401) {
            alert("Invalid username and password!");
          } else {
            alert(response.message);
          }
        });
    },
  },
};
</script>

<style scoped>
#login {
  display: flex;
  justify-content: center;
  align-items:flex-start;
  min-height: 100vh;
  padding: 20px;
}

form {
  background-color: #ffffff;
  padding: 20px 30px;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  max-width: 400px;
  width: 100%;
  text-align: center;
}

form h1 {
  font-size: 1.8rem;
  color: rgb(124, 25, 25);
  margin-bottom: 20px;
}

#fields {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

label {
  text-align: left;
  font-size: 1rem;
  color: rgb(124, 25, 25);
  font-weight: bold;
  margin-bottom: 5px;
}

input {
  padding: 10px;
  font-size: 1rem;
  border: 1px solid #ccc;
  border-radius: 8px;
  width: 100%;
  box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.1);
  transition: border-color 0.2s;
}

input:focus {
  border-color: rgb(124, 25, 25);
  outline: none;
}

button {
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

button:hover {
  background-color: rgb(145, 66, 66);
  transform: scale(1.05);
}

hr {
  margin: 20px 0;
  border: none;
  border-top: 1px solid #ccc;
}

router-link {
  color: rgb(124, 25, 25);
  font-weight: bold;
  text-decoration: none;
}

router-link:hover {
  color: rgb(145, 66, 66);
  text-decoration: underline;
}

@media only screen and (max-width: 425px) {
  form {
    padding: 15px 20px;
  }

  form h1 {
    font-size: 1.5rem;
  }

  button {
    width: 100%;
  }
}
</style>

