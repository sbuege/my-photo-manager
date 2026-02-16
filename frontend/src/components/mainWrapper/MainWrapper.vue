<script setup>

import FilterSection from "@/components/mainWrapper/FilterSection/FilterSection.vue";
import GallerySection from "@/components/mainWrapper/GallerySection/GallerySection.vue";
import TestFilterSection from "@/components/mainWrapper/TestFilterSection/TestFilterSection.vue";
</script>

<template>

  <main class="flex flex-1 w-full">

    <div class="flex flex-1 max-w-7xl mx-auto w-full p-6 gap-6">

      <aside class="w-1/5 bg-white p-4 rounded-xl shadow">
        <FilterSection label="Orientation Filter" url="/filter/orientationFilter" />

        <FilterSection label="Camera Filter" url="/filter/cameraFilter" />

        <FilterSection label="Location Filter" url="/filter/locationFilter" />

      </aside>

      <section class="w-4/5 bg-white p-4 rounded-xl shadow">
        <GallerySection />
      </section>

    </div>

  </main>

</template>
