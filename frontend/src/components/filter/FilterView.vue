<script setup>
import FilterSection from "@/components/filter/sections/FilterSection.vue";
</script>

<template>


  <aside class="bg-gray-100 rounded-xl p-5 space-y-5 w-72">
    <h2 class="text-lg font-semibold text-gray-800 text-left"> Filter </h2>

    <FilterSection label="Camera Model" url="/filter/cameraFilter"></FilterSection>
    <FilterSection label="Orientation" url="/filter/orientationFilter"></FilterSection>

  </aside>


</template>

<style scoped>

</style>