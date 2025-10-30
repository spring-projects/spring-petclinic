import React from "react";
import { Container, Row, Col, Card } from "react-bootstrap";
import { useAuth } from "../context/AuthContext";
import { FaUserGraduate, FaBook, FaWifi } from "react-icons/fa";

// Placeholder data for dashboard stats
const stats = [
  {
    icon: FaUserGraduate,
    count: 125,
    title: "Total Faculty",
    color: "primary",
  },
  { icon: FaBook, count: 48, title: "Active Courses", color: "success" },
  { icon: FaWifi, count: 12, title: "Live Sessions", color: "warning" },
  { icon: FaBook, count: 200, title: "New Publications", color: "info" },
];

const Dashboard = () => {
  const { user } = useAuth();

  return (
    <Container className="my-5">
      <h1 className="mb-4 text-primary">Admin Dashboard</h1>
      <p className="lead mb-5">
        Welcome back, **{user?.username || "Admin"}**!
      </p>

      <Row className="g-4">
        {stats.map((stat, index) => (
          <Col lg={3} md={6} key={index}>
            <Card
              className={`p-3 border-start border-5 border-${stat.color} h-100`}
            >
              <Card.Body>
                <div className="d-flex align-items-center">
                  <stat.icon className={`fs-1 text-${stat.color} me-3`} />
                  <div>
                    <h2 className="fw-bold mb-0">{stat.count}</h2>
                    <p className="text-muted mb-0">{stat.title}</p>
                  </div>
                </div>
              </Card.Body>
            </Card>
          </Col>
        ))}
      </Row>

      <Row className="mt-5">
        <Col>
          <Card className="p-4">
            <h3 className="text-secondary-purple">System Overview</h3>
            <p>
              Detailed logs and administrative controls will go here. This page
              is protected by **AuthContext** and requires a successful login
              via `/login`.
            </p>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default Dashboard;
