<script setup>
import {ref, onMounted, onUnmounted} from 'vue'

const filters = ref([])
const loading = ref(true)
const error = ref(null)
let intervalId = null

async function fetchLocationFilter() {
  try {
    const response = await fetch("/filter/locationFilter")
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
  fetchLocationFilter()
  intervalId = setInterval(fetchLocationFilter, 5000)
})

onUnmounted(() => {
  clearInterval(intervalId)
})
</script>


<template>
  <div>
    <hr>
    <h2>Location - Filter</h2>

    <p v-if="loading">Lade...</p>
    <p v-if="error" style="color:red">{{ error }}</p>

    <ul v-if="!loading && !error">
      <li v-for="filter in filters" :key="id">
        {{ filter.country }} {{ filter.city}}
      </li>
    </ul>
  </div>
</template>
