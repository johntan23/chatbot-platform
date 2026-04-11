import { useState } from 'react'
import Settings from './Settings'
import ConversationList from './ConversationList'
import { api } from '../api'
import './Chat.css'

function Chat() {
  const [messages, setMessages] = useState([])
  const [input, setInput] = useState('')
  const [conversationId, setConversationId] = useState(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)
  const [showSettings, setShowSettings] = useState(false)
  const [systemPrompt, setSystemPrompt] = useState('')
  const [temperature, setTemperature] = useState(0.7)

  const sendMessage = async () => {
    if (!input.trim()) return

    const userMessage = { role: 'USER', content: input }
    setMessages(prev => [...prev, userMessage])
    setInput('')
    setLoading(true)
    setError(null)

    try {
      const response = await api.sendMessage({
        conversationId,
        message: input,
        systemPrompt: systemPrompt || null,
        temperature
      })

      setConversationId(response.data.conversationId)
      setMessages(prev => [...prev, {
        role: 'ASSISTANT',
        content: response.data.message
      }])
    } catch (err) {
      console.error('Error:', err)
      setError('Failed to send message. Please try again.')
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

  const handleSelectConversation = async (id) => {
    try {
      const response = await api.getMessages(id)
      setMessages(response.data.map(msg => ({
        role: msg.role,
        content: msg.content
      })))
      setConversationId(id)
      setError(null)
    } catch (err) {
      console.error('Error loading conversation:', err)
      setError('Failed to load conversation.')
    }
  }

  const handleNewConversation = () => {
    setMessages([])
    setConversationId(null)
    setError(null)
  }

  return (
    <div className="app-container">
      <ConversationList
        currentConversationId={conversationId}
        onSelectConversation={handleSelectConversation}
        onNewConversation={handleNewConversation}
      />

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
            <div key={`${msg.role}-${index}`} className={`message ${msg.role.toLowerCase()}`}>
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
          {error && (
            <div className="error-message">
              {error}
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