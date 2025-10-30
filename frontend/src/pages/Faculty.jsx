import React, { useEffect, useState } from "react";
import { Container, Row, Col, Modal } from "react-bootstrap";
import { facultyService } from "../services/api";
import Button from "../components/Button";
// 🎯 Import the separated component from the components folder
import FacultyCard from "../components/FacultyCard";

const Faculty = () => {
  const [faculty, setFaculty] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [selectedFaculty, setSelectedFaculty] = useState(null);

  useEffect(() => {
    const fetchFaculty = async () => {
      try {
        // 🚨 Using the centralized Axios service call
        const response = await facultyService.getAllFaculty();

        // --- Fallback Dummy Data Logic (for testing/initial setup) ---
        const dummyData = [
          {
            id: 1,
            name: "Dr. Arifin Tech",
            subject: "Computer Science",
            email: "arifin@fms.edu",
            bio: "Expert in distributed systems, cloud architecture, and modern web development using React and Spring Boot.",
            imageUrl: "https://via.placeholder.com/150/007bff/ffffff?text=AT",
            department: "Engineering",
          },
          {
            id: 2,
            name: "Prof. Sarah Lee",
            subject: "Applied Physics",
            email: "sarah@fms.edu",
            bio: "Leading researcher in quantum optics and material science. Published over 50 peer-reviewed papers.",
            imageUrl: "https://via.placeholder.com/150/6f42c1/ffffff?text=SL",
            department: "Science",
          },
          {
            id: 3,
            name: "Dr. Ben Carter",
            subject: "Business Management",
            email: "ben@fms.edu",
            bio: "Specializes in organizational leadership and strategic market entry. Consultant for several global firms.",
            imageUrl: "https://via.placeholder.com/150/28a745/ffffff?text=BC",
            department: "Business",
          },
        ];
        // Use API data if available, otherwise use dummy data
        const data =
          response.data && response.data.length > 0 ? response.data : dummyData;
        setFaculty(data);
        // -----------------------------------------------------------

        setLoading(false);
      } catch (error) {
        console.error("Failed to fetch faculty:", error);
        setLoading(false);
        // Load dummy data on API error for continuous UI development
        setFaculty(dummyData);
      }
    };
    fetchFaculty();
  }, []);

  const handleViewProfile = (facultyMember) => {
    setSelectedFaculty(facultyMember);
    setShowModal(true);
  };

  if (loading)
    return <h2 className="text-center mt-5">Loading Faculty Data...</h2>;

  return (
    <Container className="my-5">
      <h1 className="text-center mb-5 text-secondary-purple">
        Our Distinguished Faculty
      </h1>
      <p className="lead text-center mb-5">
        Meet the experts guiding our academic community and leading cutting-edge
        research.
      </p>

      {/* Faculty Grid Layout */}
      <Row className="g-4">
        {faculty.map((member) => (
          <Col md={6} lg={4} key={member.id}>
            {/* FacultyCard is now imported and reused here */}
            <FacultyCard member={member} onViewProfile={handleViewProfile} />
          </Col>
        ))}
      </Row>

      {/* Faculty Profile Modal */}
      <Modal
        show={showModal}
        onHide={() => setShowModal(false)}
        size="lg"
        centered
      >
        <Modal.Header closeButton className="bg-light">
          <Modal.Title className="text-primary fw-bold">
            {selectedFaculty?.name}
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Row>
            <Col md={4} className="text-center">
              <img
                src={selectedFaculty?.imageUrl}
                alt={selectedFaculty?.name}
                className="img-fluid rounded-circle mb-3 border border-primary border-3"
                style={{ width: "150px", height: "150px", objectFit: "cover" }}
              />
              <p className="fw-bold fs-5 text-secondary-purple">
                {selectedFaculty?.subject}
              </p>
              <p className="text-muted">{selectedFaculty?.department}</p>
            </Col>
            <Col md={8}>
              <h5 className="text-secondary-purple border-bottom pb-2">
                Biography & Expertise
              </h5>
              <p>{selectedFaculty?.bio}</p>

              <h5 className="text-secondary-purple mt-4 border-bottom pb-2">
                Contact
              </h5>
              <p className="mb-0">
                **Email:**{" "}
                <a
                  href={`mailto:${selectedFaculty?.email}`}
                  className="text-decoration-none"
                >
                  {selectedFaculty?.email}
                </a>
              </p>
            </Col>
          </Row>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowModal(false)}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>
    </Container>
  );
};

export default Faculty;
