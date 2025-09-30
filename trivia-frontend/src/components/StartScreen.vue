<script setup>
import { nextTick, onMounted, ref } from 'vue';
import { fetchQuestions } from '../helpers/api';
import { getOrCreateSessionId } from '../helpers/session';
import Button from "primevue/button"
import Select from 'primevue/select';
import InputNumber from 'primevue/inputnumber';
import { useToast } from 'primevue/usetoast';



const emit = defineEmits(["start"]);
const categories = ref([]);
const category = ref(null);
const amount = ref(10);
const difficulty = ref("easy");
const sessionId = ref(getOrCreateSessionId());
const toast = useToast();

onMounted(async () => {
    try {
        // Fetch categories from the Open Trivia API
        const response = await fetch('https://opentdb.com/api_category.php')
        const data = await response.json();
        categories.value = data.trivia_categories;

        // Add an "Any Category" option at the beginning
        categories.value.unshift({ id: "", name: "Any Category" });
    } catch (error) {
        toast.add({ severity: 'error', summary: 'Error', detail: 'Failed to load categories', life: 3000 });
    }
});

async function startQuiz() {
    try {
        const response = await fetchQuestions(amount.value, category.value, difficulty.value, sessionId.value);
        emit("start", response);
    } catch (error) {
        toast.add({ severity: 'error', summary: 'Error', detail: 'Failed to get questions', life: 3000 });
    }
}
</script>

<template>
    <div class="start-screen">
        <div class="category-select">
            <label>Categorie: </label>
            <Select v-model="category" :options="categories" optionLabel="name" optionValue="id"
                placeholder="Selecteer een categorie" />
        </div>
        <div class="amount-select">
            <label>Aantal vragen: </label>
            <InputNumber v-model="amount" inputId="minmax-buttons" mode="decimal" showButtons :min="1" :max="50" />
        </div>
        <div class="difficulty-select">
            <label>Moeilijkheid: </label>
            <Select v-model="difficulty" :options="['easy', 'medium', 'hard']" />
        </div>
        <Button @click="startQuiz" :disabled="category === null" class="select-button">Start Quiz</Button>
    </div>
</template>

<style scoped>
.start-screen {
    width: 600px;
    margin: 2rem auto;
    padding: 2rem;
    background-color: #18181b;
    border-radius: 8px;
}

.category-select,
.amount-select,
.difficulty-select {
    margin-bottom: 0.5rem;
}

.select-button {
    margin-top: 1rem;
    width: 100%;
}
</style>
