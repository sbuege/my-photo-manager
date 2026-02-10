<script setup>

const props = defineProps({
  id: {
    type: Number,
    required: true
  }

})

</script>

<template>

  <img :src="`/photos/thumbnail/${props.id}`" alt="Thumbnail" />
</template>

<style scoped>

</style>