<script setup>
import {ref, onMounted, onUnmounted, computed} from "vue"
import FilterItem from "@/components/filter/items/FilterItem.vue";

const filters = ref([])
const loading = ref(true)
const error = ref(null)
let intervalId = null

const hasFilters = computed(() => Array.isArray(filters.value) && filters.value.length > 0)

async function fetchCameraFilter() {
  try {
    const response = await fetch("/filter/cameraFilter")
    if (!response.ok) {
      throw new Error('Fehler beim Laden')
    }
    filters.value = await response.json()
  } catch (e) {
    error.value = e?.message ?? String(e)
    filters.value = []
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchCameraFilter()
  intervalId = setInterval(fetchCameraFilter, 5000)
})

onUnmounted(() => {
  clearInterval(intervalId)
})
</script>

<template>
  <div v-if="loading || error || hasFilters">
    <h3 class="text-sm font-medium text-gray-700">
      Camera Model
    </h3>

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
