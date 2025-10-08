import React from 'react';
import { Loader2 } from 'lucide-react';

const LoadingSpinner = () => {
  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-40 backdrop-blur-sm animate-fade-in">
      <div className="glass-card rounded-3xl p-8 text-center max-w-sm">
        <Loader2 className="w-16 h-16 mx-auto mb-4 animate-spin text-warm-500" />
        <h3 className="text-xl font-semibold text-charcoal mb-2">
          Generating Your Perfect Layout
        </h3>
        <p className="text-gray-600 text-sm">
          Our AI is designing the ideal furniture arrangement for your space...
        </p>
      </div>
    </div>
  );
};

export default LoadingSpinner;
