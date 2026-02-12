<script setup>

import {onMounted, onUnmounted, ref} from "vue";
import PhotoItem from "@/components/photo/items/PhotoItem.vue";

const photoIds = ref([])
const loading = ref(true)
const error = ref(null)
let intervalId = null


async function fetchPhotos() {
  try {
    const response = await fetch("/photos/")
    if (!response.ok) {
      throw new Error('Fehler beim Laden')
    }
    const data = await response.json()
    console.log(data)
    photoIds.value = Array.isArray(data) ? data : []
  } catch (e) {
    error.value = e?.message ?? String(e)
    photoIds.value = []
  } finally {
    loading.value = false
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
  <div class="columns-1 sm:columns-2 md:columns-3 lg:columns-4 gap-6 space-y-6">

    <p v-if="loading">Lade...</p>
    <p v-else-if="error" style="color: red">{{ error }}</p>

     <PhotoItem
          v-for="id in photoIds"
          :id="id"
      />

  </div>
</template>
