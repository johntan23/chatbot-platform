import { useEffect, useState } from 'react'
import { api } from '../api'
import './ConversationList.css'

function ConversationList({ currentConversationId, onSelectConversation, onNewConversation }) {
  const [conversations, setConversations] = useState([])

 useEffect(() => {
     fetchConversations()
 }, [currentConversationId])

const fetchConversations = async () => {
    try {
      const response = await api.getConversations()
      setConversations(Array.isArray(response.data) ? response.data : [])
    } catch (error) {
      console.error('Error fetching conversations:', error)
    }
  }

  return (
    <div className="conversation-list">
      <div className="conversation-list-header">
        <h2>Conversations</h2>
        <button className="new-chat-button" onClick={onNewConversation}>
          + New
        </button>
      </div>

      <div className="conversations">
        {conversations.map((conv) => (
          <div
            key={conv.id}
            className={`conversation-item ${conv.id === currentConversationId ? 'active' : ''}`}
            onClick={() => onSelectConversation(conv.id)}
          >
            <span>{conv.title}</span>
            <span className="conversation-date">
              {new Date(conv.createdAt).toLocaleDateString()}
            </span>
          </div>
        ))}
      </div>
    </div>
  )
}

export default ConversationList