<template>
  <div class="bg-gray-50 text-gray-800 min-h-screen font-sans">
    <div class="container mx-auto max-w-4xl px-4 py-8">
      <div
        class="flex flex-col sm:flex-row justify-between items-center mb-4 gap-4"
      >
        <div class="relative w-full sm:max-w-xs">
          <div
            class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none"
          >
            <svg
              class="h-5 w-5 text-gray-400"
              xmlns="http://www.w3.org/2000/svg"
              viewBox="0 0 20 20"
              fill="currentColor"
            >
              <path
                fill-rule="evenodd"
                d="M8 4a4 4 0 100 8 4 4 0 000-8zM2 8a6 6 0 1110.89 3.476l4.817 4.817a1 1 0 01-1.414 1.414l-4.816-4.816A6 6 0 012 8z"
                clip-rule="evenodd"
              />
            </svg>
          </div>
          <input
            type="text"
            v-model="searchTerm"
            @keyup="searchProduct"
            placeholder="Search products..."
            class="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
          />
        </div>
        <button
          @click="openAddProdModal"
          class="w-full sm:w-auto bg-blue-600 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded-lg shadow-md transition-transform transform hover:scale-105"
        >
          + Add Product
        </button>
      </div>

      <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <table class="min-w-full leading-normal">
          <thead class="bg-gray-100">
            <tr>
              <th
                class="px-5 py-3 border-b-2 border-gray-200 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider"
              >
                id
              </th>
              <th
                class="px-5 py-3 border-b-2 border-gray-200 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider"
              >
                Name
              </th>
              <th
                class="px-5 py-3 border-b-2 border-gray-200 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider"
              >
                Price
              </th>
              <th
                class="px-5 py-3 border-b-2 border-gray-200 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider"
              >
                Actions
              </th>
            </tr>
          </thead>
          <tbody id="product-table-body">
            <tr v-if="filteredProduct.length === 0">
              <td colspan="4" class="text-center py-4 text-gray-500">
                No product found.
              </td>
            </tr>
            <tr v-for="prod in filteredProduct" :key="prod.id">
              <td class="px-5 py-4 border-b border-gray-200 bg-white text-sm">
                <p class="text-gray-900 whitespace-no-wrap">{{prod.id }}</p>
              </td>
              <td class="px-5 py-4 border-b border-gray-200 bg-white text-sm">
                <p class="text-gray-900 whitespace-no-wrap">{{ prod.name }}</p>
              </td>
              <td class="px-5 py-4 border-b border-gray-200 bg-white text-sm">
                <p class="text-gray-900 whitespace-no-wrap">
                  $ {{prod.price }}
                </p>
              </td>
              <td class="px-5 py-4 border-b border-gray-200 bg-white text-sm">
                <button
                  @click="openDeleteModal(prod.id)"
                  class="delete-btn text-red-600 hover:text-red-900"
                >
                  Delete
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <div
      v-if="isAddModalOpen"
      @click.self="closeAddProdModal"
      class="fixed inset-0 bg-gray-900 bg-opacity-50 flex items-center justify-center modal-backdrop"
    >
      <div
        class="bg-white rounded-lg shadow-xl w-full max-w-md p-6 modal-content"
      >
        <div class="flex justify-between items-center border-b pb-3 mb-4">
          <h2 class="text-2xl font-semibold">Add Product</h2>
          <button
            @click="closeAddProdModal"
            class="text-gray-500 hover:text-gray-800 text-2xl"
          >
            &times;
          </button>
        </div>
        <form @submit.prevent="addProduct">
          <div class="mb-4">
            <label
              for="name"
              class="block text-sm font-medium text-gray-700 mb-1"
              >Name</label
            >
            <input
              type="text"
              v-model="newProduct.name"
              id="name"
              class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
              required
            />
          </div>
          <div class="mb-4">
            <label
              for="price"
              class="block text-sm font-medium text-gray-700 mb-1"
              >Price</label
            >
            <input
              type="number"
              v-model="newProduct.price"
              step="any"
              id="price"
              class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
              required
            />
          </div>

          <div class="flex justify-end space-x-4">
            <button
              type="button"
              @click="closeAddProdModal"
              class="bg-gray-300 hover:bg-gray-400 text-gray-800 font-bold py-2 px-4 rounded-lg"
            >
              Cancel
            </button>
            <button
              type="submit"
              class="bg-blue-600 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded-lg"
            >
              Save
            </button>
          </div>
        </form>
      </div>
    </div>

    <div
      v-if="isDeleteModalOpen"
      @click.self="closeDeleteModal"
      class="fixed inset-0 bg-gray-900 bg-opacity-50 flex items-center justify-center modal-backdrop"
    >
      <div
        class="bg-white rounded-lg shadow-xl w-full max-w-sm p-6 modal-content"
      >
        <h2 class="text-xl font-semibold mb-4">Confirm Deletion</h2>
        <p class="text-gray-600 mb-6">
          Are you sure you want to delete product with ID
          <span class="font-semibold">{{ prToDelete }}</span
          >? This action cannot be undone.
        </p>
        <div class="flex justify-end space-x-4">
          <button
            @click="closeDeleteModal"
            class="bg-gray-300 hover:bg-gray-400 text-gray-800 font-bold py-2 px-4 rounded-lg"
          >
            Cancel
          </button>
          <button
            @click="confirmDelete"
            class="bg-red-600 hover:bg-red-700 text-white font-bold py-2 px-4 rounded-lg"
          >
            Delete
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from "vue";

const searchTerm = ref("");
const searchResults = ref([]);
const isAddModalOpen = ref(false);
const isDeleteModalOpen = ref(false);
const prToDelete = ref(null);

const newProduct = ref({
  name: "",
  price: 0,
});

const filteredProduct = computed(() => {
  return searchTerm.value ? searchResults.value : allProduct.value;
});

const apiUrl = "http://localhost:8080";
const {
  data: allProduct,
  pending,
  error,
  refresh: refreshData,
} = await useFetch(`/products`, {
  baseURL: apiUrl,
});

const openAddProdModal = () => {
  newProduct.value = { name: "", price: 0 };
  isAddModalOpen.value = true;
};

const closeAddProdModal = () => {
  isAddModalOpen.value = false;
};

async function searchProduct() {
  if (!searchTerm.value) {
    searchResults.value = allProduct.value;
    return;
  }

  const res = await $fetch(`/products/search?name=${searchTerm.value}`, {
    baseURL: apiUrl,
  });
  searchResults.value = res;
}

const addProduct = async () => {
  try {
    await $fetch("/products", {
      baseURL: apiUrl,
      method: "post",
      body: newProduct.value,
    });
    if (error.value) {
      throw error.value;
    }
    refreshData();
    isAddModalOpen.value = false;
  } catch (error) {
    throw error;
  }
};

const openDeleteModal = (id) => {
  if (!id) {
    return;
  }
  prToDelete.value = id;
  isDeleteModalOpen.value = true;
};

const closeDeleteModal = () => {
  isDeleteModalOpen.value = false;
  prToDelete.value = null;
};

const confirmDelete = async () => {
  try {
    await $fetch(`/products/${prToDelete.value}`, {
      method: "DELETE",
      baseURL: apiUrl,
    });
    refreshData();
    prToDelete.value = null;
    isDeleteModalOpen.value = false;
  } catch (error) {
    console.log(error);
  }
};
</script>

<style>
body {
  font-family: "Inter", sans-serif;
}
.modal-backdrop {
  transition: opacity 0.3s ease;
}
.modal-content {
  transition: transform 0.3s ease;
  transform: scale(1);
}
</style>
