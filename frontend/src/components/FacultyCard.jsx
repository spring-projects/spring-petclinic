import { Mail, Phone } from "lucide-react";

const FacultyCard = ({ faculty, onViewProfile }) => {
  const { id, name, subject, email, phone, image, qualification } = faculty;

  return (
    <div className="card profile-card border-0 shadow-sm h-100">
      <div
        style={{
          height: "250px",
          overflow: "hidden",
          backgroundColor: "#f3f4f6",
        }}
      >
        <img
          src={
            image ||
            `https://ui-avatars.com/api/?name=${encodeURIComponent(
              name
            )}&size=250&background=667eea&color=fff`
          }
          className="card-img-top"
          alt={name}
          style={{ width: "100%", height: "100%", objectFit: "cover" }}
        />
      </div>
      <div className="card-body text-center">
        <h5
          className="card-title mb-1"
          style={{ fontWeight: "700", color: "#1e293b" }}
        >
          {name}
        </h5>
        <p className="text-muted mb-2">{qualification}</p>
        <span
          className="badge"
          style={{
            background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
            padding: "6px 15px",
            fontSize: "0.85rem",
          }}
        >
          {subject}
        </span>
        <div className="mt-3">
          <div
            className="d-flex align-items-center justify-content-center mb-2"
            style={{ fontSize: "0.9rem" }}
          >
            <Mail size={16} className="me-2" style={{ color: "#667eea" }} />
            <span>{email}</span>
          </div>
          <div
            className="d-flex align-items-center justify-content-center"
            style={{ fontSize: "0.9rem" }}
          >
            <Phone size={16} className="me-2" style={{ color: "#667eea" }} />
            <span>{phone}</span>
          </div>
        </div>
        <button
          className="btn btn-gradient mt-3 w-100"
          onClick={() => onViewProfile(faculty)}
          data-bs-toggle="modal"
          data-bs-target={`#facultyModal${id}`}
        >
          View Profile
        </button>
      </div>
    </div>
  );
};

export default FacultyCard;
