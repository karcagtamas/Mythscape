<template>
  <div id="auth-frame">
    <div>
      <v-toolbar color="primary" density="compact" rounded outlined elevation="4">
        <v-toolbar-title>Mythscape</v-toolbar-title>
      </v-toolbar>
    </div>
    <div class="center">
      <router-view />
    </div>
  </div>
</template>

<style scoped lang="scss">
@mixin flex() {
  display: flex;
  flex-direction: column;
}

#auth-frame {
  padding: 1rem;
  gap: 1rem;
  height: 100%;
  overflow: hidden;
  @include flex();

  .center {
    @include flex();
    flex: 1;
    align-items: center;
    justify-content: center;
  }
}
</style>
