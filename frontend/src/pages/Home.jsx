import { useNavigate } from "react-router-dom";
import { ArrowRight, Award, Users, BookOpen, TrendingUp } from "lucide-react";

const Home = () => {
  const navigate = useNavigate();

  const features = [
    {
      icon: Users,
      title: "Expert Faculty",
      description: "Learn from world-class educators and industry experts",
    },
    {
      icon: BookOpen,
      title: "Diverse Courses",
      description: "Wide range of courses across multiple disciplines",
    },
    {
      icon: Award,
      title: "Research Excellence",
      description: "Cutting-edge research and publications",
    },
    {
      icon: TrendingUp,
      title: "Career Growth",
      description: "Professional development opportunities",
    },
  ];

  return (
    <div>
      <section className="hero-section">
        <div className="hero-background"></div>
        <div
          className="floating-shape"
          style={{ top: "10%", left: "10%", fontSize: "100px" }}
        >
          ●
        </div>
        <div
          className="floating-shape"
          style={{
            top: "60%",
            right: "15%",
            fontSize: "80px",
            animationDelay: "2s",
          }}
        >
          ●
        </div>
        <div
          className="floating-shape"
          style={{
            bottom: "20%",
            left: "20%",
            fontSize: "120px",
            animationDelay: "1s",
          }}
        >
          ●
        </div>

        <div className="container">
          <div className="row align-items-center">
            <div className="col-lg-6 text-white fade-in-up">
              <h1 className="display-3 fw-bold mb-4">
                Welcome to Our
                <br />
                <span
                  style={{
                    background: "linear-gradient(90deg, #fff 0%, #e0e7ff 100%)",
                    WebkitBackgroundClip: "text",
                    WebkitTextFillColor: "transparent",
                  }}
                >
                  Faculty Portal
                </span>
              </h1>
              <p
                className="lead mb-4"
                style={{
                  fontSize: "1.25rem",
                  lineHeight: "1.8",
                  opacity: "0.95",
                }}
              >
                Discover excellence in education. Connect with distinguished
                faculty members, explore innovative courses, and be part of
                groundbreaking research.
              </p>
              <div className="d-flex gap-3 flex-wrap">
                <button
                  className="btn btn-light btn-lg px-5"
                  style={{ borderRadius: "30px", fontWeight: "600" }}
                  onClick={() => navigate("/faculty")}
                >
                  Meet Our Faculty
                  <ArrowRight className="ms-2" size={20} />
                </button>
                <button
                  className="btn btn-outline-light btn-lg px-5"
                  style={{ borderRadius: "30px", fontWeight: "600" }}
                  onClick={() => navigate("/login")}
                >
                  Join as Faculty
                </button>
              </div>
            </div>
            <div className="col-lg-6 mt-5 mt-lg-0">
              <div className="text-center slide-in-right">
                <img
                  src="https://images.pexels.com/photos/7092613/pexels-photo-7092613.jpeg?auto=compress&cs=tinysrgb&w=600"
                  alt="Faculty"
                  style={{
                    maxWidth: "100%",
                    borderRadius: "20px",
                    boxShadow: "0 20px 60px rgba(0,0,0,0.3)",
                    animation: "float 6s ease-in-out infinite",
                  }}
                />
              </div>
            </div>
          </div>
        </div>
      </section>

      <section className="py-5" style={{ backgroundColor: "#f8f9fa" }}>
        <div className="container">
          <div className="text-center mb-5">
            <h2 className="section-title display-5 fw-bold">Why Choose Us</h2>
            <p className="text-muted" style={{ fontSize: "1.1rem" }}>
              Experience excellence in education and research
            </p>
          </div>
          <div className="row g-4">
            {features.map((feature, index) => {
              const Icon = feature.icon;
              return (
                <div key={index} className="col-md-6 col-lg-3">
                  <div
                    className="card border-0 shadow-sm card-hover h-100"
                    style={{ borderRadius: "15px" }}
                  >
                    <div className="card-body text-center p-4">
                      <div
                        className="mb-3 mx-auto"
                        style={{
                          width: "80px",
                          height: "80px",
                          borderRadius: "20px",
                          background:
                            "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
                          display: "flex",
                          alignItems: "center",
                          justifyContent: "center",
                        }}
                      >
                        <Icon size={40} color="white" />
                      </div>
                      <h5 className="fw-bold mb-3">{feature.title}</h5>
                      <p className="text-muted mb-0">{feature.description}</p>
                    </div>
                  </div>
                </div>
              );
            })}
          </div>
        </div>
      </section>

      <section className="py-5">
        <div className="container">
          <div className="row align-items-center">
            <div className="col-lg-6 mb-4 mb-lg-0">
              <img
                src="https://images.pexels.com/photos/5212317/pexels-photo-5212317.jpeg?auto=compress&cs=tinysrgb&w=600"
                alt="Research"
                className="img-fluid rounded shadow"
                style={{ borderRadius: "20px" }}
              />
            </div>
            <div className="col-lg-6 ps-lg-5">
              <h2 className="display-5 fw-bold mb-4">Research & Innovation</h2>
              <p className="lead text-muted mb-4">
                Our faculty members are at the forefront of research and
                innovation, publishing in top-tier journals and conferences
                worldwide.
              </p>
              <ul className="list-unstyled">
                <li className="mb-3 d-flex align-items-center">
                  <span
                    className="badge bg-primary me-3"
                    style={{
                      width: "30px",
                      height: "30px",
                      borderRadius: "50%",
                      display: "flex",
                      alignItems: "center",
                      justifyContent: "center",
                    }}
                  >
                    ✓
                  </span>
                  <span>500+ Research papers published annually</span>
                </li>
                <li className="mb-3 d-flex align-items-center">
                  <span
                    className="badge bg-primary me-3"
                    style={{
                      width: "30px",
                      height: "30px",
                      borderRadius: "50%",
                      display: "flex",
                      alignItems: "center",
                      justifyContent: "center",
                    }}
                  >
                    ✓
                  </span>
                  <span>International collaborations and partnerships</span>
                </li>
                <li className="mb-3 d-flex align-items-center">
                  <span
                    className="badge bg-primary me-3"
                    style={{
                      width: "30px",
                      height: "30px",
                      borderRadius: "50%",
                      display: "flex",
                      alignItems: "center",
                      justifyContent: "center",
                    }}
                  >
                    ✓
                  </span>
                  <span>State-of-the-art research facilities</span>
                </li>
              </ul>
              <button
                className="btn btn-gradient btn-lg mt-3"
                onClick={() => navigate("/research")}
              >
                Explore Research
                <ArrowRight className="ms-2" size={20} />
              </button>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
};

export default Home;
