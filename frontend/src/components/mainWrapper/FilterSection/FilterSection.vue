<script setup>

import {onMounted, onUnmounted, ref} from "vue";
import FilterItem from "@/components/mainWrapper/FilterSection/items/FilterItem.vue";

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

let intervalId = null
const filters = ref([])

async function fetchFilter() {
  try {
    const response = await fetch(props.url)
    if (!response.ok) {
      throw new Error('Fehler beim Laden')
    }
    filters.value = await response.json()
  } catch (e) {
    //error.value = e?.message ?? String(e)
    filters.value = []
  } finally {
    //loading.value = false
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

  <div class="rounded-xl shadow p-4 border-2 border-gray-200 ml-2 mr-2 m-4">

  <h3 class="text-sm text-center font-medium text-gray-700">
    {{ label }}
  </h3>

  <FilterItem
      v-for="filter in filters"
      :id="filter.id"
      :label="filter.name"
      :selected="false"
  />
  </div>
</template>

<style scoped>

</style>