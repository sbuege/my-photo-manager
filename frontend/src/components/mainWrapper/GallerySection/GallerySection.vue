<script setup>

import {onMounted, onUnmounted, ref} from "vue";
import PhotoItem from "@/components/mainWrapper/GallerySection/items/PhotoItem.vue";


let intervalId = null
const photos = ref([])
async function fetchPhotos() {
  try {
    const response = await fetch("/photos/")
    if (!response.ok) {
      throw new Error('Fehler beim Laden')
    }

    photos.value = await response.json()

  } catch (e) {
    //error.value = e?.message ?? String(e)
  } finally {
    //loading.value = false
  }
}

onMounted(() => {
  fetchPhotos()
  intervalId = setInterval(fetchPhotos, 5000)
})

onUnmounted(() => {
  clearInterval(intervalId)
})

</script>

<template>

  <div class="columns-2 md:columns-3 lg:columns-4 xl:columns-5">
    <PhotoItem
        v-for="photo in photos"
        :id="photo.id"
        :camera-model="photo.cameraModel ?? 'unknown camera model'"
    />
  </div>
</template>

<style scoped>

</style>