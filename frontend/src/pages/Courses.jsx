import React, { useState, useEffect } from "react";
import { Container, Row, Col, Card, Badge, Form } from "react-bootstrap";
import { courseService } from "../services/api";
import { FaGraduationCap, FaFilter } from "react-icons/fa";

// Placeholder data for filtering
const courseCategories = [
  "All",
  "Computer Science",
  "Applied Physics",
  "Business",
  "Mathematics",
  "Engineering",
];

// Placeholder Course Data (to be replaced by API)
const dummyCourses = [
  {
    id: 101,
    title: "Advanced React & Vite",
    category: "Computer Science",
    credits: 3,
    level: "Graduate",
    description:
      "Deep dive into state management and performance optimization.",
  },
  {
    id: 102,
    title: "Quantum Optics",
    category: "Applied Physics",
    credits: 4,
    level: "Advanced",
    description: "Study of light and matter at the quantum level.",
  },
  {
    id: 103,
    title: "Strategic Market Entry",
    category: "Business",
    credits: 3,
    level: "Undergraduate",
    description: "Analyzing global markets and developing entry strategies.",
  },
  {
    id: 104,
    title: "Differential Equations",
    category: "Mathematics",
    credits: 3,
    level: "Undergraduate",
    description:
      "Classic methods for solving ordinary and partial differential equations.",
  },
  {
    id: 105,
    title: "Structural Integrity",
    category: "Engineering",
    credits: 4,
    level: "Graduate",
    description: "Principles of structural stability and failure analysis.",
  },
];

const Courses = () => {
  const [courses, setCourses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedCategory, setSelectedCategory] = useState("All");

  useEffect(() => {
    const fetchCourses = async () => {
      try {
        const response = await courseService.getAllCourses();
        // Use API data or dummy data
        const data =
          response.data && response.data.length > 0
            ? response.data
            : dummyCourses;
        setCourses(data);
        setLoading(false);
      } catch (error) {
        console.error("Failed to fetch courses:", error);
        setCourses(dummyCourses);
        setLoading(false);
      }
    };
    fetchCourses();
  }, []);

  const handleCategoryChange = (e) => {
    setSelectedCategory(e.target.value);
  };

  const filteredCourses = courses.filter(
    (course) =>
      selectedCategory === "All" || course.category === selectedCategory
  );

  if (loading) return <h2 className="text-center mt-5">Loading Courses...</h2>;

  return (
    <Container className="my-5">
      <h1 className="text-center mb-4 text-primary">Active Courses Catalog</h1>
      <p className="lead text-center mb-5">
        Explore our wide range of academic offerings across various disciplines.
      </p>

      {/* Filter Section */}
      <Row className="mb-5 align-items-center bg-white p-4 rounded-3 shadow-sm">
        <Col md={3} className="d-flex align-items-center">
          <FaFilter className="fs-4 text-secondary-purple me-2" />
          <h5 className="mb-0 text-secondary-purple">Filter By Category:</h5>
        </Col>
        <Col md={4}>
          <Form.Select
            onChange={handleCategoryChange}
            value={selectedCategory}
            className="shadow-none border-secondary-purple"
          >
            {courseCategories.map((cat) => (
              <option key={cat} value={cat}>
                {cat}
              </option>
            ))}
          </Form.Select>
        </Col>
        <Col md={5} className="text-md-end mt-3 mt-md-0">
          <Badge bg="info" className="p-2 fs-6">
            Showing {filteredCourses.length} of {courses.length} Courses
          </Badge>
        </Col>
      </Row>

      {/* Course Grid */}
      <Row className="g-4">
        {filteredCourses.length > 0 ? (
          filteredCourses.map((course) => (
            <Col md={6} lg={4} key={course.id}>
              <Card className="h-100 p-3">
                <Card.Body className="d-flex flex-column">
                  <div className="d-flex justify-content-between align-items-center mb-3">
                    <Card.Title className="text-primary fw-bold mb-0">
                      {course.title}
                    </Card.Title>
                    <FaGraduationCap className="fs-3 text-secondary-purple" />
                  </div>
                  <Badge
                    bg="secondary-purple"
                    className="mb-2 p-2"
                    style={{ backgroundColor: "var(--secondary-purple)" }}
                  >
                    {course.category}
                  </Badge>
                  <Card.Text className="text-muted flex-grow-1">
                    {course.description}
                  </Card.Text>
                  <div className="mt-2 pt-3 border-top d-flex justify-content-between">
                    <Badge bg="success" className="fs-6">
                      Credits: {course.credits}
                    </Badge>
                    <Badge bg="warning" text="dark" className="fs-6">
                      {course.level}
                    </Badge>
                  </div>
                </Card.Body>
              </Card>
            </Col>
          ))
        ) : (
          <Col>
            <p className="text-center text-muted fs-5">
              No courses found in the selected category.
            </p>
          </Col>
        )}
      </Row>
    </Container>
  );
};

// 🌟 THIS LINE IS CRUCIAL TO FIX YOUR ERROR 🌟
export default Courses;
