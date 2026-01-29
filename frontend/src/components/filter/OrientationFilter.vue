<script setup>
import {onMounted, onUnmounted, ref} from 'vue'

const filters = ref([])
const loading = ref(true)
const error = ref(null)
let intervalId = null

async function fetchOrientationFilter() {
  try {
    const response = await fetch("/filter/orientationFilter")
    if (!response.ok) {
      throw new Error('Fehler beim Laden')
    }
    filters.value = await response.json()
  } catch (e) {
    error.value = e.message
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchOrientationFilter()
  intervalId = setInterval(fetchOrientationFilter, 5000)
})

onUnmounted(() => {
  clearInterval(intervalId)
})
</script>


<template>
  <div>
    <hr>
    <h2>Orientation - Filter</h2>

    <p v-if="loading">Lade...</p>
    <p v-if="error" style="color:red">{{ error }}</p>

    <ul v-if="!loading && !error">
      <li v-for="filter in filters" :key="id">
        {{ filter.name }}
      </li>
    </ul>
  </div>
</template>
