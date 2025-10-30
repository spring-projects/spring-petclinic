import React from "react";
import { Container, Row, Col, Form, Card } from "react-bootstrap";
import Button from "../components/Button";

const Contact = () => {
  return (
    <Container className="my-5">
      <h1 className="text-center mb-5 text-primary">Get In Touch</h1>

      <Row className="g-5">
        <Col lg={6}>
          <Card className="p-4 h-100">
            <h3 className="mb-4 text-secondary-purple">Send Us a Message</h3>
            <Form>
              <Form.Group className="mb-3" controlId="contactName">
                <Form.Label>Full Name</Form.Label>
                <Form.Control
                  type="text"
                  placeholder="Enter your name"
                  required
                />
              </Form.Group>
              <Form.Group className="mb-3" controlId="contactEmail">
                <Form.Label>Email Address</Form.Label>
                <Form.Control
                  type="email"
                  placeholder="name@example.com"
                  required
                />
              </Form.Group>
              <Form.Group className="mb-3" controlId="contactSubject">
                <Form.Label>Subject</Form.Label>
                <Form.Control
                  type="text"
                  placeholder="Inquiry subject"
                  required
                />
              </Form.Group>
              <Form.Group className="mb-4" controlId="contactMessage">
                <Form.Label>Message</Form.Label>
                <Form.Control
                  as="textarea"
                  rows={4}
                  placeholder="Your message here"
                  required
                />
              </Form.Group>
              <Button type="submit">Submit Inquiry</Button>
            </Form>
          </Card>
        </Col>

        <Col lg={6}>
          <h3 className="mb-4 text-secondary-purple">Our Location & Details</h3>
          <Card className="p-3 mb-4">
            <p>
              <strong>Address:</strong> 123 Main Street, Faculty Tower, City,
              12345
            </p>
            <p>
              <strong>Phone:</strong> (123) 456-7890
            </p>
            <p>
              <strong>Email:</strong> info@fms.edu
            </p>
          </Card>

          {/* Google Map Embed */}
          <div className="ratio ratio-16x9 shadow-lg rounded-3">
            <iframe
              src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d15222.754779261073!2d85.80582525!3d20.3238686!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x3a1909a727d5301f%3A0x889895088f11b228!2sKIIT%20University!5e0!3m2!1sen!2sin!4v1680199432000!5m2!1sen!2sin"
              title="Location Map"
              allowFullScreen=""
              loading="lazy"
              referrerPolicy="no-referrer-when-downgrade"
              className="rounded-3"
            ></iframe>
          </div>
        </Col>
      </Row>
    </Container>
  );
};

export default Contact;
