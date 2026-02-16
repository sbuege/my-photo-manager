<script setup>

</script>

<template>
  <div class="flex flex-wrap gap-2">
    <!-- Tag für Kamera -->
    <div
        class="flex items-center bg-blue-100 text-blue-700 text-sm font-medium px-3 py-1 rounded-full shadow-sm"
    >
      Sony A7IV
      <button
          class="ml-2 w-5 h-5 flex items-center justify-center rounded-full bg-blue-200 text-blue-700 hover:bg-blue-300 hover:text-blue-900 focus:outline-none"
          @click="removeTag('Sony A7IV')"
      >
        ×
      </button>
    </div>

    <!-- Tag für Location -->
    <div
        class="flex items-center bg-green-100 text-green-700 text-sm font-medium px-3 py-1 rounded-full shadow-sm"
    >
      München
      <button
          class="ml-2 w-5 h-5 flex items-center justify-center rounded-full bg-green-200 text-green-700 hover:bg-green-300 hover:text-green-900 focus:outline-none"
          @click="removeTag('München')"
      >
        ×
      </button>
    </div>
  </div>
</template>

<style scoped>

</style>