import React from "react";
import { Container, Row, Col, Card } from "react-bootstrap";
import Button from "../components/Button";
import { Link } from "react-router-dom";

const Home = () => {
  return (
    <>
      {/* Hero Section with Background Animation (see global.css) */}
      <header className="hero-section text-center mb-5">
        <Container>
          <h1 className="display-3 fw-bold mb-4 animate__animated animate__fadeInDown">
            Faculty Management System
          </h1>
          <p className="lead mb-5 fs-4 animate__animated animate__fadeInUp">
            Empowering academic excellence through streamlined management and
            collaboration.
          </p>
          <div className="animate__animated animate__zoomIn">
            <Button
              variant="light"
              size="lg"
              className="me-3"
              as={Link}
              to="/faculty"
            >
              Meet Our Faculty
            </Button>
            <Button variant="outline-light" size="lg" as={Link} to="/contact">
              Join as Faculty
            </Button>
          </div>
        </Container>
      </header>

      {/* Mission & Vision Section (Simple cards) */}
      <Container className="my-5">
        <h2 className="text-center mb-5 text-primary">
          Why Choose Our Institution?
        </h2>
        <Row className="g-4">
          <Col md={4}>
            <Card className="p-4 h-100 text-center">
              <Card.Body>
                <i className="bi bi-lightbulb-fill fs-1 text-secondary-purple mb-3"></i>
                <Card.Title>Innovation</Card.Title>
                <Card.Text>
                  A culture that fosters cutting-edge research and innovative
                  teaching methods.
                </Card.Text>
              </Card.Body>
            </Card>
          </Col>
          <Col md={4}>
            <Card className="p-4 h-100 text-center">
              <Card.Body>
                <i className="bi bi-people-fill fs-1 text-secondary-purple mb-3"></i>
                <Card.Title>Community</Card.Title>
                <Card.Text>
                  A collaborative environment where faculty and students thrive
                  together.
                </Card.Text>
              </Card.Body>
            </Card>
          </Col>
          <Col md={4}>
            <Card className="p-4 h-100 text-center">
              <Card.Body>
                <i className="bi bi-award-fill fs-1 text-secondary-purple mb-3"></i>
                <Card.Title>Excellence</Card.Title>
                <Card.Text>
                  Commitment to the highest standards of academic and
                  professional achievement.
                </Card.Text>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Container>
    </>
  );
};

export default Home;
