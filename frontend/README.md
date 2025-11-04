# Faculty Management System - Frontend

A modern, professional, and animated faculty management website built with React, Bootstrap 5, and custom CSS animations.

## Features

### Pages

- **Home Page**: Hero section with animated background, features, and call-to-action buttons
- **About Page**: Vision, mission, institution history, and statistics
- **Faculty Page**: Grid of faculty profiles with search, filter, and modal view
- **Courses Page**: List of courses with category filters and detailed information
- **Research & Publications**: Animated list of research papers, conferences, and journals
- **Events Page**: Upcoming events with calendar cards and registration
- **Login/Register Page**: Stylish authentication with gradient background animation
- **Dashboard**: Admin dashboard with stats, faculty management, and quick actions
- **Contact Page**: Contact form with Google Maps integration

### Features

- Responsive Bootstrap 5 design
- Custom CSS animations (gradients, fade-ins, hover effects)
- React Router for navigation
- Axios for API integration
- JWT authentication with AuthContext
- Protected routes for dashboard
- Modern gradient color scheme (blue, purple)

## Installation

```bash
# Install dependencies
npm install

# Start development server
npm run dev

# Build for production
npm run build
```

## Backend API Integration

The app is configured to connect to your Spring Boot + PostgreSQL backend. Set the API URL in your environment:

Create a `.env` file:

```env
VITE_API_BASE_URL=http://localhost:8080/api
```

### API Endpoints Expected

```
# Faculty
GET    /api/faculty
POST   /api/faculty
GET    /api/faculty/:id
PUT    /api/faculty/:id
DELETE /api/faculty/:id

# Courses
GET    /api/courses
POST   /api/courses
GET    /api/courses/:id
PUT    /api/courses/:id
DELETE /api/courses/:id
GET    /api/courses/category/:category

# Authentication
POST   /api/auth/login
POST   /api/auth/register
POST   /api/auth/logout
GET    /api/auth/me

# Research
GET    /api/research
POST   /api/research
GET    /api/research/:id

# Events
GET    /api/events
GET    /api/events/upcoming
POST   /api/events/:id/register

# Dashboard
GET    /api/dashboard/stats
```

## Project Structure

```
src/
├── assets/
│   └── styles/
│       └── global.css          # Custom CSS animations and styles
├── components/
│   ├── Header.jsx              # Navbar with scroll effect
│   ├── Footer.jsx              # Animated footer with links
│   ├── Button.jsx              # Reusable button component
│   ├── FacultyCard.jsx         # Faculty profile card
│   └── DashboardStats.jsx      # Dashboard statistics cards
├── pages/
│   ├── Home.jsx                # Home page
│   ├── About.jsx               # About page
│   ├── Faculty.jsx             # Faculty listing page
│   ├── Courses.jsx             # Courses page
│   ├── Research.jsx            # Research publications
│   ├── Events.jsx              # Events page
│   ├── Contact.jsx             # Contact page with form
│   ├── Login.jsx               # Login/Register page
│   └── Dashboard.jsx           # Admin dashboard
├── layouts/
│   └── MainLayout.jsx          # Main layout with header/footer
├── context/
│   └── AuthContext.jsx         # Authentication context
├── services/
│   └── api.js                  # API integration with Axios
├── router/
│   └── AppRouter.jsx           # React Router setup
├── App.jsx                     # Main app component
└── main.jsx                    # Entry point
```

## Technologies Used

- **React 18** - UI library
- **React Router v6** - Routing
- **Bootstrap 5** - UI components and grid
- **Axios** - HTTP client
- **Lucide React** - Icons
- **Custom CSS** - Animations and styling
- **Vite** - Build tool

## Styling

The app uses Bootstrap 5 for layout and components, with custom CSS for:

- Gradient backgrounds
- Smooth animations (fade-in, slide-in, float)
- Hover effects on cards
- Animated page transitions
- Custom color scheme (blue/purple gradients)

## Authentication

The app uses JWT token authentication:

- Login/Register forms
- Token stored in localStorage
- AuthContext for state management
- Protected routes (Dashboard requires auth)
- Auto-redirect on 401 errors

## Mock Data

The app includes mock data for all pages when the backend API is unavailable, making it easy to demo the UI without a backend.

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## License

MIT
