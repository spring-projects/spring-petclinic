import axios from "axios";

// Base URL for your Spring Boot Backend
const API_URL = "http://localhost:8080/api";

const api = axios.create({
  baseURL: API_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

// Request Interceptor: Attach JWT token to all outgoing requests
api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// API Functions
export const authService = {
  login: (credentials) => api.post("/auth/login", credentials), // { username, password }
};

export const facultyService = {
  getAllFaculty: () => api.get("/faculty"),
  getFacultyById: (id) => api.get(`/faculty/${id}`),
  addFaculty: (facultyData) => api.post("/faculty", facultyData),
  deleteFaculty: (id) => api.delete(`/faculty/${id}`),
};

export const courseService = {
  getAllCourses: () => api.get("/courses"),
  // Add other course methods as needed
};

// You can export the raw axios instance too if needed
export default api;
