<script setup>

import {computed, onMounted, onUnmounted, ref} from "vue";
import FilterItem from "@/components/filter/items/FilterItem.vue";

const hasFilters = computed(() => Array.isArray(filters.value) && filters.value.length > 0)

const props = defineProps({
  url: {
    type: String,
    required: true
  },

  label: {
    type: String,
    required: true
  }
})

const filters = ref([])
const loading = ref(true)
const error = ref(null)
let intervalId = null

async function fetchFilter() {
  try {
    const response = await fetch(props.url)
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
  fetchFilter()
  intervalId = setInterval(fetchFilter, 5000)
})

onUnmounted(() => {
  clearInterval(intervalId)
})

</script>

<template>
  <div v-if="loading || error || hasFilters" class="space-y-3 border border-gray rounded-2xl p-2">
    <h3 class="text-sm font-medium text-gray-700">
      {{ label }}
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