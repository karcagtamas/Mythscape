<template>
  <div class="h-screen w-screen p-4 flex flex-col gap-4 overflow-hidden bg-background">
    <header
      class="h-10 px-4 flex items-center bg-primary text-white text-sm font-semibold tracking-wider uppercase rounded border border-primary/20 shadow-lg shrink-0">
      <span>Mythscape</span>
    </header>
    <main class="flex-1 min-h-0 flex flex-col items-center justify-center relative">
      <router-view />
    </main>
  </div>
</template>