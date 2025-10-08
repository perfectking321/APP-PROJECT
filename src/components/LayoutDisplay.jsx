import React from 'react';
import FurnitureItem from './FurnitureItem';
import CostSummary from './CostSummary';
import { PIXELS_PER_METER, FURNITURE_ICONS } from '../utils/constants';
import { Home, MapPin, DollarSign } from 'lucide-react';

const LayoutDisplay = ({ layoutResult, roomData, onReset }) => {
  const { furniture = [] } = layoutResult;

  // Calculate dimensions for floor plan
  const roomWidthM = roomData.width;
  const roomLengthM = roomData.length;
  const scale = PIXELS_PER_METER;

  const containerWidth = roomWidthM * scale;
  const containerHeight = roomLengthM * scale;

  // Add padding for better visualization
  const padding = 20;
  const totalWidth = containerWidth + padding * 2;
  const totalHeight = containerHeight + padding * 2;

  return (
    <div className="min-h-screen p-4 md:p-8 animate-fade-in">
      {/* Header */}
      <div className="max-w-7xl mx-auto mb-6">
        <div className="glass-card rounded-2xl p-6 shadow-lg">
          <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
            <div>
              <h1 className="text-3xl font-bold text-warm-500 mb-2 flex items-center">
                <Home className="w-8 h-8 mr-3" />
                Your Perfect Room Layout
              </h1>
              <p className="text-gray-600">
                {roomWidthM}m × {roomLengthM}m room with ${roomData.budget} budget
              </p>
            </div>
            <button
              onClick={onReset}
              className="px-6 py-3 bg-gradient-to-r from-warm-500 to-warm-400 text-white font-semibold rounded-lg hover:from-warm-600 hover:to-warm-500 hover:scale-105 transition-all shadow-md"
            >
              🎨 Design Another Room
            </button>
          </div>
        </div>
      </div>

      {/* Main Content */}
      <div className="max-w-7xl mx-auto grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Floor Plan - Takes 2 columns on large screens */}
        <div className="lg:col-span-2">
          <div className="glass-card rounded-2xl p-6 shadow-xl">
            <h2 className="text-2xl font-bold text-charcoal mb-4 flex items-center">
              <MapPin className="w-6 h-6 mr-2 text-warm-500" />
              Floor Plan (2D View)
            </h2>

            {/* Floor Plan Container */}
            <div className="bg-warm-100 rounded-xl p-4 overflow-auto">
              <div
                className="relative mx-auto bg-white border-4 border-warm-600 rounded-lg"
                style={{
                  width: `${totalWidth}px`,
                  height: `${totalHeight}px`,
                  backgroundImage: `
                    linear-gradient(0deg, transparent 24%, rgba(200, 200, 200, .1) 25%, rgba(200, 200, 200, .1) 26%, transparent 27%, transparent 74%, rgba(200, 200, 200, .1) 75%, rgba(200, 200, 200, .1) 76%, transparent 77%, transparent),
                    linear-gradient(90deg, transparent 24%, rgba(200, 200, 200, .1) 25%, rgba(200, 200, 200, .1) 26%, transparent 27%, transparent 74%, rgba(200, 200, 200, .1) 75%, rgba(200, 200, 200, .1) 76%, transparent 77%, transparent)
                  `,
                  backgroundSize: '50px 50px',
                }}
              >
                {/* Room dimensions label */}
                <div className="absolute -top-8 left-1/2 transform -translate-x-1/2 text-sm font-semibold text-warm-600">
                  {roomWidthM}m × {roomLengthM}m
                </div>

                {/* Furniture Items */}
                <div
                  className="relative"
                  style={{
                    width: `${containerWidth}px`,
                    height: `${containerHeight}px`,
                    margin: `${padding}px`,
                  }}
                >
                  {furniture.map((item, index) => (
                    <FurnitureItem key={index} furniture={item} scale={scale} />
                  ))}
                </div>
              </div>

              {/* Legend */}
              <div className="mt-4 text-xs text-gray-600 text-center">
                💡 Hover over furniture to see details | Scale: 1m = {scale}px
              </div>
            </div>
          </div>

          {/* Furniture List */}
          <div className="glass-card rounded-2xl p-6 shadow-xl mt-6">
            <h2 className="text-2xl font-bold text-charcoal mb-4">
              📦 Furniture Details
            </h2>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {furniture.map((item, index) => {
                // Handle both nested (FurniturePosition) and flat structure
                const furnitureData = item.furniture || item;
                const xPos = item.x ?? 0;
                const yPos = item.y ?? 0;
                
                return (
                  <div
                    key={index}
                    className="bg-white rounded-xl p-4 shadow hover:shadow-lg hover:scale-105 transition-all cursor-pointer"
                  >
                    <div className="flex items-start justify-between">
                      <div className="flex items-center space-x-3">
                        <span className="text-3xl">
                          {FURNITURE_ICONS[furnitureData.name] || FURNITURE_ICONS['Default']}
                        </span>
                        <div>
                          <h3 className="font-bold text-charcoal text-lg">
                            {furnitureData.name}
                          </h3>
                          <p className="text-sm text-gray-600 flex items-center mt-1">
                            <MapPin className="w-3 h-3 mr-1" />
                            At ({xPos.toFixed(1)}m, {yPos.toFixed(1)}m)
                          </p>
                          <p className="text-sm text-gray-600">
                            Size: {(furnitureData.width ?? 0).toFixed(1)}m × {(furnitureData.length ?? 0).toFixed(1)}m
                          </p>
                        </div>
                      </div>
                      <div className="text-right">
                        <div className="flex items-center justify-end text-warm-600 font-bold text-lg">
                          <DollarSign className="w-4 h-4" />
                          {(furnitureData.price ?? 0).toFixed(2)}
                        </div>
                      </div>
                    </div>
                  </div>
                );
              })}
            </div>

            {furniture.length === 0 && (
              <p className="text-center text-gray-500 py-8">
                No furniture items placed in this layout
              </p>
            )}
          </div>
        </div>

        {/* Cost Summary Sidebar */}
        <div className="lg:col-span-1">
          <div className="sticky top-8">
            <CostSummary layoutResult={layoutResult} roomData={roomData} />
          </div>
        </div>
      </div>
    </div>
  );
};

export default LayoutDisplay;
