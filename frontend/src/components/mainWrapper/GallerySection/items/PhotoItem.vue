<script setup>

const props = defineProps({
  externalId: {
    type: String,
    required: true
  },
  tags:{
    type: Array,
  },

})

</script>

<template>
  <div class="rounded-xl shadow p-2 border border-gray-300">
    <img :src="`/photos/thumbnail/${props.externalId}`" alt="Thumbnail" class="w-full h-auto" />
    <p
      v-for="tag in tags"
      class="text-sm text-gray-500"
  >{{tag.name}}</p>

  </div>
</template>
