import React, { useState } from 'react';
import RoomForm from './components/RoomForm';
import LayoutDisplay from './components/LayoutDisplay';
import LoadingSpinner from './components/LoadingSpinner';
import { api } from './services/api';

function App() {
  const [roomData, setRoomData] = useState(null);
  const [layoutResult, setLayoutResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  /**
   * Handle form submission and API call
   */
  const handleFormSubmit = async (formData) => {
    setLoading(true);
    setError(null);

    try {
      // Call API to generate layout
      const result = await api.generateLayout(formData);
      
      // Store room data and layout result
      setRoomData(formData);
      setLayoutResult(result);
    } catch (err) {
      // Handle error
      setError(err);
      console.error('Error generating layout:', err);
    } finally {
      setLoading(false);
    }
  };

  /**
   * Reset to initial state
   */
  const handleReset = () => {
    setRoomData(null);
    setLayoutResult(null);
    setError(null);
  };

  return (
    <div className="App">
      {/* Loading Overlay */}
      {loading && <LoadingSpinner />}

      {/* Show Error or Layout Display or Form */}
      {error ? (
        // Error State
        <div className="min-h-screen flex items-center justify-center p-4">
          <div className="glass-card rounded-3xl shadow-2xl p-8 md:p-12 max-w-md text-center animate-scale-in">
            <div className="text-6xl mb-4">❌</div>
            <h2 className="text-2xl font-bold text-red-600 mb-4">
              Oops! Something Went Wrong
            </h2>
            <p className="text-gray-700 mb-6">{error}</p>
            <button
              onClick={handleReset}
              className="px-6 py-3 bg-gradient-to-r from-warm-500 to-warm-400 text-white font-semibold rounded-lg hover:from-warm-600 hover:to-warm-500 hover:scale-105 transition-all shadow-md"
            >
              Try Again
            </button>
          </div>
        </div>
      ) : layoutResult && roomData ? (
        // Layout Display State
        <LayoutDisplay
          layoutResult={layoutResult}
          roomData={roomData}
          onReset={handleReset}
        />
      ) : (
        // Form State
        <RoomForm onSubmit={handleFormSubmit} loading={loading} />
      )}
    </div>
  );
}

export default App;
