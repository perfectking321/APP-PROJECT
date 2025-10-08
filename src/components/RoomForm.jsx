import React, { useState } from 'react';
import { Ruler, Square, DollarSign, Sparkles, AlertCircle } from 'lucide-react';
import { VALIDATION } from '../utils/constants';

const RoomForm = ({ onSubmit, loading }) => {
  const [formData, setFormData] = useState({
    length: '',
    width: '',
    budget: '',
  });

  const [errors, setErrors] = useState({});
  const [touched, setTouched] = useState({});

  // Validation functions
  const validateField = (name, value) => {
    const numValue = parseFloat(value);

    switch (name) {
      case 'length':
        if (!value) return 'Room length is required';
        if (isNaN(numValue)) return 'Please enter a valid number';
        if (numValue < VALIDATION.ROOM_LENGTH.MIN || numValue > VALIDATION.ROOM_LENGTH.MAX) {
          return `Length must be between ${VALIDATION.ROOM_LENGTH.MIN}-${VALIDATION.ROOM_LENGTH.MAX} meters`;
        }
        return '';

      case 'width':
        if (!value) return 'Room width is required';
        if (isNaN(numValue)) return 'Please enter a valid number';
        if (numValue < VALIDATION.ROOM_WIDTH.MIN || numValue > VALIDATION.ROOM_WIDTH.MAX) {
          return `Width must be between ${VALIDATION.ROOM_WIDTH.MIN}-${VALIDATION.ROOM_WIDTH.MAX} meters`;
        }
        return '';

      case 'budget':
        if (!value) return 'Budget is required';
        if (isNaN(numValue)) return 'Please enter a valid number';
        if (numValue < VALIDATION.BUDGET.MIN || numValue > VALIDATION.BUDGET.MAX) {
          return `Budget must be between $${VALIDATION.BUDGET.MIN}-$${VALIDATION.BUDGET.MAX}`;
        }
        return '';

      default:
        return '';
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));

    // Validate on change if field has been touched
    if (touched[name]) {
      const error = validateField(name, value);
      setErrors((prev) => ({ ...prev, [name]: error }));
    }
  };

  const handleBlur = (e) => {
    const { name, value } = e.target;
    setTouched((prev) => ({ ...prev, [name]: true }));

    // Validate on blur
    const error = validateField(name, value);
    setErrors((prev) => ({ ...prev, [name]: error }));

    // Format budget as currency on blur
    if (name === 'budget' && value && !isNaN(parseFloat(value))) {
      const formatted = parseFloat(value).toFixed(0);
      setFormData((prev) => ({ ...prev, [name]: formatted }));
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    // Validate all fields
    const newErrors = {};
    Object.keys(formData).forEach((key) => {
      const error = validateField(key, formData[key]);
      if (error) newErrors[key] = error;
    });

    setErrors(newErrors);
    setTouched({ length: true, width: true, budget: true });

    // If no errors, submit
    if (Object.keys(newErrors).length === 0) {
      onSubmit({
        length: parseFloat(formData.length),
        width: parseFloat(formData.width),
        budget: parseFloat(formData.budget),
      });
    }
  };

  const isFormValid = () => {
    return (
      formData.length &&
      formData.width &&
      formData.budget &&
      Object.values(errors).every((error) => !error)
    );
  };

  return (
    <div className="min-h-screen flex items-center justify-center p-4 animate-scale-in">
      <div className="glass-card rounded-3xl shadow-2xl p-8 md:p-12 w-full max-w-md">
        {/* Header */}
        <div className="text-center mb-8">
          <h1 className="text-4xl font-bold text-warm-500 mb-2">
            🏠 Design Your Dream Room
          </h1>
          <p className="text-gray-600 text-sm">
            Enter your room details and let AI create the perfect layout
          </p>
        </div>

        {/* Form */}
        <form onSubmit={handleSubmit} className="space-y-6">
          {/* Room Length */}
          <div>
            <label className="flex items-center text-charcoal font-medium mb-2">
              <Ruler className="w-5 h-5 mr-2 text-warm-500" />
              Room Length (meters)
            </label>
            <input
              type="number"
              name="length"
              value={formData.length}
              onChange={handleChange}
              onBlur={handleBlur}
              placeholder="Enter length (3-15 meters)"
              step="0.1"
              className={`w-full px-4 py-3 rounded-lg border-2 ${
                errors.length && touched.length
                  ? 'border-red-500 focus:border-red-600'
                  : 'border-warm-200 focus:border-warm-500'
              } focus:ring-4 focus:ring-warm-200 focus:ring-opacity-50 transition-all`}
              disabled={loading}
            />
            {errors.length && touched.length && (
              <div className="flex items-center mt-2 text-red-600 text-sm animate-fade-in">
                <AlertCircle className="w-4 h-4 mr-1" />
                {errors.length}
              </div>
            )}
          </div>

          {/* Room Width */}
          <div>
            <label className="flex items-center text-charcoal font-medium mb-2">
              <Square className="w-5 h-5 mr-2 text-warm-500" />
              Room Width (meters)
            </label>
            <input
              type="number"
              name="width"
              value={formData.width}
              onChange={handleChange}
              onBlur={handleBlur}
              placeholder="Enter width (3-15 meters)"
              step="0.1"
              className={`w-full px-4 py-3 rounded-lg border-2 ${
                errors.width && touched.width
                  ? 'border-red-500 focus:border-red-600'
                  : 'border-warm-200 focus:border-warm-500'
              } focus:ring-4 focus:ring-warm-200 focus:ring-opacity-50 transition-all`}
              disabled={loading}
            />
            {errors.width && touched.width && (
              <div className="flex items-center mt-2 text-red-600 text-sm animate-fade-in">
                <AlertCircle className="w-4 h-4 mr-1" />
                {errors.width}
              </div>
            )}
          </div>

          {/* Budget */}
          <div>
            <label className="flex items-center text-charcoal font-medium mb-2">
              <DollarSign className="w-5 h-5 mr-2 text-warm-500" />
              Budget ($)
            </label>
            <input
              type="number"
              name="budget"
              value={formData.budget}
              onChange={handleChange}
              onBlur={handleBlur}
              placeholder="Enter budget ($500-$10,000)"
              step="50"
              className={`w-full px-4 py-3 rounded-lg border-2 ${
                errors.budget && touched.budget
                  ? 'border-red-500 focus:border-red-600'
                  : 'border-warm-200 focus:border-warm-500'
              } focus:ring-4 focus:ring-warm-200 focus:ring-opacity-50 transition-all`}
              disabled={loading}
            />
            {errors.budget && touched.budget && (
              <div className="flex items-center mt-2 text-red-600 text-sm animate-fade-in">
                <AlertCircle className="w-4 h-4 mr-1" />
                {errors.budget}
              </div>
            )}
          </div>

          {/* Submit Button */}
          <button
            type="submit"
            disabled={!isFormValid() || loading}
            className={`w-full py-4 px-8 rounded-lg font-semibold text-white text-lg flex items-center justify-center space-x-2 transition-all ${
              !isFormValid() || loading
                ? 'bg-gray-400 cursor-not-allowed'
                : 'bg-gradient-to-r from-warm-500 to-warm-400 hover:from-warm-600 hover:to-warm-500 hover:scale-105 hover:shadow-lg active:scale-100'
            }`}
          >
            {loading ? (
              <>
                <div className="w-5 h-5 border-2 border-white border-t-transparent rounded-full animate-spin"></div>
                <span>Generating...</span>
              </>
            ) : (
              <>
                <Sparkles className="w-5 h-5" />
                <span>Generate Layout</span>
              </>
            )}
          </button>
        </form>

        {/* Info Text */}
        <p className="text-center text-gray-500 text-xs mt-6">
          Our AI will optimize furniture placement based on your space and budget
        </p>
      </div>
    </div>
  );
};

export default RoomForm;
