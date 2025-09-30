const API_BASE_URL = 'http://localhost:8080/api';

export async function fetchQuestions(amount, category, difficulty, sessionId) {
  let url = `${API_BASE_URL}/questions?amount=${amount}&difficulty=${difficulty}`;
  if (category !== "") {
    url += `&category=${category}`;
  }
  const response = await fetch(url, {
    method: 'GET',
    headers: {  
      'sessionId': sessionId,
    },
  });
  if (!response.ok) {
    console.log('API response error:', response);
    throw new Error(response.statusText);
  }
  const data = await response.json();
  return data;

}

export async function checkAnswers(answers) {
  console.log('Checking answers:', answers);
  const response = await fetch(`${API_BASE_URL}/checkanswers`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ answers }),
  });
  if (!response.ok) {
    throw new Error('Error checking answers');
  }
  return response.json();
}
