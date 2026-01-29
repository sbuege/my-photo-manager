<script setup>
import { ref, onMounted } from 'vue'

const filters = ref([])
const loading = ref(true)
const error = ref(null)

onMounted(async () => {
  try {
    const response = await fetch('/filter/creationYear')
    if (!response.ok) {
      throw new Error('Fehler beim Laden')
    }
    filters.value = await response.json()
  } catch (e) {
    error.value = e.message
  } finally {
    loading.value = false
  }
})
</script>


<template>
  <div>
    <h2>Creation Year - Filter</h2>

    <p v-if="loading">Lade...</p>
    <p v-if="error" style="color:red">{{ error }}</p>

    <ul v-if="!loading && !error">
      <li v-for="filter in filters" :key="id">
        {{ filter.name }}
      </li>
    </ul>
  </div>
</template>
