import './Settings.css'

function Settings({ systemPrompt, temperature, onSystemPromptChange, onTemperatureChange }) {
  return (
    <div className="settings-panel">
      <h2>Settings</h2>

      <div className="setting-group">
        <label>System Prompt</label>
        <textarea
          value={systemPrompt}
          onChange={(e) => onSystemPromptChange(e.target.value)}
          rows={6}
          placeholder="Enter system prompt..."
        />
      </div>

      <div className="setting-group">
        <label>Temperature: {temperature}</label>
        <input
          type="range"
          min="0"
          max="1"
          step="0.1"
          value={temperature}
          onChange={(e) => onTemperatureChange(parseFloat(e.target.value))}
        />
        <div className="temperature-labels">
          <span>Precise (0.0)</span>
          <span>Creative (1.0)</span>
        </div>
      </div>
    </div>
  )
}

export default Settings