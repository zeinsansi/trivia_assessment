<script setup>
import { ref } from "vue";
import { checkAnswers } from "../helpers/api";
import Card from 'primevue/card';
import RadioButton from 'primevue/radiobutton';
import Button from 'primevue/button';
import { useToast } from 'primevue/usetoast';

const props = defineProps({
  questions: Array,
});
const emit = defineEmits(["finish"]);


const answers = ref({});
const currentIndex = ref(0);
const toast = useToast();

function nextQuestion() {
  if (currentIndex.value < props.questions.length - 1) {
    currentIndex.value++;
  }
}

// Submit answers to the API and emit the results
async function submit() {
    try {
        const payload = Object.entries(answers.value).map(([questionId, answer]) => ({
            questionId,
            answer,
        }));
        const res = await checkAnswers(payload);
        emit("finish", res);
    } catch (error) {
        toast.add({ severity: 'error', summary: 'Error', detail: 'Failed to submit answers', life: 3000 });
    }
}
</script>

<template>
  <div class="quiz-screen" v-if="props.questions[currentIndex]">
    <h3 class="question" v-html="`${currentIndex + 1}. ${props.questions[currentIndex].question}`"></h3>
    
    <div class="options">
      <div 
        v-for="option in props.questions[currentIndex].options" 
        :key="option"
        class="option-item"
      >
        <RadioButton 
          v-model="answers[props.questions[currentIndex].id]" 
          :inputId="option"
          :value="option" 
        />
        <label :for="option" v-html="option"></label>
      </div>
    </div>

    <Button 
      v-if="currentIndex < props.questions.length - 1"
      @click="nextQuestion"
      :disabled="!answers[props.questions[currentIndex].id]"
      label="Volgende"
      class="button"
    />
    
    <Button 
      v-else
      @click="submit"
      :disabled="!answers[props.questions[currentIndex].id]"
      label="Quiz Indienen"
      severity="success"
      class="button"
    />
  </div>
</template>

<style scoped>
.quiz-screen {
  width: 600px;
  margin: 2rem auto;
  padding: 2rem;
  background-color: #18181b;
  border-radius: 8px;
  text-align: center;
}

.question {
  display: flex;
  align-self: flex-start;
}

.options {
  margin: 1.5rem 0;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
}

.option-item {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  gap: 0.5rem;
}

.option-item label {
  cursor: pointer;
}

.button {
  margin-top: 1rem;
  width: 100%;
}
</style>