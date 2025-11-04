import { useState, useEffect } from "react";
import { researchAPI } from "../services/api";
import { FileText, Users, Calendar, ExternalLink } from "lucide-react";

const Research = () => {
  const [research, setResearch] = useState([]);
  const [loading, setLoading] = useState(true);

  const mockResearch = [
    {
      id: 1,
      title: "Deep Learning Applications in Medical Imaging",
      authors: ["Dr. Sarah Johnson", "Dr. Emily Rodriguez"],
      journal: "Nature Machine Intelligence",
      year: 2024,
      type: "Journal",
      abstract:
        "Novel approaches to medical image analysis using convolutional neural networks...",
      citations: 45,
    },
    {
      id: 2,
      title: "Quantum Computing Algorithms for Optimization Problems",
      authors: ["Dr. Emily Rodriguez", "Prof. Michael Chen"],
      journal: "Physical Review Letters",
      year: 2024,
      type: "Journal",
      abstract:
        "Exploration of quantum algorithms for solving complex optimization challenges...",
      citations: 32,
    },
    {
      id: 3,
      title: "Sustainable Chemistry: Green Synthesis Methods",
      authors: ["Prof. David Williams"],
      journal: "Journal of the American Chemical Society",
      year: 2023,
      type: "Journal",
      abstract: "Environmentally friendly approaches to organic synthesis...",
      citations: 67,
    },
    {
      id: 4,
      title: "CRISPR Gene Editing: Advances and Ethics",
      authors: ["Dr. Lisa Anderson", "Dr. Sarah Johnson"],
      conference: "International Conference on Molecular Biology",
      year: 2024,
      type: "Conference",
      abstract:
        "Latest developments in gene editing technology and ethical considerations...",
      citations: 28,
    },
    {
      id: 5,
      title: "Cloud-Native Architecture Patterns",
      authors: ["Prof. James Taylor"],
      journal: "IEEE Software",
      year: 2023,
      type: "Journal",
      abstract: "Best practices for designing scalable cloud applications...",
      citations: 51,
    },
  ];

  useEffect(() => {
    fetchResearch();
  }, []);

  const fetchResearch = async () => {
    try {
      const response = await researchAPI.getAll();
      setResearch(response.data);
    } catch (error) {
      console.log("Using mock data");
      setResearch(mockResearch);
    } finally {
      setLoading(false);
    }
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
          <h1 className="display-4 fw-bold mb-3 fade-in-up">
            Research & Publications
          </h1>
          <p
            className="lead fade-in-up"
            style={{
              maxWidth: "800px",
              margin: "0 auto",
              animationDelay: "0.2s",
            }}
          >
            Cutting-edge research shaping the future of science and technology
          </p>
        </div>
      </section>

      <section className="py-5">
        <div className="container">
          <div className="row mb-5">
            <div className="col-md-4 text-center mb-4 mb-md-0">
              <div className="stat-card">
                <h2
                  className="display-4 fw-bold mb-2"
                  style={{ color: "#667eea" }}
                >
                  500+
                </h2>
                <p className="text-muted fw-semibold">Research Papers</p>
              </div>
            </div>
            <div className="col-md-4 text-center mb-4 mb-md-0">
              <div className="stat-card">
                <h2
                  className="display-4 fw-bold mb-2"
                  style={{ color: "#764ba2" }}
                >
                  150+
                </h2>
                <p className="text-muted fw-semibold">Active Projects</p>
              </div>
            </div>
            <div className="col-md-4 text-center">
              <div className="stat-card">
                <h2
                  className="display-4 fw-bold mb-2"
                  style={{ color: "#3b82f6" }}
                >
                  50+
                </h2>
                <p className="text-muted fw-semibold">Collaborations</p>
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
            <div>
              <h3 className="section-title mb-4">Recent Publications</h3>
              {research.map((paper, index) => (
                <div
                  key={paper.id}
                  className="research-paper fade-in-up"
                  style={{ animationDelay: `${index * 0.1}s` }}
                >
                  <div className="d-flex justify-content-between align-items-start mb-2">
                    <div className="flex-grow-1">
                      <h5 className="fw-bold mb-2">{paper.title}</h5>
                      <div className="d-flex align-items-center gap-3 mb-2 flex-wrap">
                        <div className="d-flex align-items-center">
                          <Users size={16} className="me-2 text-primary" />
                          <small className="text-muted">
                            {paper.authors.join(", ")}
                          </small>
                        </div>
                        <div className="d-flex align-items-center">
                          <Calendar size={16} className="me-2 text-primary" />
                          <small className="text-muted">{paper.year}</small>
                        </div>
                        <div className="d-flex align-items-center">
                          <FileText size={16} className="me-2 text-primary" />
                          <small className="text-muted">{paper.type}</small>
                        </div>
                      </div>
                      <p className="mb-2">
                        <strong>Published in:</strong>{" "}
                        {paper.journal || paper.conference}
                      </p>
                      <p className="text-muted mb-2">{paper.abstract}</p>
                      <p className="text-muted mb-0">
                        <strong>Citations:</strong> {paper.citations}
                      </p>
                    </div>
                    <button className="btn btn-outline-primary btn-sm ms-3">
                      <ExternalLink size={16} />
                    </button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </section>

      <section className="py-5" style={{ backgroundColor: "#f8f9fa" }}>
        <div className="container">
          <div className="text-center mb-5">
            <h2 className="section-title">Research Areas</h2>
          </div>
          <div className="row g-4">
            {[
              "Artificial Intelligence",
              "Quantum Physics",
              "Biotechnology",
              "Sustainable Chemistry",
              "Data Science",
              "Nanotechnology",
            ].map((area, index) => (
              <div key={index} className="col-md-4">
                <div
                  className="card border-0 shadow-sm card-hover h-100"
                  style={{ borderRadius: "15px" }}
                >
                  <div className="card-body text-center p-4">
                    <div
                      className="mb-3 mx-auto"
                      style={{
                        width: "70px",
                        height: "70px",
                        borderRadius: "15px",
                        background:
                          "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "center",
                      }}
                    >
                      <FileText size={35} color="white" />
                    </div>
                    <h5 className="fw-bold">{area}</h5>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>
    </div>
  );
};

export default Research;
