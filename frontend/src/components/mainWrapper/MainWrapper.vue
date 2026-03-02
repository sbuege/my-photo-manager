<script setup>

import TagSection from "@/components/mainWrapper/TagSection/TagSection.vue";
import GallerySection from "@/components/mainWrapper/GallerySection/GallerySection.vue";
import TagItem from "@/components/mainWrapper/TagSection/items/TagItem.vue";
import {ref} from "vue";


const activeTags = ref([])

function addActiveTag(tag) {
  console.log("addActiveTag", tag)

  if (!tag?.id) return

  const id = String(tag.id)
  if (activeTags.value.some(t => String(t.id) === id)) return

  activeTags.value.push({ id, name: tag.name ?? "" })
}

function removeActiveTag(tag) {
  console.log("removeActiveTag", tag)

  const id = String(tag?.id ?? "")
  activeTags.value = activeTags.value.filter(t => String(t.id) !== id)
}

</script>

<template>

  <main class="flex flex-col flex-1 w-full">
    <div class="grid flex-1 max-w-7xl mx-auto w-full p-6 gap-6 grid-cols-4">
      <!-- Full-width innerhalb max-w-7xl -->
      <div class="col-span-4 bg-white p-4 rounded-xl shadow">
        <h2 class="font-semibold">Active Tags</h2>

        <div v-if="activeTags.length === 0" class="mt-3 text-sm text-gray-500">
          Noch keine Tags ausgewählt.
        </div>

        <div v-else class="mt-3 flex flex-wrap gap-2">
          <TagItem
              v-for="tag in activeTags"
              :key="tag.id"
              :id="tag.id"
              :label="tag.name"
              :selected="true"
              @remove="removeActiveTag"
          />
        </div>
      </div>



      <aside class="col-span-1 bg-white p-4 rounded-xl shadow">
        <TagSection label="Format" url="/tag/orientationTags"  @select-tag="addActiveTag"/>
<!--        <TagSection label="Creation Year Tags" url="/tag/creationYearTags" />-->
        <TagSection label="Camera Tags" url="/tag/cameraTags"  @select-tag="addActiveTag" />
        <TagSection label="Location Tags" url="/tag/locationTags" @select-tag="addActiveTag" />
      </aside>

      <section class="col-span-3 bg-white p-4 rounded-xl shadow">
        <GallerySection :active-tags="activeTags"/>
      </section>
    </div>
  </main>

</template>
