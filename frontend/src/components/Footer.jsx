import React from "react";
import { Container, Row, Col } from "react-bootstrap";
import { FaFacebook, FaTwitter, FaLinkedin, FaGithub } from "react-icons/fa";

const Footer = () => {
  return (
    <footer className="bg-dark text-white pt-5 pb-3 mt-5">
      <Container>
        <Row>
          <Col md={4} className="mb-4">
            <h5 className="text-primary mb-3">Faculty Management System</h5>
            <p className="text-muted">
              Dedicated to managing academic resources and simplifying
              administrative tasks.
            </p>
          </Col>
          <Col md={4} className="mb-4">
            <h5 className="text-primary mb-3">Quick Links</h5>
            <ul className="list-unstyled">
              <li>
                <a href="/faculty" className="text-white text-decoration-none">
                  Our Faculty
                </a>
              </li>
              <li>
                <a href="/courses" className="text-white text-decoration-none">
                  Active Courses
                </a>
              </li>
              <li>
                <a href="/contact" className="text-white text-decoration-none">
                  Support
                </a>
              </li>
              <li>
                <a href="/login" className="text-white text-decoration-none">
                  Login
                </a>
              </li>
            </ul>
          </Col>
          <Col md={4} className="mb-4">
            <h5 className="text-primary mb-3">Connect With Us</h5>
            <div className="d-flex social-icons">
              <a href="#" className="text-white me-3 fs-4 hover-scale">
                <FaFacebook />
              </a>
              <a href="#" className="text-white me-3 fs-4 hover-scale">
                <FaTwitter />
              </a>
              <a href="#" className="text-white me-3 fs-4 hover-scale">
                <FaLinkedin />
              </a>
              <a href="#" className="text-white fs-4 hover-scale">
                <FaGithub />
              </a>
            </div>
            <p className="mt-3 text-muted">
              &copy; {new Date().getFullYear()} FMS. All Rights Reserved.
            </p>
          </Col>
        </Row>
      </Container>

      {/* Custom CSS for hover effect on social icons */}
      <style>{`
        .hover-scale {
          transition: transform 0.2s ease-in-out;
        }
        .hover-scale:hover {
          transform: scale(1.15);
        }
      `}</style>
    </footer>
  );
};

export default Footer;
