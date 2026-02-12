<script setup>

const props = defineProps({
  id: {
    type: Number,
    required: true
  }

})

</script>

<template>
  <img :src="`/photos/thumbnail/${props.id}`" alt="Thumbnail" class="w-full rounded-2xl shadow-md transition duration-300 break-inside-avoid" />
</template>

<style scoped>

</style>