import React from "react";
import { Routes, Route, Navigate } from "react-router-dom";
// Import Layout
import MainLayout from "../layouts/MainLayout";
// Import Pages
import Home from "../pages/Home";
import About from "../pages/About";
import Faculty from "../pages/Faculty";
import Courses from "../pages/Courses";
import Research from "../pages/Research";
import Events from "../pages/Events";
import Contact from "../pages/Contact";
import Login from "../pages/Login";
import Dashboard from "../pages/Dashboard";
import NotFound from "../pages/NotFound";
// Import Context
import { useAuth } from "../context/AuthContext";

// --- FIX: Using standard v6 pattern for a cleaner ProtectedRoute ---
/**
 * Custom wrapper component to restrict access to a route.
 * Renders the children (the target component) if authenticated,
 * otherwise navigates to the login page.
 */
const ProtectedRoute = ({ children }) => {
  const { isAuthenticated } = useAuth();

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  return children;
};
// ------------------------------------------------------------------

const AppRouter = () => {
  return (
    <Routes>
      {/* Routes using MainLayout (Public + Protected) */}
      <Route element={<MainLayout />}>
        <Route path="/" element={<Home />} />
        <Route path="/about" element={<About />} />
        <Route path="/faculty" element={<Faculty />} />
        <Route path="/courses" element={<Courses />} />
        <Route path="/research" element={<Research />} />
        <Route path="/events" element={<Events />} />
        <Route path="/contact" element={<Contact />} />

        {/* Protected Dashboard Route */}
        <Route
          path="/dashboard"
          // Pass Dashboard as a child of ProtectedRoute
          element={
            <ProtectedRoute>
              <Dashboard />
            </ProtectedRoute>
          }
        />
      </Route>

      {/* Standalone Routes (no MainLayout) */}
      <Route path="/login" element={<Login />} />
      <Route path="*" element={<NotFound />} />
    </Routes>
  );
};

export default AppRouter;
