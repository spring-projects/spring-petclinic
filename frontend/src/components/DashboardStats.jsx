// src/components/DashboardStats.jsx

import React from "react";
import { Users, BookOpen, Calendar, TrendingUp } from "lucide-react";

const DashboardStats = ({ stats }) => {
  const statsData = [
    {
      title: "Total Faculty",
      value: stats?.totalFaculty || 0,
      icon: Users,
      color: "#3b82f6",
      gradient: "linear-gradient(135deg, #3b82f6 0%, #2563eb 100%)",
    },
    {
      title: "Active Courses",
      value: stats?.activeCourses || 0,
      icon: BookOpen,
      color: "#8b5cf6",
      gradient: "linear-gradient(135deg, #8b5cf6 0%, #7c3aed 100%)",
    },
    {
      title: "Upcoming Events",
      value: stats?.upcomingEvents || 0,
      icon: Calendar,
      color: "#10b981",
      gradient: "linear-gradient(135deg, #10b981 0%, #059669 100%)",
    },
    {
      title: "Research Papers",
      value: stats?.researchPapers || 0,
      icon: TrendingUp,
      color: "#f59e0b",
      gradient: "linear-gradient(135deg, #f59e0b 0%, #d97706 100%)",
    },
  ];

  return (
    <div className="row g-4">
      {statsData.map((stat, index) => {
        const Icon = stat.icon;
        return (
          <div key={index} className="col-md-6 col-lg-3">
            <div
              className="stat-card fade-in-up"
              style={{ animationDelay: `${index * 0.1}s` }}
            >
              <div className="d-flex justify-content-between align-items-start mb-3">
                <div>
                  <p className="text-muted mb-1" style={{ fontSize: "0.9rem" }}>
                    {stat.title}
                  </p>
                  <h2
                    className="mb-0"
                    style={{ fontWeight: "700", color: "#1e293b" }}
                  >
                    {stat.value}
                  </h2>
                </div>
                <div
                  style={{
                    width: "60px",
                    height: "60px",
                    borderRadius: "15px",
                    background: stat.gradient,
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                  }}
                >
                  <Icon size={30} color="white" />
                </div>
              </div>
              <div
                className="progress"
                style={{ height: "6px", borderRadius: "3px" }}
              >
                <div
                  className="progress-bar"
                  role="progressbar"
                  style={{ width: "75%", background: stat.gradient }}
                ></div>
              </div>
            </div>
          </div>
        );
      })}
    </div>
  );
};

export default DashboardStats;
