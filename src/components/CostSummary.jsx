import React from 'react';
import { DollarSign, Package, TrendingUp, AlertCircle } from 'lucide-react';

const CostSummary = ({ layoutResult, roomData }) => {
  // Calculate totals
  const totalItems = layoutResult.furniture?.length || 0;
  const totalCost = layoutResult.totalCost || 0;
  const budget = roomData.budget || 0;
  const remaining = budget - totalCost;
  const budgetUsedPercentage = budget > 0 ? (totalCost / budget) * 100 : 0;

  // Determine progress bar color based on usage
  const getProgressColor = () => {
    if (budgetUsedPercentage > 95) return 'bg-red-500';
    if (budgetUsedPercentage > 80) return 'bg-yellow-500';
    return 'bg-gradient-to-r from-warm-500 to-accent';
  };

  return (
    <div className="glass-card rounded-2xl p-6 shadow-xl animate-scale-in">
      <h3 className="text-2xl font-bold text-charcoal mb-6 flex items-center">
        <DollarSign className="w-6 h-6 mr-2 text-warm-500" />
        Cost Summary
      </h3>

      <div className="space-y-4">
        {/* Total Items */}
        <div className="flex items-center justify-between p-3 bg-white bg-opacity-50 rounded-lg">
          <div className="flex items-center">
            <Package className="w-5 h-5 mr-2 text-warm-500" />
            <span className="font-medium text-charcoal">Items Placed</span>
          </div>
          <span className="text-lg font-bold text-warm-600">{totalItems}</span>
        </div>

        {/* Budget Progress */}
        <div className="p-3 bg-white bg-opacity-50 rounded-lg">
          <div className="flex items-center justify-between mb-2">
            <div className="flex items-center">
              <TrendingUp className="w-5 h-5 mr-2 text-warm-500" />
              <span className="font-medium text-charcoal">Budget Used</span>
            </div>
            <span className="text-sm font-semibold text-gray-700">
              {budgetUsedPercentage.toFixed(1)}%
            </span>
          </div>

          {/* Progress Bar */}
          <div className="w-full bg-gray-200 rounded-full h-3 overflow-hidden">
            <div
              className={`h-full transition-all duration-500 ${getProgressColor()}`}
              style={{ width: `${Math.min(budgetUsedPercentage, 100)}%` }}
            />
          </div>

          {/* Budget Details */}
          <div className="mt-3 space-y-1 text-sm">
            <div className="flex justify-between">
              <span className="text-gray-600">Spent:</span>
              <span className="font-semibold text-charcoal">
                ${totalCost.toFixed(2)}
              </span>
            </div>
            <div className="flex justify-between">
              <span className="text-gray-600">Budget:</span>
              <span className="font-semibold text-charcoal">
                ${budget.toFixed(2)}
              </span>
            </div>
            <div className="flex justify-between border-t border-gray-300 pt-1 mt-1">
              <span className="text-gray-600">Remaining:</span>
              <span
                className={`font-bold ${
                  remaining >= 0 ? 'text-green-600' : 'text-red-600'
                }`}
              >
                ${remaining.toFixed(2)}
              </span>
            </div>
          </div>
        </div>

        {/* AI Reasoning */}
        {layoutResult.reasoning && (
          <div className="p-3 bg-white bg-opacity-50 rounded-lg">
            <div className="flex items-start mb-2">
              <AlertCircle className="w-5 h-5 mr-2 text-warm-500 flex-shrink-0 mt-0.5" />
              <span className="font-medium text-charcoal">AI Insights</span>
            </div>
            <p className="text-sm text-gray-700 italic leading-relaxed ml-7">
              {layoutResult.reasoning}
            </p>
          </div>
        )}

        {/* Warning if over budget */}
        {remaining < 0 && (
          <div className="p-3 bg-red-50 border border-red-200 rounded-lg animate-fade-in">
            <div className="flex items-start">
              <AlertCircle className="w-5 h-5 mr-2 text-red-600 flex-shrink-0 mt-0.5" />
              <div>
                <p className="text-sm font-semibold text-red-800">Over Budget</p>
                <p className="text-xs text-red-600 mt-1">
                  The layout exceeds your budget by ${Math.abs(remaining).toFixed(2)}
                </p>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default CostSummary;
