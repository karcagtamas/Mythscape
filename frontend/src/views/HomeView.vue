<template>
  <div id="home">
    <v-card elevation="2">
      <template v-slot:title>
        <div id="title">
          <img class="main-color" src="/main.svg" />
          <div class="text-primary text-h3 font-weight-bold">Welcome to the Mythscape</div>
        </div>
      </template>
      <v-divider></v-divider>
      <v-card variant="tonal">
        <v-card-text>
          <p class="text-h6">
            This application helps you manage your campaigns, sessions, and notes for any
            role-playing world.
          </p>
        </v-card-text>
      </v-card>
      <v-divider></v-divider>
      <v-card-text>
        <div id="links">
          <div class="link">
            <div class="text-secondary">Go to "Login" if you already have an account.</div>
            <v-btn variant="outlined" color="secondary" @click="handleToLogin">To Login</v-btn>
          </div>
          <div class="link">
            <div class="text-secondary">Or click "Register" if you want to create an account.</div>
            <v-btn variant="outlined" color="secondary" @click="handleToRegister"
              >To Register</v-btn
            >
          </div>
        </div>
      </v-card-text>
    </v-card>
    <footer>
      <div>Created by KarcagS</div>
      <div @click="handleToVersions">Version 1.0.0</div>
    </footer>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()

const handleToLogin = () => {
  return router.push('/auth/login')
}

const handleToRegister = () => {
  return router.push('/auth/register')
}

const handleToVersions = () => {
  return router.push('/versions')
}
</script>

<style lang="scss" scoped>
#home {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;

  #title {
    display: flex;
    flex-direction: row;
    align-items: center;
    gap: 2rem;
    padding: 1rem;

    img {
      height: 240px;
      width: 200px;
    }
  }

  p {
    padding: 1rem 1rem;
  }

  #links {
    display: flex;
    flex-direction: row;

    .link {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      flex: 1;
      gap: 1rem;
    }
  }

  footer {
    position: absolute;
    display: flex;
    flex-direction: row;
    bottom: 0;
    width: 100%;

    div {
      flex: 1;
      padding: 0.4rem;

      &:last-child {
        text-align: right;

        &:hover {
          cursor: pointer;
        }
      }
    }
  }
}
</style>
