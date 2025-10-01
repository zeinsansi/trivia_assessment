<script setup>
import Button from 'primevue/button';

const props = defineProps({
    results: Object,
});
</script>

<template>
    <div class="result-screen">
        <h2>Je score: {{ results.score }} / {{ results.total }}</h2>

        <div class="results-list">
            <div v-for="r in results.results" :key="r.id" class="result-item"
                :class="{ 'correct': r.correct, 'incorrect': !r.correct }">
                <p><strong v-html="r.question"></strong></p>
                <p>Jouw antwoord: <span v-html="r.yourAnswer"></span></p>
                <p>Correct antwoord: <span v-html="r.correctAnswer"></span></p>
            </div>
        </div>

        <Button @click="$emit('restart')" label="Opnieuw Starten" class="restart-button" />
    </div>
</template>

<style scoped>
.result-screen {
    width: 800px;
    margin: 2rem auto;
    padding: 2rem;
    background-color: #18181b;
    border-radius: 8px;
    text-align: left;
}

.results-list {
    margin: 1.5rem 0;
}

.result-item {
    padding: 1rem;
    margin-bottom: 1rem;
    border-radius: 6px;
    border-left: 4px solid;
}

.result-item.correct {
    background-color: #065f46;
    border-left-color: #10b981;
}

.result-item.incorrect {
    background-color: #7f1d1d;
    border-left-color: #ef4444;
}

.result-item p {
    margin-bottom: 0.5rem;
}
.restart-button {
    margin-top: 1rem;
    width: 100%;
}
</style>