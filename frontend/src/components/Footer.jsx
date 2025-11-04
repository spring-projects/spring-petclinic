import {
  Facebook,
  Twitter,
  Linkedin,
  Instagram,
  Mail,
  Phone,
  MapPin,
} from "lucide-react";
import { Link } from "react-router-dom";

const Footer = () => {
  return (
    <footer className="footer-gradient text-white py-5">
      <div className="container">
        <div className="row">
          <div className="col-lg-4 mb-4">
            <h5 className="mb-3" style={{ fontWeight: "700" }}>
              Faculty Management System
            </h5>
            <p style={{ opacity: "0.9", lineHeight: "1.8" }}>
              Empowering education through excellence. Our faculty management
              system connects students with world-class educators and
              researchers.
            </p>
            <div className="d-flex gap-3 mt-3">
              <a
                href="#"
                className="text-white"
                style={{ transition: "transform 0.3s" }}
                onMouseOver={(e) =>
                  (e.currentTarget.style.transform = "scale(1.2)")
                }
                onMouseOut={(e) =>
                  (e.currentTarget.style.transform = "scale(1)")
                }
              >
                <Facebook size={24} />
              </a>
              <a
                href="#"
                className="text-white"
                style={{ transition: "transform 0.3s" }}
                onMouseOver={(e) =>
                  (e.currentTarget.style.transform = "scale(1.2)")
                }
                onMouseOut={(e) =>
                  (e.currentTarget.style.transform = "scale(1)")
                }
              >
                <Twitter size={24} />
              </a>
              <a
                href="#"
                className="text-white"
                style={{ transition: "transform 0.3s" }}
                onMouseOver={(e) =>
                  (e.currentTarget.style.transform = "scale(1.2)")
                }
                onMouseOut={(e) =>
                  (e.currentTarget.style.transform = "scale(1)")
                }
              >
                <Linkedin size={24} />
              </a>
              <a
                href="#"
                className="text-white"
                style={{ transition: "transform 0.3s" }}
                onMouseOver={(e) =>
                  (e.currentTarget.style.transform = "scale(1.2)")
                }
                onMouseOut={(e) =>
                  (e.currentTarget.style.transform = "scale(1)")
                }
              >
                <Instagram size={24} />
              </a>
            </div>
          </div>

          <div className="col-lg-2 col-md-6 mb-4">
            <h5 className="mb-3" style={{ fontWeight: "700" }}>
              Quick Links
            </h5>
            <ul className="list-unstyled">
              <li className="mb-2">
                <Link
                  to="/"
                  className="text-white"
                  style={{ textDecoration: "none", opacity: "0.9" }}
                >
                  Home
                </Link>
              </li>
              <li className="mb-2">
                <Link
                  to="/about"
                  className="text-white"
                  style={{ textDecoration: "none", opacity: "0.9" }}
                >
                  About Us
                </Link>
              </li>
              <li className="mb-2">
                <Link
                  to="/faculty"
                  className="text-white"
                  style={{ textDecoration: "none", opacity: "0.9" }}
                >
                  Faculty
                </Link>
              </li>
              <li className="mb-2">
                <Link
                  to="/courses"
                  className="text-white"
                  style={{ textDecoration: "none", opacity: "0.9" }}
                >
                  Courses
                </Link>
              </li>
            </ul>
          </div>

          <div className="col-lg-3 col-md-6 mb-4">
            <h5 className="mb-3" style={{ fontWeight: "700" }}>
              Resources
            </h5>
            <ul className="list-unstyled">
              <li className="mb-2">
                <Link
                  to="/research"
                  className="text-white"
                  style={{ textDecoration: "none", opacity: "0.9" }}
                >
                  Research
                </Link>
              </li>
              <li className="mb-2">
                <Link
                  to="/events"
                  className="text-white"
                  style={{ textDecoration: "none", opacity: "0.9" }}
                >
                  Events
                </Link>
              </li>
              <li className="mb-2">
                <Link
                  to="/contact"
                  className="text-white"
                  style={{ textDecoration: "none", opacity: "0.9" }}
                >
                  Contact
                </Link>
              </li>
              <li className="mb-2">
                <Link
                  to="/login"
                  className="text-white"
                  style={{ textDecoration: "none", opacity: "0.9" }}
                >
                  Faculty Login
                </Link>
              </li>
            </ul>
          </div>

          <div className="col-lg-3 mb-4">
            <h5 className="mb-3" style={{ fontWeight: "700" }}>
              Contact Info
            </h5>
            <div className="d-flex align-items-start mb-3">
              <MapPin
                size={20}
                className="me-2 mt-1"
                style={{ minWidth: "20px" }}
              />
              <span style={{ opacity: "0.9" }}>
                123 University Ave, Education City, EC 12345
              </span>
            </div>
            <div className="d-flex align-items-center mb-3">
              <Phone size={20} className="me-2" />
              <span style={{ opacity: "0.9" }}>+1 (555) 123-4567</span>
            </div>
            <div className="d-flex align-items-center">
              <Mail size={20} className="me-2" />
              <span style={{ opacity: "0.9" }}>info@facultyportal.edu</span>
            </div>
          </div>
        </div>

        <hr
          style={{ borderColor: "rgba(255,255,255,0.2)", margin: "30px 0" }}
        />

        <div className="row">
          <div className="col-md-6 text-center text-md-start">
            <p style={{ opacity: "0.8", margin: 0 }}>
              &copy; 2025 Faculty Management System. All rights reserved.
            </p>
          </div>
          <div className="col-md-6 text-center text-md-end">
            <a
              href="#"
              className="text-white me-3"
              style={{ textDecoration: "none", opacity: "0.8" }}
            >
              Privacy Policy
            </a>
            <a
              href="#"
              className="text-white"
              style={{ textDecoration: "none", opacity: "0.8" }}
            >
              Terms of Service
            </a>
          </div>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
