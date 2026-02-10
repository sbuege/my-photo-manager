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
  <li>
    <label class="flex items-center gap-3">
      <input type="checkbox" :checked="selected" class="h-4 w-4 text-blue-600 rounded">
      <span class="text-sm text-gray-700">{{ props.label }}</span>
    </label>
  </li>
</template>
