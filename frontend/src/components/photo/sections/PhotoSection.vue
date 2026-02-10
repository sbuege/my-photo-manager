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
  <div>
    <hr />
    <h2>Photo - Section</h2>

    <p v-if="loading">Lade...</p>
    <p v-else-if="error" style="color: red">{{ error }}</p>

    <ul v-else>
      <PhotoItem
          v-for="id in photoIds"
          :id="id"
      />
    </ul>
  </div>
</template>
