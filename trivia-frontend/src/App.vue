<script setup>
import QuizScreen from './components/QuizScreen.vue';
import ResultScreen from './components/ResultScreen.vue';
import StartScreen from './components/StartScreen.vue';
import { ref } from 'vue';
import Toast from 'primevue/toast';

const stage = ref('start');
const questions = ref([]);
const results = ref(null);

function handleStart(questionsData) {
  questions.value = questionsData;
  stage.value = 'quiz';
}

function handleFinish(resultsData) {
  results.value = resultsData;
  stage.value =  'result';
}

function handleRestart() {
  questions.value = [];
  results.value = null;
  stage.value = 'start';
}

</script>

<template>
  <div>
    <Toast />
    <h1>Quad Trivia</h1>
    <StartScreen v-if="stage ==='start'" @start="handleStart" />
    <QuizScreen v-if="stage === 'quiz'" :questions="questions" @finish="handleFinish" />
    <ResultScreen v-if="stage ==='result'":results="results" @restart="handleRestart" />
  </div>
</template>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: 'Montserrat', sans-serif;

}

body {
  background-color: #271c36;
  color: white;
}
</style>
