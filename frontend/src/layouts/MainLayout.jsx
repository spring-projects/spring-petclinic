// src/layouts/MainLayout.jsx

import React from "react";
import { Outlet } from "react-router-dom";
// Assuming Header and Footer are in src/components/
import Header from "../components/Header";
import Footer from "../components/Footer";

const MainLayout = () => {
  return (
    <div className="d-flex flex-column min-vh-100">
      <Header />
      <main className="flex-grow-1">
        <Outlet />
      </main>
      <Footer />
    </div>
  );
};

export default MainLayout;
