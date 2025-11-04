import { useState, useEffect } from "react";
import { coursesAPI } from "../services/api";
import { Clock, Users, BookOpen } from "lucide-react";

const Courses = () => {
  const [courses, setCourses] = useState([]);
  const [filteredCourses, setFilteredCourses] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState("all");
  const [loading, setLoading] = useState(true);

  const mockCourses = [
    {
      id: 1,
      title: "Advanced Machine Learning",
      category: "Computer Science",
      instructor: "Dr. Sarah Johnson",
      duration: "12 weeks",
      students: 45,
      level: "Advanced",
      description:
        "Deep dive into neural networks, deep learning, and AI applications.",
    },
    {
      id: 2,
      title: "Calculus III",
      category: "Mathematics",
      instructor: "Prof. Michael Chen",
      duration: "16 weeks",
      students: 60,
      level: "Intermediate",
      description:
        "Multivariable calculus, vector analysis, and differential equations.",
    },
    {
      id: 3,
      title: "Quantum Mechanics",
      category: "Physics",
      instructor: "Dr. Emily Rodriguez",
      duration: "14 weeks",
      students: 35,
      level: "Advanced",
      description: "Fundamentals of quantum theory and its applications.",
    },
    {
      id: 4,
      title: "Organic Chemistry II",
      category: "Chemistry",
      instructor: "Prof. David Williams",
      duration: "15 weeks",
      students: 50,
      level: "Intermediate",
      description: "Advanced organic reactions and synthesis techniques.",
    },
    {
      id: 5,
      title: "Molecular Biology",
      category: "Biology",
      instructor: "Dr. Lisa Anderson",
      duration: "12 weeks",
      students: 40,
      level: "Intermediate",
      description: "DNA, RNA, protein synthesis, and genetic engineering.",
    },
    {
      id: 6,
      title: "Web Development",
      category: "Computer Science",
      instructor: "Prof. James Taylor",
      duration: "10 weeks",
      students: 55,
      level: "Beginner",
      description: "Full-stack web development using modern frameworks.",
    },
  ];

  useEffect(() => {
    fetchCourses();
  }, []);

  useEffect(() => {
    filterCourses();
  }, [selectedCategory, courses]);

  const fetchCourses = async () => {
    try {
      const response = await coursesAPI.getAll();
      setCourses(response.data);
    } catch (error) {
      console.log("Using mock data");
      setCourses(mockCourses);
    } finally {
      setLoading(false);
    }
  };

  const filterCourses = () => {
    if (selectedCategory === "all") {
      setFilteredCourses(courses);
    } else {
      setFilteredCourses(
        courses.filter((c) => c.category === selectedCategory)
      );
    }
  };

  const categories = ["all", ...new Set(courses.map((c) => c.category))];

  const getLevelColor = (level) => {
    switch (level) {
      case "Beginner":
        return "bg-success";
      case "Intermediate":
        return "bg-warning";
      case "Advanced":
        return "bg-danger";
      default:
        return "bg-primary";
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
          <h1 className="display-4 fw-bold mb-3 fade-in-up">Our Courses</h1>
          <p
            className="lead fade-in-up"
            style={{
              maxWidth: "800px",
              margin: "0 auto",
              animationDelay: "0.2s",
            }}
          >
            Explore our comprehensive range of academic programs
          </p>
        </div>
      </section>

      <section className="py-5">
        <div className="container">
          <div className="mb-4">
            <div className="d-flex gap-2 flex-wrap justify-content-center">
              {categories.map((category) => (
                <button
                  key={category}
                  className={`course-badge ${
                    selectedCategory === category
                      ? "btn-gradient text-white"
                      : "bg-light text-dark"
                  }`}
                  onClick={() => setSelectedCategory(category)}
                  style={{ border: "none", cursor: "pointer" }}
                >
                  {category === "all" ? "All Courses" : category}
                </button>
              ))}
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
              {filteredCourses.map((course, index) => (
                <div key={course.id} className="col-md-6 col-lg-4">
                  <div
                    className="card border-0 shadow-sm card-hover h-100 fade-in-up"
                    style={{
                      borderRadius: "15px",
                      animationDelay: `${index * 0.1}s`,
                    }}
                  >
                    <div
                      className="card-header border-0"
                      style={{
                        background:
                          "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
                        borderRadius: "15px 15px 0 0",
                        padding: "20px",
                      }}
                    >
                      <h5 className="card-title text-white fw-bold mb-0">
                        {course.title}
                      </h5>
                    </div>
                    <div className="card-body p-4">
                      <div className="mb-3">
                        <span
                          className={`badge ${getLevelColor(
                            course.level
                          )} me-2`}
                        >
                          {course.level}
                        </span>
                        <span className="badge bg-light text-dark">
                          {course.category}
                        </span>
                      </div>
                      <p className="text-muted mb-3">{course.description}</p>
                      <p className="mb-2">
                        <strong>Instructor:</strong> {course.instructor}
                      </p>
                      <div className="d-flex justify-content-between align-items-center mt-3 pt-3 border-top">
                        <div className="d-flex align-items-center">
                          <Clock size={18} className="me-2 text-primary" />
                          <small>{course.duration}</small>
                        </div>
                        <div className="d-flex align-items-center">
                          <Users size={18} className="me-2 text-primary" />
                          <small>{course.students} students</small>
                        </div>
                      </div>
                    </div>
                    <div
                      className="card-footer bg-white border-0 p-4"
                      style={{ borderRadius: "0 0 15px 15px" }}
                    >
                      <button className="btn btn-gradient w-100">
                        <BookOpen size={18} className="me-2" />
                        Enroll Now
                      </button>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          )}

          {filteredCourses.length === 0 && !loading && (
            <div className="text-center py-5">
              <p className="text-muted">No courses found in this category.</p>
            </div>
          )}
        </div>
      </section>
    </div>
  );
};

export default Courses;
