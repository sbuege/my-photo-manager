<script setup>

import TagSection from "@/components/mainWrapper/TagSection/TagSection.vue";
import GallerySection from "@/components/mainWrapper/GallerySection/GallerySection.vue";
import TestFilterSection from "@/components/mainWrapper/TestFilterSection/TestFilterSection.vue";
</script>

<template>

  <main class="flex flex-1 w-full">

    <div class="flex flex-1 max-w-7xl mx-auto w-full p-6 gap-6">

      <aside class="w-1/4 bg-white p-4 rounded-xl shadow">
        <TagSection label="Format" url="/tag/orientationTags" />

        <TagSection label="Creation Year Tags" url="/tag/creationYearTags" />

        <TagSection label="Camera Tags" url="/tag/cameraTags" />

        <TagSection label="Location Tags" url="/tag/locationTags" />



      </aside>

      <section class="w-3/4 bg-white p-4 rounded-xl shadow">
        <GallerySection />
      </section>

    </div>

  </main>

</template>
