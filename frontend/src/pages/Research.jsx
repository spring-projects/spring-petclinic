import React, { useState, useEffect } from "react";
import { Container, Row, Col, Card, Badge, ListGroup } from "react-bootstrap";
import {
  FaBookOpen,
  FaGlobe,
  FaCertificate,
  FaArrowRight,
} from "react-icons/fa";
// Assuming you'll have a research service later, though we'll use dummy data for now
// import { researchService } from '../services/api';

// Placeholder Research Data
const dummyResearch = [
  {
    id: 301,
    type: "Journal",
    title: "Optimizing State Management in Large-Scale React Applications",
    author: "A. Tech, S. Lee",
    year: 2024,
    source: "Journal of Web Technologies",
    link: "#",
  },
  {
    id: 302,
    type: "Conference",
    title: "Spring Boot Security with JWT: A Practical Approach",
    author: "A. Tech",
    year: 2023,
    source: "International Dev Conference",
    link: "#",
  },
  {
    id: 303,
    type: "Paper",
    title: "Quantum Entanglement in Solid-State Materials",
    author: "S. Lee",
    year: 2024,
    source: "Pre-print Server",
    link: "#",
  },
  {
    id: 304,
    type: "Journal",
    title: "Financial Modeling using PostgreSQL Analytical Functions",
    author: "B. Carter",
    year: 2023,
    source: "The Finance Review",
    link: "#",
  },
];

// Helper function to get the appropriate icon
const getTypeIcon = (type) => {
  switch (type) {
    case "Journal":
      return <FaBookOpen className="text-success me-2" />;
    case "Conference":
      return <FaGlobe className="text-info me-2" />;
    case "Paper":
      return <FaCertificate className="text-warning me-2" />;
    default:
      return <FaArrowRight className="text-secondary-purple me-2" />;
  }
};

const Research = () => {
  const [researchOutputs, setResearchOutputs] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    // In a final application, you would fetch from your backend API
    setResearchOutputs(dummyResearch);
    setLoading(false);
  }, []);

  if (loading)
    return <h2 className="text-center mt-5">Loading Research Outputs...</h2>;

  return (
    <Container className="my-5">
      <h1 className="text-center mb-4 text-primary">Research & Publications</h1>
      <p className="lead text-center mb-5">
        A showcase of our faculty's contributions to the global academic
        community.
      </p>

      {/* Publication List */}
      <ListGroup className="shadow-lg rounded-3">
        {researchOutputs.map((item) => (
          // Card for animated effect from global.css
          <ListGroup.Item key={item.id} className="p-0 border-0">
            <Card className="research-card h-100 border-0">
              <Card.Body className="d-flex justify-content-between align-items-center py-4 px-4">
                <div className="flex-grow-1">
                  <Badge bg="secondary-purple" className="mb-2 p-2 fw-normal">
                    {getTypeIcon(item.type)} {item.type}
                  </Badge>
                  <Card.Title className="fs-5 text-dark fw-bold">
                    {item.title}
                  </Card.Title>
                  <Card.Text className="text-muted mb-1">
                    **Authors:** {item.author}
                  </Card.Text>
                  <Card.Text className="text-muted fst-italic">
                    **Source:** {item.source} ({item.year})
                  </Card.Text>
                </div>
                <a
                  href={item.link}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="btn btn-outline-primary btn-sm flex-shrink-0 me-3"
                >
                  View Source
                </a>
              </Card.Body>
            </Card>

            {/* Divider between items */}
            {item.id !== researchOutputs[researchOutputs.length - 1].id && (
              <hr className="my-0" />
            )}
          </ListGroup.Item>
        ))}
      </ListGroup>

      {/* Custom CSS for research card (optional, can be moved to global.css) */}
      <style>{`
        .research-card {
          transition: background-color 0.3s ease;
        }
        .research-card:hover {
          background-color: var(--light-gray); /* defined in global.css */
          cursor: pointer;
        }
      `}</style>
    </Container>
  );
};

// 🌟 THIS LINE IS CRUCIAL TO FIX YOUR ERROR 🌟
export default Research;
