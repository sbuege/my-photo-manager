<script setup>

const props = defineProps({
  id: {
    type: Number,
    required: true
  },
  tags:{
    type: Array,
  },

})

</script>

<template>
  <div class="rounded-xl shadow p-2 border-1 border-gray-300">
    <img :src="`/photos/thumbnail/${props.id}`" alt="Thumbnail" class="w-full h-auto" />
    <p
      v-for="tag in tags"
      class="text-sm text-gray-500"
  >{{tag.name}}</p>

  </div>
</template>
