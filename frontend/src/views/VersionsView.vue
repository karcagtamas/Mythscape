<template>
  <div id="version">
    <VersionBlock
      v-for="version in versions"
      :key="version.release"
      :version="version"
    ></VersionBlock>
  </div>
</template>

<script setup lang="ts">
import { VERSIONS } from '@/utils/constants'
import { computed } from 'vue'
import VersionBlock from '@/components/versions/VersionBlock.vue'

const versions = computed(() => VERSIONS)
</script>

<style lang="scss">
#version {
  display: flex;
  padding: 1rem;
  gap: 1rem;

  div {
    width: 100%;
  }
}
</style>
