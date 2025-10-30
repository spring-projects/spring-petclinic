import React from "react";
import { Button as BootstrapButton } from "react-bootstrap";

const Button = ({
  children,
  variant = "primary",
  size = "md",
  onClick,
  className = "",
}) => {
  return (
    <BootstrapButton
      variant={variant}
      size={size}
      onClick={onClick}
      className={`animated-btn ${className}`}
    >
      {children}
    </BootstrapButton>
  );
};

export default Button;
