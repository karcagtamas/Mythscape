<template>
  <v-card elevation="4" class="block">
    <v-card-title class="text-h4 text-primary font-weight-bold">Login</v-card-title>
    <v-card-text>
      <v-form v-model="data.valid">
        <v-text-field label="User Name" v-model="data.username"></v-text-field>
        <v-text-field label="Password" v-model="data.password"></v-text-field>
        <v-btn elevation="2" raised block color="primary" @click="submit" :disabled="!data.valid">Login</v-btn>
      </v-form>
    </v-card-text>
    <v-card-actions>
      <v-btn elevation="2" color="secondary">To Register</v-btn>
    </v-card-actions>
  </v-card>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const data = ref({
  username: '',
  password: '',
  valid: false,
});

function submit() {
  // TODO:
}

</script>

<style lang="scss" scoped>
.block {
  width: min(50%, 400px);
}
</style>
