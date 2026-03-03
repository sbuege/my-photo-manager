<script setup>

import {onMounted, onUnmounted, ref, watch} from "vue";
import PhotoItem from "@/components/mainWrapper/GallerySection/items/PhotoItem.vue";

const props = defineProps({
  activeTags: {
    type: Array,
    default: () => []
  }
})

let intervalId = null
const photos = ref([])
async function fetchPhotos() {
  const response = await fetch("/photos/");
  if (!response.ok) {
    throw new Error("Fehler beim Laden");
  }
  photos.value = await response.json();
}

async function fetchPhotosByTags(externalTagIds) {
  const params = new URLSearchParams();
  for (const id of externalTagIds) {
    params.append("externalTagIds", String(id));
  }

  const response = await fetch(`/photos/byTags?${params.toString()}`);
  if (!response.ok) {
    throw new Error("Failed to load photos");
  }

  photos.value = await response.json();
}

async function loadPhotos() {
  const externalTagIds = (props.activeTags ?? [])
      .map(t => t?.id)
      .filter(id => id !== null && id !== undefined)
      .map(id => String(id));

  if (externalTagIds.length === 0) {
    await fetchPhotos();
    return;
  }

  // Sonst: gefiltert laden
  await fetchPhotosByTags(externalTagIds);
}

watch(
    () => props.activeTags.map(t => t?.id),
    async () => {
      await loadPhotos();
    },
    { immediate: true }
);

onMounted(() => {
  intervalId = setInterval(() => {
    loadPhotos().catch(() => {
    });
  }, 5000);
});

onUnmounted(() => {
  if (intervalId) clearInterval(intervalId);
});

</script>

<template>

  <div class="columns-2 md:columns-3 lg:columns-4 xl:columns-5">
    <PhotoItem
        v-for="photo in photos"
        :externalId="photo.externalId"
        :tags="photo.tags"
    />
  </div>
</template>

<style scoped>

</style>