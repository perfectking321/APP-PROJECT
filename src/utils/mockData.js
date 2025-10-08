/**
 * Sample API response for testing without backend
 * Use this in development to test the UI without Spring Boot backend
 */

export const sampleLayoutResponse = {
  furniture: [
    {
      name: "Sofa",
      x: 1.5,
      y: 2.0,
      width: 2.2,
      length: 0.9,
      price: 899.99
    },
    {
      name: "Coffee Table",
      x: 1.8,
      y: 3.5,
      width: 1.2,
      length: 0.6,
      price: 249.99
    },
    {
      name: "TV Stand",
      x: 0.5,
      y: 0.5,
      width: 1.8,
      length: 0.4,
      price: 349.99
    },
    {
      name: "Bookshelf",
      x: 6.5,
      y: 0.5,
      width: 1.0,
      length: 0.3,
      price: 299.99
    },
    {
      name: "Armchair",
      x: 5.0,
      y: 2.0,
      width: 0.9,
      length: 0.9,
      price: 449.99
    },
    {
      name: "Side Table",
      x: 6.0,
      y: 2.0,
      width: 0.5,
      length: 0.5,
      price: 129.99
    }
  ],
  totalCost: 2379.94,
  reasoning: "This layout optimizes the living space by placing the sofa as the focal point with a clear view of the TV stand. The coffee table is positioned for easy access from the sofa. The bookshelf adds storage along the wall, while the armchair and side table create a cozy reading nook. All furniture fits comfortably within the budget while maximizing functionality and aesthetic appeal."
};

export const sampleFurnitureCatalog = [
  {
    id: 1,
    name: "Sofa",
    width: 2.2,
    length: 0.9,
    price: 899.99,
    category: "Seating"
  },
  {
    id: 2,
    name: "Coffee Table",
    width: 1.2,
    length: 0.6,
    price: 249.99,
    category: "Tables"
  },
  {
    id: 3,
    name: "TV Stand",
    width: 1.8,
    length: 0.4,
    price: 349.99,
    category: "Storage"
  },
  {
    id: 4,
    name: "Bookshelf",
    width: 1.0,
    length: 0.3,
    price: 299.99,
    category: "Storage"
  },
  {
    id: 5,
    name: "Armchair",
    width: 0.9,
    length: 0.9,
    price: 449.99,
    category: "Seating"
  },
  {
    id: 6,
    name: "Side Table",
    width: 0.5,
    length: 0.5,
    price: 129.99,
    category: "Tables"
  },
  {
    id: 7,
    name: "Dining Table",
    width: 1.8,
    length: 0.9,
    price: 599.99,
    category: "Tables"
  },
  {
    id: 8,
    name: "Chair",
    width: 0.5,
    length: 0.5,
    price: 149.99,
    category: "Seating"
  },
  {
    id: 9,
    name: "Bed",
    width: 2.0,
    length: 2.0,
    price: 1299.99,
    category: "Bedroom"
  },
  {
    id: 10,
    name: "Desk",
    width: 1.4,
    length: 0.7,
    price: 399.99,
    category: "Office"
  }
];

/**
 * Mock API functions for testing
 * Replace the real API calls with these in App.jsx for development
 */
export const mockApi = {
  generateLayout: async (roomData) => {
    // Simulate API delay
    await new Promise(resolve => setTimeout(resolve, 2000));
    
    // Return sample data
    return sampleLayoutResponse;
  },
  
  getAllFurniture: async () => {
    // Simulate API delay
    await new Promise(resolve => setTimeout(resolve, 500));
    
    // Return sample catalog
    return sampleFurnitureCatalog;
  }
};
