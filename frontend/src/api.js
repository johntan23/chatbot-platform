import axios from 'axios'

const API_BASE_URL = 'http://localhost:8080/api/chat'

export const api = {
  sendMessage: (data) => axios.post(`${API_BASE_URL}/message`, data),
  getConversations: () => axios.get(`${API_BASE_URL}/conversations`),
  getMessages: (id) => axios.get(`${API_BASE_URL}/conversations/${id}/messages`)
}