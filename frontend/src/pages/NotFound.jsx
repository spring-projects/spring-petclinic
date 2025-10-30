import React from "react";
import { Container, Button } from "react-bootstrap";
import { Link } from "react-router-dom";

const NotFound = () => {
  return (
    <div
      className="d-flex align-items-center justify-content-center"
      style={{ minHeight: "80vh" }}
    >
      <Container className="text-center p-5">
        <h1 className="display-1 fw-bold text-danger">404</h1>
        <h2 className="mb-4 text-secondary-purple">Page Not Found</h2>
        <p className="lead mb-5">
          The page you are looking for doesn't exist or an error occurred.
        </p>
        <Button as={Link} to="/" variant="primary" size="lg">
          Go to Homepage
        </Button>
      </Container>
    </div>
  );
};

export default NotFound;
