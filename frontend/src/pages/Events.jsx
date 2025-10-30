import React, { useState, useEffect } from "react";
import { Container, Row, Col, Card, Badge } from "react-bootstrap";
import { FaCalendarAlt, FaMapMarkerAlt, FaRegClock } from "react-icons/fa";
import Button from "../components/Button";

// Placeholder Event Data (to be replaced by API)
const dummyEvents = [
  {
    id: 201,
    title: "Annual Research Symposium",
    date: "2025-11-15",
    time: "9:00 AM - 4:00 PM",
    location: "Main Auditorium, Campus 1",
    category: "Academic",
    description:
      "A day of presentations showcasing the latest faculty and student research.",
  },
  {
    id: 202,
    title: "Industry Collaboration Workshop",
    date: "2025-12-05",
    time: "2:00 PM - 5:00 PM",
    location: "Online (Zoom)",
    category: "Industry",
    description:
      "Bridging the gap between academia and real-world industrial applications.",
  },
  {
    id: 203,
    title: "Faculty Excellence Awards",
    date: "2026-01-10",
    time: "7:00 PM - 9:00 PM",
    location: "Grand Ballroom, City Center",
    category: "Social",
    description:
      "Celebrating outstanding achievements in teaching and service.",
  },
];

const Events = () => {
  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(false); // Assume fast loading for dummy data

  useEffect(() => {
    // In a real app, you would fetch from your /api/events endpoint here
    // For now, use the placeholder data
    setEvents(dummyEvents);
    setLoading(false);
  }, []);

  const handleRegister = (title) => {
    alert(`Registration link simulated for: ${title}`);
    // Replace with actual routing or API call to external registration form
  };

  if (loading) return <h2 className="text-center mt-5">Loading Events...</h2>;

  return (
    <Container className="my-5">
      <h1 className="text-center mb-4 text-secondary-purple">
        Upcoming Events & Activities
      </h1>
      <p className="lead text-center mb-5">
        Join our academic community for conferences, workshops, and ceremonies.
      </p>

      {/* Events Grid */}
      <Row className="g-4">
        {events.map((event) => (
          <Col md={6} lg={4} key={event.id}>
            <Card className="h-100 p-3 shadow-sm">
              <Card.Body className="d-flex flex-column">
                <div className="d-flex justify-content-between align-items-start mb-3">
                  <Card.Title className="text-primary fw-bold mb-1">
                    {event.title}
                  </Card.Title>
                  <FaCalendarAlt className="fs-3 text-secondary-purple flex-shrink-0 ms-2" />
                </div>

                <Badge
                  bg={
                    event.category === "Academic"
                      ? "info"
                      : event.category === "Industry"
                      ? "warning"
                      : "success"
                  }
                  className="mb-3 align-self-start"
                >
                  {event.category}
                </Badge>

                <Card.Text className="text-muted flex-grow-1 mb-3">
                  {event.description}
                </Card.Text>

                <div className="border-top pt-3">
                  <p className="mb-1">
                    <FaRegClock className="me-2 text-primary" />
                    **{new Date(event.date).toDateString()}** at {event.time}
                  </p>
                  <p className="mb-3">
                    <FaMapMarkerAlt className="me-2 text-primary" />
                    {event.location}
                  </p>

                  <Button
                    onClick={() => handleRegister(event.title)}
                    variant="outline-primary"
                    size="sm"
                    className="w-100"
                  >
                    Register Now
                  </Button>
                </div>
              </Card.Body>
            </Card>
          </Col>
        ))}
      </Row>
    </Container>
  );
};

// 🌟 THIS LINE IS CRUCIAL TO FIX YOUR ERROR 🌟
export default Events;
