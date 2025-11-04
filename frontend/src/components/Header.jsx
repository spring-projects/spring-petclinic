import { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { GraduationCap, User, LogOut } from "lucide-react";

const Header = () => {
  const [scrolled, setScrolled] = useState(false);
  const { user, logout, isAuthenticated } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    const handleScroll = () => {
      setScrolled(window.scrollY > 50);
    };
    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, []);

  const handleLogout = () => {
    logout();
    navigate("/");
  };

  return (
    <nav
      className={`navbar navbar-expand-lg fixed-top ${
        scrolled ? "navbar-scrolled" : "navbar-light"
      }`}
      style={{
        backgroundColor: scrolled ? "#fff" : "transparent",
        transition: "all 0.3s ease",
        padding: "15px 0",
      }}
    >
      <div className="container">
        <Link className="navbar-brand d-flex align-items-center" to="/">
          <GraduationCap
            size={32}
            className="me-2"
            style={{ color: "#667eea" }}
          />
          <span
            style={{
              fontWeight: "700",
              fontSize: "1.5rem",
              color: scrolled ? "#1e293b" : "#fff",
            }}
          >
            Faculty Portal
          </span>
        </Link>

        <button
          className="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarNav"
        >
          <span className="navbar-toggler-icon"></span>
        </button>

        <div className="collapse navbar-collapse" id="navbarNav">
          <ul className="navbar-nav ms-auto align-items-center">
            <li className="nav-item">
              <Link
                className="nav-link"
                to="/"
                style={{
                  color: scrolled ? "#1e293b" : "#fff",
                  fontWeight: "500",
                }}
              >
                Home
              </Link>
            </li>
            <li className="nav-item">
              <Link
                className="nav-link"
                to="/about"
                style={{
                  color: scrolled ? "#1e293b" : "#fff",
                  fontWeight: "500",
                }}
              >
                About
              </Link>
            </li>
            <li className="nav-item">
              <Link
                className="nav-link"
                to="/faculty"
                style={{
                  color: scrolled ? "#1e293b" : "#fff",
                  fontWeight: "500",
                }}
              >
                Faculty
              </Link>
            </li>
            <li className="nav-item">
              <Link
                className="nav-link"
                to="/courses"
                style={{
                  color: scrolled ? "#1e293b" : "#fff",
                  fontWeight: "500",
                }}
              >
                Courses
              </Link>
            </li>
            <li className="nav-item">
              <Link
                className="nav-link"
                to="/research"
                style={{
                  color: scrolled ? "#1e293b" : "#fff",
                  fontWeight: "500",
                }}
              >
                Research
              </Link>
            </li>
            <li className="nav-item">
              <Link
                className="nav-link"
                to="/events"
                style={{
                  color: scrolled ? "#1e293b" : "#fff",
                  fontWeight: "500",
                }}
              >
                Events
              </Link>
            </li>
            <li className="nav-item">
              <Link
                className="nav-link"
                to="/contact"
                style={{
                  color: scrolled ? "#1e293b" : "#fff",
                  fontWeight: "500",
                }}
              >
                Contact
              </Link>
            </li>
            {isAuthenticated() ? (
              <>
                <li className="nav-item">
                  <Link
                    className="nav-link"
                    to="/dashboard"
                    style={{
                      color: scrolled ? "#1e293b" : "#fff",
                      fontWeight: "500",
                    }}
                  >
                    Dashboard
                  </Link>
                </li>
                <li className="nav-item dropdown">
                  <a
                    className="nav-link dropdown-toggle d-flex align-items-center"
                    href="#"
                    role="button"
                    data-bs-toggle="dropdown"
                    style={{ color: scrolled ? "#1e293b" : "#fff" }}
                  >
                    <User size={20} className="me-1" />
                    {user?.name || "User"}
                  </a>
                  <ul className="dropdown-menu">
                    <li>
                      <button className="dropdown-item" onClick={handleLogout}>
                        <LogOut size={16} className="me-2" />
                        Logout
                      </button>
                    </li>
                  </ul>
                </li>
              </>
            ) : (
              <li className="nav-item ms-2">
                <Link
                  className="btn btn-gradient px-4"
                  to="/login"
                  style={{ borderRadius: "25px" }}
                >
                  Login
                </Link>
              </li>
            )}
          </ul>
        </div>
      </div>
    </nav>
  );
};

export default Header;
