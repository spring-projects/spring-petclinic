import { Target, Eye, Book, Award } from "lucide-react";

const About = () => {
  const values = [
    {
      icon: Target,
      title: "Our Mission",
      description:
        "To provide world-class education through dedicated faculty members who are committed to academic excellence and student success.",
    },
    {
      icon: Eye,
      title: "Our Vision",
      description:
        "To be a globally recognized institution that shapes future leaders and innovators through cutting-edge research and teaching.",
    },
    {
      icon: Book,
      title: "Academic Excellence",
      description:
        "Fostering an environment of intellectual curiosity, critical thinking, and lifelong learning for all our students.",
    },
    {
      icon: Award,
      title: "Research Impact",
      description:
        "Conducting groundbreaking research that addresses real-world challenges and contributes to societal advancement.",
    },
  ];

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
            About Our Institution
          </h1>
          <p
            className="lead fade-in-up"
            style={{
              maxWidth: "800px",
              margin: "0 auto",
              animationDelay: "0.2s",
            }}
          >
            A legacy of excellence in education, research, and innovation
          </p>
        </div>
      </section>

      <section className="py-5">
        <div className="container">
          <div className="row g-4">
            {values.map((value, index) => {
              const Icon = value.icon;
              return (
                <div key={index} className="col-md-6">
                  <div
                    className="card border-0 shadow-sm h-100 card-hover"
                    style={{
                      borderRadius: "15px",
                      animationDelay: `${index * 0.1}s`,
                    }}
                  >
                    <div className="card-body p-4">
                      <div
                        className="mb-3"
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
                        <Icon size={35} color="white" />
                      </div>
                      <h4 className="fw-bold mb-3">{value.title}</h4>
                      <p
                        className="text-muted mb-0"
                        style={{ lineHeight: "1.8" }}
                      >
                        {value.description}
                      </p>
                    </div>
                  </div>
                </div>
              );
            })}
          </div>
        </div>
      </section>

      <section className="py-5" style={{ backgroundColor: "#f8f9fa" }}>
        <div className="container">
          <div className="row align-items-center">
            <div className="col-lg-6 mb-4 mb-lg-0">
              <h2 className="display-5 fw-bold mb-4">Our History</h2>
              <p className="lead text-muted mb-4">
                Established in 1950, our institution has been a beacon of
                knowledge and excellence for over seven decades.
              </p>
              <p className="text-muted mb-3" style={{ lineHeight: "1.8" }}>
                From humble beginnings with just 50 students and 10 faculty
                members, we have grown into a premier educational institution
                serving thousands of students annually. Our commitment to
                academic excellence, innovative research, and community
                engagement has remained unwavering throughout our journey.
              </p>
              <p className="text-muted" style={{ lineHeight: "1.8" }}>
                Today, we stand proud with state-of-the-art facilities,
                world-renowned faculty, and a vibrant community of learners who
                continue to push the boundaries of knowledge and innovation.
              </p>
            </div>
            <div className="col-lg-6">
              <img
                src="https://images.pexels.com/photos/5676744/pexels-photo-5676744.jpeg?auto=compress&cs=tinysrgb&w=600"
                alt="Campus"
                className="img-fluid rounded shadow"
                style={{ borderRadius: "20px" }}
              />
            </div>
          </div>
        </div>
      </section>

      <section className="py-5">
        <div className="container">
          <div className="text-center mb-5">
            <h2 className="section-title display-5 fw-bold">By The Numbers</h2>
          </div>
          <div className="row g-4">
            <div className="col-md-3 col-sm-6">
              <div className="text-center">
                <h1
                  className="display-3 fw-bold mb-2"
                  style={{
                    background:
                      "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
                    WebkitBackgroundClip: "text",
                    WebkitTextFillColor: "transparent",
                  }}
                >
                  500+
                </h1>
                <p className="text-muted fw-semibold">Faculty Members</p>
              </div>
            </div>
            <div className="col-md-3 col-sm-6">
              <div className="text-center">
                <h1
                  className="display-3 fw-bold mb-2"
                  style={{
                    background:
                      "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
                    WebkitBackgroundClip: "text",
                    WebkitTextFillColor: "transparent",
                  }}
                >
                  15K+
                </h1>
                <p className="text-muted fw-semibold">Students</p>
              </div>
            </div>
            <div className="col-md-3 col-sm-6">
              <div className="text-center">
                <h1
                  className="display-3 fw-bold mb-2"
                  style={{
                    background:
                      "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
                    WebkitBackgroundClip: "text",
                    WebkitTextFillColor: "transparent",
                  }}
                >
                  200+
                </h1>
                <p className="text-muted fw-semibold">Programs</p>
              </div>
            </div>
            <div className="col-md-3 col-sm-6">
              <div className="text-center">
                <h1
                  className="display-3 fw-bold mb-2"
                  style={{
                    background:
                      "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
                    WebkitBackgroundClip: "text",
                    WebkitTextFillColor: "transparent",
                  }}
                >
                  75+
                </h1>
                <p className="text-muted fw-semibold">Years of Excellence</p>
              </div>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
};

export default About;
