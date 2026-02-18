<script setup>
const props = defineProps({
  id: {
    type: Number,
    required: true
  },
  label: {
    type: String,
    required: true
  },
  selected: {
    type: Boolean,
    default: false,
    required: false
  }
})
</script>

<template>

  <button
      class="inline-flex items-center gap-2 px-3 py-1 rounded-full text-sm font-medium bg-blue-100 text-blue-800 hover:bg-blue-200 transition"
  >
    {{ props.label }}
    <span class="text-blue-600 hover:text-blue-800">&times;</span>
  </button>



</template>
