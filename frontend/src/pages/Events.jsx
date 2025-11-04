import { useState, useEffect } from "react";
import { eventsAPI } from "../services/api";
import { Calendar, MapPin, Clock, Users } from "lucide-react";

const Events = () => {
  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(true);

  const mockEvents = [
    {
      id: 1,
      title: "International AI Conference 2024",
      date: "2024-12-15",
      time: "09:00 AM",
      location: "Main Auditorium",
      description:
        "Join leading experts in artificial intelligence for a day of insights and networking.",
      attendees: 250,
      category: "Conference",
    },
    {
      id: 2,
      title: "Workshop: Advanced Data Analytics",
      date: "2024-11-20",
      time: "02:00 PM",
      location: "Computer Lab 3",
      description:
        "Hands-on workshop covering modern data analytics techniques and tools.",
      attendees: 50,
      category: "Workshop",
    },
    {
      id: 3,
      title: "Guest Lecture: Future of Quantum Computing",
      date: "2024-11-25",
      time: "11:00 AM",
      location: "Lecture Hall A",
      description:
        "Renowned physicist Dr. James Anderson discusses quantum computing breakthroughs.",
      attendees: 150,
      category: "Lecture",
    },
    {
      id: 4,
      title: "Research Symposium 2024",
      date: "2024-12-10",
      time: "10:00 AM",
      location: "Convention Center",
      description:
        "Annual showcase of groundbreaking research from our faculty and students.",
      attendees: 300,
      category: "Symposium",
    },
    {
      id: 5,
      title: "Career Fair: Tech Industry",
      date: "2024-11-30",
      time: "01:00 PM",
      location: "Sports Complex",
      description:
        "Connect with top tech companies and explore career opportunities.",
      attendees: 500,
      category: "Career Fair",
    },
    {
      id: 6,
      title: "Seminar: Green Chemistry Innovations",
      date: "2024-12-05",
      time: "03:00 PM",
      location: "Chemistry Building",
      description:
        "Exploring sustainable and eco-friendly approaches in modern chemistry.",
      attendees: 80,
      category: "Seminar",
    },
  ];

  useEffect(() => {
    fetchEvents();
  }, []);

  const fetchEvents = async () => {
    try {
      const response = await eventsAPI.getUpcoming();
      setEvents(response.data);
    } catch (error) {
      console.log("Using mock data");
      setEvents(mockEvents);
    } finally {
      setLoading(false);
    }
  };

  const getCategoryColor = (category) => {
    const colors = {
      Conference: "#3b82f6",
      Workshop: "#8b5cf6",
      Lecture: "#10b981",
      Symposium: "#f59e0b",
      "Career Fair": "#ef4444",
      Seminar: "#06b6d4",
    };
    return colors[category] || "#667eea";
  };

  const formatDate = (dateString) => {
    const options = { year: "numeric", month: "long", day: "numeric" };
    return new Date(dateString).toLocaleDateString("en-US", options);
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
          <h1 className="display-4 fw-bold mb-3 fade-in-up">Upcoming Events</h1>
          <p
            className="lead fade-in-up"
            style={{
              maxWidth: "800px",
              margin: "0 auto",
              animationDelay: "0.2s",
            }}
          >
            Stay connected with our academic community through conferences,
            workshops, and seminars
          </p>
        </div>
      </section>

      <section className="py-5">
        <div className="container">
          {loading ? (
            <div className="text-center py-5">
              <div className="spinner-border text-primary" role="status">
                <span className="visually-hidden">Loading...</span>
              </div>
            </div>
          ) : (
            <div className="row g-4">
              {events.map((event, index) => (
                <div key={event.id} className="col-md-6 col-lg-4">
                  <div
                    className="card event-card border-0 shadow-sm h-100 fade-in-up"
                    style={{
                      borderRadius: "15px",
                      animationDelay: `${index * 0.1}s`,
                      borderLeftColor: getCategoryColor(event.category),
                    }}
                  >
                    <div className="card-body p-4">
                      <div className="d-flex justify-content-between align-items-start mb-3">
                        <span
                          className="badge"
                          style={{
                            backgroundColor: getCategoryColor(event.category),
                            padding: "6px 15px",
                            fontSize: "0.85rem",
                          }}
                        >
                          {event.category}
                        </span>
                        <div className="d-flex align-items-center text-muted">
                          <Users size={16} className="me-1" />
                          <small>{event.attendees}</small>
                        </div>
                      </div>

                      <h5 className="fw-bold mb-3">{event.title}</h5>
                      <p className="text-muted mb-3">{event.description}</p>

                      <div className="mb-2">
                        <div className="d-flex align-items-center text-muted mb-2">
                          <Calendar
                            size={18}
                            className="me-2"
                            style={{ color: getCategoryColor(event.category) }}
                          />
                          <small>{formatDate(event.date)}</small>
                        </div>
                        <div className="d-flex align-items-center text-muted mb-2">
                          <Clock
                            size={18}
                            className="me-2"
                            style={{ color: getCategoryColor(event.category) }}
                          />
                          <small>{event.time}</small>
                        </div>
                        <div className="d-flex align-items-center text-muted">
                          <MapPin
                            size={18}
                            className="me-2"
                            style={{ color: getCategoryColor(event.category) }}
                          />
                          <small>{event.location}</small>
                        </div>
                      </div>

                      <button
                        className="btn btn-gradient w-100 mt-3"
                        onClick={() =>
                          alert("Registration feature coming soon!")
                        }
                      >
                        Register Now
                      </button>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </section>

      <section className="py-5" style={{ backgroundColor: "#f8f9fa" }}>
        <div className="container">
          <div className="row align-items-center">
            <div className="col-lg-6 mb-4 mb-lg-0">
              <h2 className="display-5 fw-bold mb-4">Host Your Event</h2>
              <p className="lead text-muted mb-4">
                Interested in organizing an event at our institution? We provide
                world-class facilities and support.
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
                  <span>Modern auditoriums and conference halls</span>
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
                  <span>Advanced audio-visual equipment</span>
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
                  <span>Professional event management support</span>
                </li>
              </ul>
              <button className="btn btn-gradient btn-lg mt-3">
                Contact Us
              </button>
            </div>
            <div className="col-lg-6">
              <img
                src="https://images.pexels.com/photos/2774556/pexels-photo-2774556.jpeg?auto=compress&cs=tinysrgb&w=600"
                alt="Events"
                className="img-fluid rounded shadow"
                style={{ borderRadius: "20px" }}
              />
            </div>
          </div>
        </div>
      </section>
    </div>
  );
};

export default Events;
