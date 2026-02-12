<script setup>
import {computed, onMounted, onUnmounted, ref} from 'vue'
import FilterItem from "@/components/filter/items/FilterItem.vue";

const filters = ref([])
const loading = ref(true)
const error = ref(null)
let intervalId = null

const hasFilters = computed(() => Array.isArray(filters.value) && filters.value.length > 0)

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
  <div v-if="loading || error || hasFilters">
    <hr />
    <h2>Orientation - Section</h2>

    <p v-if="loading">Lade...</p>
    <p v-else-if="error" style="color: red">{{ error }}</p>

    <ul v-else>
      <FilterItem
          v-for="filter in filters"
          :id="filter.id"
          :label="filter.name"
          :selected="false"
      />
    </ul>
  </div>
</template>
