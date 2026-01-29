<script setup>
import CameraFilter from './CameraFilter.vue'
// import CreationYearFilter from './CreationYearFilter.vue'
import LocationFilter from "@/components/filter/LocationFilter.vue";
import OrientationFilter from "@/components/filter/OrientationFilter.vue";
</script>

<template>
  <main>
    <h2>Filter</h2>


    <CameraFilter/>

    <LocationFilter/>
    <OrientationFilter/>

  </main>
</template>

<style scoped>

</style>