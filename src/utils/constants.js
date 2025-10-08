// API Configuration
export const API_BASE_URL = 'http://localhost:8080/api';

// Validation Constants
export const VALIDATION = {
  ROOM_LENGTH: { MIN: 3, MAX: 15 },
  ROOM_WIDTH: { MIN: 3, MAX: 15 },
  BUDGET: { MIN: 500, MAX: 10000 },
};

// Furniture Colors by Category
export const FURNITURE_COLORS = {
  'Sofa': '#8B7355',
  'Coffee Table': '#D4A574',
  'TV Stand': '#6B5D4F',
  'Bookshelf': '#A0826D',
  'Side Table': '#E8C7A5',
  'Armchair': '#9B8577',
  'Dining Table': '#A0826D',
  'Chair': '#9B8577',
  'Bed': '#8B7355',
  'Desk': '#6B5D4F',
  'Dresser': '#A0826D',
  'Nightstand': '#E8C7A5',
  'Ottoman': '#9B8577',
  'Default': '#A0826D',
};

// Furniture Icons (emoji)
export const FURNITURE_ICONS = {
  'Sofa': '🛋️',
  'Coffee Table': '☕',
  'TV Stand': '📺',
  'Bookshelf': '📚',
  'Side Table': '🪑',
  'Armchair': '🪑',
  'Dining Table': '🍽️',
  'Chair': '🪑',
  'Bed': '🛏️',
  'Desk': '💼',
  'Dresser': '👔',
  'Nightstand': '🕯️',
  'Ottoman': '🪑',
  'Default': '🪑',
};

// Scale factor for rendering (pixels per meter)
export const PIXELS_PER_METER = 60;

// Error Messages
export const ERROR_MESSAGES = {
  NETWORK_ERROR: 'Unable to reach server. Please check your connection.',
  SERVER_ERROR: 'Server error occurred. Please try again.',
  TIMEOUT_ERROR: 'Request took too long, please try again.',
  VALIDATION_ERROR: 'Please check your input values.',
};
