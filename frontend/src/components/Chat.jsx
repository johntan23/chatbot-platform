import { useState } from 'react'
import axios from 'axios'
import Settings from './Settings'
import './Chat.css'

function Chat() {
  const [messages, setMessages] = useState([])
  const [input, setInput] = useState('')
  const [conversationId, setConversationId] = useState(null)
  const [loading, setLoading] = useState(false)
  const [showSettings, setShowSettings] = useState(false)
  const [systemPrompt, setSystemPrompt] = useState('')
  const [temperature, setTemperature] = useState(0.7)

  const sendMessage = async () => {
    if (!input.trim()) return

    const userMessage = { role: 'USER', content: input }
    setMessages(prev => [...prev, userMessage])
    setInput('')
    setLoading(true)

    try {
      const response = await axios.post('http://localhost:8080/api/chat/message', {
        conversationId: conversationId,
        message: input,
        systemPrompt: systemPrompt || null,
        temperature: temperature
      })

      setConversationId(response.data.conversationId)
      setMessages(prev => [...prev, {
        role: 'ASSISTANT',
        content: response.data.message
      }])
    } catch (error) {
      console.error('Error:', error)
    } finally {
      setLoading(false)
    }
  }

  const handleKeyPress = (e) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault()
      sendMessage()
    }
  }

  return (
    <div className="app-container">
      <div className="chat-container">
        <div className="chat-header">
          <h1>AI Chatbot</h1>
          <button
            className="settings-button"
            onClick={() => setShowSettings(!showSettings)}
          >
            ⚙️ Settings
          </button>
        </div>

        <div className="chat-messages">
          {messages.map((msg, index) => (
            <div key={index} className={`message ${msg.role.toLowerCase()}`}>
              <div className="message-bubble">
                {msg.content}
              </div>
            </div>
          ))}
          {loading && (
            <div className="message assistant">
              <div className="message-bubble loading">
                Typing...
              </div>
            </div>
          )}
        </div>

        <div className="chat-input">
          <textarea
            value={input}
            onChange={(e) => setInput(e.target.value)}
            onKeyDown={handleKeyPress}
            placeholder="Type a message..."
            rows={1}
          />
          <button onClick={sendMessage} disabled={loading}>
            Send
          </button>
        </div>
      </div>

      {showSettings && (
        <Settings
          systemPrompt={systemPrompt}
          temperature={temperature}
          onSystemPromptChange={setSystemPrompt}
          onTemperatureChange={setTemperature}
        />
      )}
    </div>
  )
}

export default Chat