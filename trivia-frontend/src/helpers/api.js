const API_BASE_URL = 'https://zeintrivia.up.railway.app/api';

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
  const data = await response.json();
  if (!response.ok) {
    throw new Error(data.message || 'Unknown error occurred while fetching questions');
  }
  return data;

}

export async function checkAnswers(answers) {
  const response = await fetch(`${API_BASE_URL}/checkanswers`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ answers }),
  });
  const data = await response.json();
  if (!response.ok) {
    throw new Error(data.message || 'Unknown error occurred while checking answers');
  }
  return data;
}
