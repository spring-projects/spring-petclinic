import React from "react";
import { Card } from "react-bootstrap";
import Button from "./Button"; // Import your reusable Button component

const FacultyCard = ({ member, onViewProfile }) => (
  <Card className="text-center h-100">
    <Card.Img
      variant="top"
      src={member.imageUrl}
      style={{ height: "180px", objectFit: "cover" }}
      className="rounded-top"
    />
    <Card.Body className="d-flex flex-column">
      <Card.Title className="text-primary">{member.name}</Card.Title>
      <Card.Text className="text-muted flex-grow-1">{member.subject}</Card.Text>
      <Button onClick={() => onViewProfile(member)}>View Profile Modal</Button>
    </Card.Body>
  </Card>
);

export default FacultyCard;
