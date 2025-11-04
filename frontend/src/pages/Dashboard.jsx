import { useState, useEffect } from "react";
import { useAuth } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";
import { dashboardAPI, facultyAPI, coursesAPI } from "../services/api";
import DashboardStats from "../components/DashboardStats";
import { Plus, Edit, Trash2, Eye } from "lucide-react";

const Dashboard = () => {
  const { user, isAuthenticated } = useAuth();
  const navigate = useNavigate();
  const [stats, setStats] = useState({});
  const [faculty, setFaculty] = useState([]);
  const [loading, setLoading] = useState(true);

  const mockStats = {
    totalFaculty: 156,
    activeCourses: 48,
    upcomingEvents: 12,
    researchPapers: 234,
  };

  const mockFaculty = [
    {
      id: 1,
      name: "Dr. Sarah Johnson",
      subject: "Computer Science",
      email: "sarah.j@edu.com",
      status: "Active",
    },
    {
      id: 2,
      name: "Prof. Michael Chen",
      subject: "Mathematics",
      email: "michael.c@edu.com",
      status: "Active",
    },
    {
      id: 3,
      name: "Dr. Emily Rodriguez",
      subject: "Physics",
      email: "emily.r@edu.com",
      status: "Active",
    },
  ];

  useEffect(() => {
    if (!isAuthenticated()) {
      navigate("/login");
      return;
    }
    fetchDashboardData();
  }, [isAuthenticated, navigate]);

  const fetchDashboardData = async () => {
    try {
      const [statsRes, facultyRes] = await Promise.all([
        dashboardAPI.getStats(),
        facultyAPI.getAll(),
      ]);
      setStats(statsRes.data);
      setFaculty(facultyRes.data.slice(0, 5));
    } catch (error) {
      console.log("Using mock data");
      setStats(mockStats);
      setFaculty(mockFaculty);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    if (
      window.confirm("Are you sure you want to delete this faculty member?")
    ) {
      try {
        await facultyAPI.delete(id);
        fetchDashboardData();
      } catch (error) {
        alert("Failed to delete faculty member");
      }
    }
  };

  if (!isAuthenticated()) {
    return null;
  }

  return (
    <div
      style={{
        paddingTop: "80px",
        backgroundColor: "#f8f9fa",
        minHeight: "100vh",
      }}
    >
      <section className="py-5">
        <div className="container">
          <div className="mb-4 fade-in-up">
            <h1 className="display-5 fw-bold mb-2">
              Welcome back, {user?.name || "Admin"}!
            </h1>
            <p className="text-muted">
              Here's what's happening with your faculty portal today.
            </p>
          </div>

          {loading ? (
            <div className="text-center py-5">
              <div className="spinner-border text-primary" role="status">
                <span className="visually-hidden">Loading...</span>
              </div>
            </div>
          ) : (
            <>
              <DashboardStats stats={stats} />

              <div className="mt-5">
                <div
                  className="card border-0 shadow-sm"
                  style={{ borderRadius: "15px" }}
                >
                  <div className="card-body p-4">
                    <div className="d-flex justify-content-between align-items-center mb-4">
                      <h4 className="fw-bold mb-0">Recent Faculty Members</h4>
                      <button className="btn btn-gradient">
                        <Plus size={20} className="me-2" />
                        Add Faculty
                      </button>
                    </div>

                    <div className="table-responsive">
                      <table className="table table-hover">
                        <thead>
                          <tr>
                            <th>Name</th>
                            <th>Subject</th>
                            <th>Email</th>
                            <th>Status</th>
                            <th>Actions</th>
                          </tr>
                        </thead>
                        <tbody>
                          {faculty.map((member) => (
                            <tr key={member.id}>
                              <td className="fw-semibold">{member.name}</td>
                              <td>{member.subject}</td>
                              <td>{member.email}</td>
                              <td>
                                <span className="badge bg-success">
                                  {member.status || "Active"}
                                </span>
                              </td>
                              <td>
                                <div className="d-flex gap-2">
                                  <button
                                    className="btn btn-sm btn-outline-primary"
                                    title="View"
                                  >
                                    <Eye size={16} />
                                  </button>
                                  <button
                                    className="btn btn-sm btn-outline-secondary"
                                    title="Edit"
                                  >
                                    <Edit size={16} />
                                  </button>
                                  <button
                                    className="btn btn-sm btn-outline-danger"
                                    onClick={() => handleDelete(member.id)}
                                    title="Delete"
                                  >
                                    <Trash2 size={16} />
                                  </button>
                                </div>
                              </td>
                            </tr>
                          ))}
                        </tbody>
                      </table>
                    </div>

                    <div className="text-center mt-3">
                      <button
                        className="btn btn-outline-primary"
                        onClick={() => navigate("/faculty")}
                      >
                        View All Faculty
                      </button>
                    </div>
                  </div>
                </div>
              </div>

              <div className="row g-4 mt-4">
                <div className="col-md-6">
                  <div
                    className="card border-0 shadow-sm h-100"
                    style={{ borderRadius: "15px" }}
                  >
                    <div className="card-body p-4">
                      <h5 className="fw-bold mb-3">Quick Actions</h5>
                      <div className="d-grid gap-2">
                        <button className="btn btn-outline-primary text-start">
                          <Plus size={18} className="me-2" />
                          Add New Faculty
                        </button>
                        <button className="btn btn-outline-primary text-start">
                          <Plus size={18} className="me-2" />
                          Create Course
                        </button>
                        <button className="btn btn-outline-primary text-start">
                          <Plus size={18} className="me-2" />
                          Schedule Event
                        </button>
                      </div>
                    </div>
                  </div>
                </div>

                <div className="col-md-6">
                  <div
                    className="card border-0 shadow-sm h-100"
                    style={{ borderRadius: "15px" }}
                  >
                    <div className="card-body p-4">
                      <h5 className="fw-bold mb-3">Recent Activity</h5>
                      <ul className="list-unstyled mb-0">
                        <li className="mb-3 pb-3 border-bottom">
                          <small className="text-muted">2 hours ago</small>
                          <p className="mb-0">
                            Dr. Sarah Johnson published a new research paper
                          </p>
                        </li>
                        <li className="mb-3 pb-3 border-bottom">
                          <small className="text-muted">5 hours ago</small>
                          <p className="mb-0">
                            New course "Advanced AI" has been created
                          </p>
                        </li>
                        <li className="mb-0">
                          <small className="text-muted">Yesterday</small>
                          <p className="mb-0">
                            Prof. Michael Chen updated course materials
                          </p>
                        </li>
                      </ul>
                    </div>
                  </div>
                </div>
              </div>
            </>
          )}
        </div>
      </section>
    </div>
  );
};

export default Dashboard;
