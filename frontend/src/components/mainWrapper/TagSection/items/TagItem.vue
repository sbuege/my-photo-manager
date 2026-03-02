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
  selected: { type: Boolean, default: false }
})

const emit = defineEmits(["select", "remove"])

function onSelect() {
  emit("select", { id: props.id, name: props.label })
  console.log("Tag Item selected:", props.label)
}

function onRemove(e) {
  e.stopPropagation()
  emit("remove", { id: props.id })
}

</script>

<template>

  <button
      type="button"
      class="inline-flex items-center gap-2 px-3 py-1 rounded-full text-sm font-medium bg-blue-100 text-blue-800 hover:bg-blue-200 transition"
      @click="onSelect"
  >
    {{ props.label }}

    <button
        v-if="props.selected"
        type="button"
        class="ml-1 w-5 h-5 flex items-center justify-center rounded-full bg-blue-200 text-blue-700 hover:bg-blue-300 hover:text-blue-900 focus:outline-none"
        @click="onRemove"
    >
      ×
    </button>
  </button>



</template>
