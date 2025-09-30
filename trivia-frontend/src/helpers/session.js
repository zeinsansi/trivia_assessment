import { v4 as uuidv4 } from "uuid";

// Retrieve or create a unique session ID and store it in localStorage
export function getOrCreateSessionId() {
  let sessionId = localStorage.getItem("sessionId");
  if (!sessionId) {
    sessionId = uuidv4();
    localStorage.setItem("sessionId", sessionId);
  }
  return sessionId;
}