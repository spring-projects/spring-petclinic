import { useState } from "react";
import { Mail, Phone, MapPin, Send } from "lucide-react";

const Contact = () => {
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    subject: "",
    message: "",
  });

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    alert("Message sent successfully! We will get back to you soon.");
    setFormData({ name: "", email: "", subject: "", message: "" });
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
          <h1 className="display-4 fw-bold mb-3 fade-in-up">Contact Us</h1>
          <p
            className="lead fade-in-up"
            style={{
              maxWidth: "800px",
              margin: "0 auto",
              animationDelay: "0.2s",
            }}
          >
            Get in touch with us for any inquiries or assistance
          </p>
        </div>
      </section>

      <section className="py-5">
        <div className="container">
          <div className="row g-5">
            <div className="col-lg-4">
              <div className="mb-4">
                <div
                  className="d-inline-flex align-items-center justify-content-center mb-3"
                  style={{
                    width: "60px",
                    height: "60px",
                    borderRadius: "15px",
                    background:
                      "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
                  }}
                >
                  <MapPin size={30} color="white" />
                </div>
                <h5 className="fw-bold mb-2">Our Location</h5>
                <p className="text-muted">
                  123 University Avenue
                  <br />
                  Education City, EC 12345
                  <br />
                  United States
                </p>
              </div>

              <div className="mb-4">
                <div
                  className="d-inline-flex align-items-center justify-content-center mb-3"
                  style={{
                    width: "60px",
                    height: "60px",
                    borderRadius: "15px",
                    background:
                      "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
                  }}
                >
                  <Phone size={30} color="white" />
                </div>
                <h5 className="fw-bold mb-2">Phone Number</h5>
                <p className="text-muted">
                  Main: +1 (555) 123-4567
                  <br />
                  Admissions: +1 (555) 123-4568
                  <br />
                  Support: +1 (555) 123-4569
                </p>
              </div>

              <div className="mb-4">
                <div
                  className="d-inline-flex align-items-center justify-content-center mb-3"
                  style={{
                    width: "60px",
                    height: "60px",
                    borderRadius: "15px",
                    background:
                      "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
                  }}
                >
                  <Mail size={30} color="white" />
                </div>
                <h5 className="fw-bold mb-2">Email Address</h5>
                <p className="text-muted">
                  info@facultyportal.edu
                  <br />
                  admissions@facultyportal.edu
                  <br />
                  support@facultyportal.edu
                </p>
              </div>
            </div>

            <div className="col-lg-8">
              <div
                className="card border-0 shadow-sm"
                style={{ borderRadius: "20px" }}
              >
                <div className="card-body p-5">
                  <h3 className="fw-bold mb-4">Send us a Message</h3>
                  <form onSubmit={handleSubmit}>
                    <div className="row g-3">
                      <div className="col-md-6">
                        <label className="form-label fw-semibold">
                          Your Name
                        </label>
                        <input
                          type="text"
                          className="form-control"
                          name="name"
                          value={formData.name}
                          onChange={handleChange}
                          required
                          style={{ borderRadius: "10px", padding: "12px" }}
                        />
                      </div>
                      <div className="col-md-6">
                        <label className="form-label fw-semibold">
                          Your Email
                        </label>
                        <input
                          type="email"
                          className="form-control"
                          name="email"
                          value={formData.email}
                          onChange={handleChange}
                          required
                          style={{ borderRadius: "10px", padding: "12px" }}
                        />
                      </div>
                      <div className="col-12">
                        <label className="form-label fw-semibold">
                          Subject
                        </label>
                        <input
                          type="text"
                          className="form-control"
                          name="subject"
                          value={formData.subject}
                          onChange={handleChange}
                          required
                          style={{ borderRadius: "10px", padding: "12px" }}
                        />
                      </div>
                      <div className="col-12">
                        <label className="form-label fw-semibold">
                          Message
                        </label>
                        <textarea
                          className="form-control"
                          name="message"
                          value={formData.message}
                          onChange={handleChange}
                          rows="6"
                          required
                          style={{ borderRadius: "10px", padding: "12px" }}
                        ></textarea>
                      </div>
                      <div className="col-12">
                        <button
                          type="submit"
                          className="btn btn-gradient btn-lg w-100"
                        >
                          <Send size={20} className="me-2" />
                          Send Message
                        </button>
                      </div>
                    </div>
                  </form>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      <section className="py-5" style={{ backgroundColor: "#f8f9fa" }}>
        <div className="container">
          <div
            className="rounded overflow-hidden shadow"
            style={{ height: "400px" }}
          >
            <iframe
              src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3022.2412648718453!2d-73.98784492346618!3d40.75889737138558!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x89c25855c6480299%3A0x55194ec5a1ae072e!2sTimes%20Square!5e0!3m2!1sen!2sus!4v1699999999999!5m2!1sen!2sus"
              width="100%"
              height="100%"
              style={{ border: 0 }}
              allowFullScreen=""
              loading="lazy"
              referrerPolicy="no-referrer-when-downgrade"
            ></iframe>
          </div>
        </div>
      </section>
    </div>
  );
};

export default Contact;
