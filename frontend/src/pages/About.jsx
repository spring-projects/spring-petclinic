import React from "react";
import { Container, Row, Col, Card } from "react-bootstrap";
import { FaEye, FaBullseye, FaHistory } from "react-icons/fa";

const About = () => {
  return (
    <Container className="my-5">
      <h1 className="text-center mb-5 text-primary">About Our Institution</h1>

      <p className="lead text-center mb-5">
        We are committed to academic excellence, innovation, and fostering the
        next generation of leaders.
      </p>

      {/* Vision, Mission, History Cards */}
      <Row className="g-4 mb-5">
        <Col md={4}>
          <Card className="text-center p-4 h-100 shadow-sm">
            <FaEye className="fs-1 text-secondary-purple mx-auto mb-3" />
            <Card.Body>
              <Card.Title className="fw-bold">Our Vision</Card.Title>
              <Card.Text className="text-muted">
                To be a globally recognized center of learning, renowned for
                pioneering research and transformative education that addresses
                societal challenges.
              </Card.Text>
            </Card.Body>
          </Card>
        </Col>

        <Col md={4}>
          <Card className="text-center p-4 h-100 shadow-sm">
            <FaBullseye className="fs-1 text-secondary-purple mx-auto mb-3" />
            <Card.Body>
              <Card.Title className="fw-bold">Our Mission</Card.Title>
              <Card.Text className="text-muted">
                To provide a dynamic, student-centric learning environment,
                foster a culture of ethical inquiry, and empower our faculty to
                achieve teaching and research excellence.
              </Card.Text>
            </Card.Body>
          </Card>
        </Col>

        <Col md={4}>
          <Card className="text-center p-4 h-100 shadow-sm">
            <FaHistory className="fs-1 text-secondary-purple mx-auto mb-3" />
            <Card.Body>
              <Card.Title className="fw-bold">Institution History</Card.Title>
              <Card.Text className="text-muted">
                Established in 1985, our institution has grown from a small
                college to a comprehensive university, constantly adapting to
                meet the evolving demands of the modern world.
              </Card.Text>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      {/* Institutional Milestones Section (Optional Content) */}
      <Row className="mt-5">
        <Col>
          <h2 className="text-secondary-purple mb-4 border-bottom pb-2">
            Key Milestones
          </h2>
          <ul className="list-group list-group-flush">
            <li className="list-group-item">
              **1985:** Founding of the Institute of Technology.
            </li>
            <li className="list-group-item">
              **2001:** Awarded University status by the National Council.
            </li>
            <li className="list-group-item">
              **2015:** Launched the Faculty Exchange Program with 15
              international universities.
            </li>
            <li className="list-group-item">
              **2023:** Recognized as a 'Center of Excellence' for Artificial
              Intelligence research.
            </li>
          </ul>
        </Col>
      </Row>
    </Container>
  );
};

// 🌟 THIS LINE IS CRUCIAL TO FIX YOUR ERROR 🌟
export default About;
