import { useState, useEffect } from "react";
import { facultyAPI } from "../services/api";
import FacultyCard from "../components/FacultyCard";
import { Search, Filter } from "lucide-react";

const Faculty = () => {
  const [faculty, setFaculty] = useState([]);
  const [filteredFaculty, setFilteredFaculty] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [selectedSubject, setSelectedSubject] = useState("all");
  const [loading, setLoading] = useState(true);
  const [selectedFaculty, setSelectedFaculty] = useState(null);

  const mockFaculty = [
    {
      id: 1,
      name: "Dr. Sarah Johnson",
      subject: "Computer Science",
      email: "sarah.j@edu.com",
      phone: "+1 555-0101",
      qualification: "Ph.D. in Computer Science",
      image:
        "https://images.pexels.com/photos/3862130/pexels-photo-3862130.jpeg?auto=compress&cs=tinysrgb&w=300",
      bio: "Expert in Machine Learning and AI with 15+ years of experience.",
    },
    {
      id: 2,
      name: "Prof. Michael Chen",
      subject: "Mathematics",
      email: "michael.c@edu.com",
      phone: "+1 555-0102",
      qualification: "Ph.D. in Applied Mathematics",
      image:
        "https://images.pexels.com/photos/2379004/pexels-photo-2379004.jpeg?auto=compress&cs=tinysrgb&w=300",
      bio: "Specializing in differential equations and mathematical modeling.",
    },
    {
      id: 3,
      name: "Dr. Emily Rodriguez",
      subject: "Physics",
      email: "emily.r@edu.com",
      phone: "+1 555-0103",
      qualification: "Ph.D. in Theoretical Physics",
      image:
        "https://images.pexels.com/photos/3785079/pexels-photo-3785079.jpeg?auto=compress&cs=tinysrgb&w=300",
      bio: "Research focus on quantum mechanics and particle physics.",
    },
    {
      id: 4,
      name: "Prof. David Williams",
      subject: "Chemistry",
      email: "david.w@edu.com",
      phone: "+1 555-0104",
      qualification: "Ph.D. in Organic Chemistry",
      image:
        "https://images.pexels.com/photos/1516680/pexels-photo-1516680.jpeg?auto=compress&cs=tinysrgb&w=300",
      bio: "Expert in synthetic chemistry and drug development.",
    },
    {
      id: 5,
      name: "Dr. Lisa Anderson",
      subject: "Biology",
      email: "lisa.a@edu.com",
      phone: "+1 555-0105",
      qualification: "Ph.D. in Molecular Biology",
      image:
        "https://images.pexels.com/photos/3862130/pexels-photo-3862130.jpeg?auto=compress&cs=tinysrgb&w=300",
      bio: "Specializing in genetics and molecular biology research.",
    },
    {
      id: 6,
      name: "Prof. James Taylor",
      subject: "Computer Science",
      email: "james.t@edu.com",
      phone: "+1 555-0106",
      qualification: "Ph.D. in Software Engineering",
      image:
        "https://images.pexels.com/photos/2379004/pexels-photo-2379004.jpeg?auto=compress&cs=tinysrgb&w=300",
      bio: "Expert in software architecture and cloud computing.",
    },
  ];

  useEffect(() => {
    fetchFaculty();
  }, []);

  useEffect(() => {
    filterFaculty();
  }, [searchTerm, selectedSubject, faculty]);

  const fetchFaculty = async () => {
    try {
      const response = await facultyAPI.getAll();
      setFaculty(response.data);
    } catch (error) {
      console.log("Using mock data");
      setFaculty(mockFaculty);
    } finally {
      setLoading(false);
    }
  };

  const filterFaculty = () => {
    let filtered = faculty;

    if (searchTerm) {
      filtered = filtered.filter(
        (f) =>
          f.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
          f.subject.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }

    if (selectedSubject !== "all") {
      filtered = filtered.filter((f) => f.subject === selectedSubject);
    }

    setFilteredFaculty(filtered);
  };

  const subjects = ["all", ...new Set(faculty.map((f) => f.subject))];

  const handleViewProfile = (facultyMember) => {
    setSelectedFaculty(facultyMember);
  };

  return (
    <div style={{ paddingTop: "80px" }}>
      <section
        className="py-5"
        style={{
          background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
        }}
      >
        <div className="container text-white text-center">
          <h1 className="display-4 fw-bold mb-3 fade-in-up">Our Faculty</h1>
          <p
            className="lead fade-in-up"
            style={{
              maxWidth: "800px",
              margin: "0 auto",
              animationDelay: "0.2s",
            }}
          >
            Meet our distinguished team of educators and researchers
          </p>
        </div>
      </section>

      <section className="py-5">
        <div className="container">
          <div className="row mb-4">
            <div className="col-lg-6 mb-3 mb-lg-0">
              <div
                className="input-group shadow-sm"
                style={{ borderRadius: "10px" }}
              >
                <span className="input-group-text bg-white border-0">
                  <Search size={20} />
                </span>
                <input
                  type="text"
                  className="form-control border-0"
                  placeholder="Search faculty by name or subject..."
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                />
              </div>
            </div>
            <div className="col-lg-6">
              <div
                className="input-group shadow-sm"
                style={{ borderRadius: "10px" }}
              >
                <span className="input-group-text bg-white border-0">
                  <Filter size={20} />
                </span>
                <select
                  className="form-select border-0"
                  value={selectedSubject}
                  onChange={(e) => setSelectedSubject(e.target.value)}
                >
                  {subjects.map((subject) => (
                    <option key={subject} value={subject}>
                      {subject === "all" ? "All Subjects" : subject}
                    </option>
                  ))}
                </select>
              </div>
            </div>
          </div>

          {loading ? (
            <div className="text-center py-5">
              <div className="spinner-border text-primary" role="status">
                <span className="visually-hidden">Loading...</span>
              </div>
            </div>
          ) : (
            <div className="row g-4">
              {filteredFaculty.map((facultyMember, index) => (
                <div key={facultyMember.id} className="col-md-6 col-lg-4">
                  <div
                    className="fade-in-up"
                    style={{ animationDelay: `${index * 0.1}s` }}
                  >
                    <FacultyCard
                      faculty={facultyMember}
                      onViewProfile={handleViewProfile}
                    />
                  </div>
                </div>
              ))}
            </div>
          )}

          {filteredFaculty.length === 0 && !loading && (
            <div className="text-center py-5">
              <p className="text-muted">
                No faculty members found matching your criteria.
              </p>
            </div>
          )}
        </div>
      </section>

      {selectedFaculty && (
        <div
          className="modal fade"
          id={`facultyModal${selectedFaculty.id}`}
          tabIndex="-1"
        >
          <div className="modal-dialog modal-lg modal-dialog-centered">
            <div className="modal-content" style={{ borderRadius: "20px" }}>
              <div className="modal-header border-0">
                <button
                  type="button"
                  className="btn-close"
                  data-bs-dismiss="modal"
                ></button>
              </div>
              <div className="modal-body p-4">
                <div className="row">
                  <div className="col-md-4 text-center mb-4 mb-md-0">
                    <img
                      src={
                        selectedFaculty.image ||
                        `https://ui-avatars.com/api/?name=${encodeURIComponent(
                          selectedFaculty.name
                        )}&size=200&background=667eea&color=fff`
                      }
                      alt={selectedFaculty.name}
                      className="img-fluid rounded shadow"
                      style={{ maxWidth: "200px" }}
                    />
                  </div>
                  <div className="col-md-8">
                    <h3 className="fw-bold mb-2">{selectedFaculty.name}</h3>
                    <p className="text-muted mb-3">
                      {selectedFaculty.qualification}
                    </p>
                    <span
                      className="badge mb-3"
                      style={{
                        background:
                          "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
                        padding: "8px 20px",
                        fontSize: "0.9rem",
                      }}
                    >
                      {selectedFaculty.subject}
                    </span>
                    <p className="mt-3" style={{ lineHeight: "1.8" }}>
                      {selectedFaculty.bio}
                    </p>
                    <hr />
                    <div className="mt-3">
                      <p className="mb-2">
                        <strong>Email:</strong> {selectedFaculty.email}
                      </p>
                      <p className="mb-2">
                        <strong>Phone:</strong> {selectedFaculty.phone}
                      </p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default Faculty;
