import axios from 'axios';
import { API_BASE_URL, ERROR_MESSAGES } from '../utils/constants';

// Create axios instance with default config
const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 30000, // 30 seconds
  headers: {
    'Content-Type': 'application/json',
  },
});

// API methods
export const api = {
  /**
   * Get all furniture items from catalog
   * @returns {Promise} Array of furniture items
   */
  getAllFurniture: async () => {
    try {
      const response = await apiClient.get('/furniture');
      return response.data;
    } catch (error) {
      throw handleApiError(error);
    }
  },

  /**
   * Generate room layout with AI
   * @param {Object} roomData - Room configuration { length, width, budget }
   * @returns {Promise} Layout result with furniture placements
   */
  generateLayout: async (roomData) => {
    try {
      const response = await apiClient.post('/layout', roomData);
      return response.data;
    } catch (error) {
      throw handleApiError(error);
    }
  },
};

/**
 * Handle API errors and return user-friendly messages
 * @param {Error} error - Axios error object
 * @returns {string} Error message
 */
export const handleApiError = (error) => {
  if (error.code === 'ECONNABORTED') {
    return ERROR_MESSAGES.TIMEOUT_ERROR;
  }

  if (error.response) {
    // Server responded with error status
    const status = error.response.status;
    const message = error.response.data?.message || error.response.data?.error;

    if (status >= 500) {
      return message || ERROR_MESSAGES.SERVER_ERROR;
    }

    if (status >= 400) {
      return message || ERROR_MESSAGES.VALIDATION_ERROR;
    }

    return message || 'An error occurred';
  } else if (error.request) {
    // Request made but no response
    return ERROR_MESSAGES.NETWORK_ERROR;
  } else {
    // Something else happened
    return error.message || 'An unexpected error occurred';
  }
};

export default api;
