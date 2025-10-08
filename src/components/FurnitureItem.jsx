import React from 'react';
import { FURNITURE_COLORS, FURNITURE_ICONS } from '../utils/constants';

const FurnitureItem = ({ furniture, scale }) => {
  // Get color and icon for furniture type
  const color = FURNITURE_COLORS[furniture.name] || FURNITURE_COLORS['Default'];
  const icon = FURNITURE_ICONS[furniture.name] || FURNITURE_ICONS['Default'];

  // Calculate darker border color
  const darkenColor = (hex) => {
    const r = parseInt(hex.slice(1, 3), 16);
    const g = parseInt(hex.slice(3, 5), 16);
    const b = parseInt(hex.slice(5, 7), 16);
    return `rgb(${Math.max(0, r - 30)}, ${Math.max(0, g - 30)}, ${Math.max(0, b - 30)})`;
  };

  const borderColor = darkenColor(color);

  // Calculate position and size in pixels
  const left = furniture.x * scale;
  const top = furniture.y * scale;
  const width = furniture.width * scale;
  const height = furniture.length * scale;

  return (
    <div
      className="furniture-item absolute rounded-lg shadow-md cursor-grab flex flex-col items-center justify-center text-center"
      style={{
        left: `${left}px`,
        top: `${top}px`,
        width: `${width}px`,
        height: `${height}px`,
        backgroundColor: color,
        border: `2px solid ${borderColor}`,
      }}
      title={`${furniture.name} - $${furniture.price.toFixed(2)}`}
    >
      {/* Icon */}
      <div className="text-2xl md:text-3xl" style={{ filter: 'drop-shadow(0 1px 2px rgba(0,0,0,0.3))' }}>
        {icon}
      </div>
      
      {/* Label */}
      <div className="text-xs md:text-sm font-semibold text-white mt-1 px-2" 
           style={{ textShadow: '1px 1px 2px rgba(0,0,0,0.5)' }}>
        {furniture.name}
      </div>
    </div>
  );
};

export default FurnitureItem;
