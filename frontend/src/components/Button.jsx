import React from "react";
// import "./Button.css"; // Don't forget to import the CSS file!
import "../styles/Button.css";
// Helper function to conditionally join class names
const cn = (...classes) => {
  return classes.filter(Boolean).join(" ");
};

const Button = ({
  children,
  // New prop for disabled state
  disabled = false,
  // New prop for full width
  fullWidth = false,
  // New prop for icon placement (should be a React component/element)
  icon: IconComponent = null,
  variant = "primary",
  size = "md",
  onClick,
  className = "",
  ...props
}) => {
  const baseClasses = "btn-base";

  const variantClasses = {
    // Enhanced default: Gradient with shadow
    primary: "btn-primary-gradient",
    // Outline style
    secondary: "btn-outline-primary",
    // Red for destructive actions
    danger: "btn-danger",
    // New: Text only, low emphasis button
    ghost: "btn-ghost",
  };

  const sizeClasses = {
    sm: "btn-sm",
    // Adjusted 'md' to use base class styles
    md: "btn-md",
    lg: "btn-lg",
  };

  return (
    <button
      className={cn(
        baseClasses,
        variantClasses[variant],
        sizeClasses[size],
        fullWidth && "btn-full-width", // Apply full-width class conditionally
        disabled && "btn-disabled", // Apply disabled class conditionally
        className
      )}
      onClick={onClick}
      disabled={disabled} // Pass disabled prop to the native button element
      {...props}
    >
      {/* Conditionally render the icon */}
      {IconComponent && (
        <span className="btn-icon-wrapper">{IconComponent}</span>
      )}

      {/* Button text/content */}
      {children}
    </button>
  );
};

export default Button;
