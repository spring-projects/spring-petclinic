import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { authAPI } from "../services/api";
import { Lock, Mail, User } from "lucide-react";

const Login = () => {
  const [isLogin, setIsLogin] = useState(true);
  const [formData, setFormData] = useState({
    email: "",
    password: "",
    name: "",
  });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
    setError("");
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");

    try {
      if (isLogin) {
        const response = await authAPI.login({
          email: formData.email,
          password: formData.password,
        });
        login(response.data.user, response.data.token);
        navigate("/dashboard");
      } else {
        const response = await authAPI.register(formData);
        login(response.data.user, response.data.token);
        navigate("/dashboard");
      }
    } catch (err) {
      setError(
        err.response?.data?.message ||
          "Authentication failed. Please try again."
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-container animated-gradient">
      <div className="container">
        <div className="row justify-content-center">
          <div className="col-md-10 col-lg-8">
            <div className="login-card">
              <div className="row g-0">
                <div
                  className="col-lg-6 d-none d-lg-flex align-items-center justify-content-center p-5"
                  style={{
                    background:
                      "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
                  }}
                >
                  <div className="text-white text-center">
                    <h2 className="display-5 fw-bold mb-4">Welcome Back!</h2>
                    <p className="lead mb-4">
                      Access your faculty portal and manage your academic
                      journey
                    </p>
                    <img
                      src="https://images.pexels.com/photos/3184465/pexels-photo-3184465.jpeg?auto=compress&cs=tinysrgb&w=400"
                      alt="Education"
                      className="img-fluid rounded shadow"
                      style={{
                        maxWidth: "300px",
                        animation: "float 6s ease-in-out infinite",
                      }}
                    />
                  </div>
                </div>

                <div className="col-lg-6 p-5">
                  <div className="mb-4">
                    <h3 className="fw-bold mb-2">
                      {isLogin ? "Login" : "Register"}
                    </h3>
                    <p className="text-muted">
                      {isLogin
                        ? "Enter your credentials to access your account"
                        : "Create your faculty account"}
                    </p>
                  </div>

                  {error && (
                    <div className="alert alert-danger" role="alert">
                      {error}
                    </div>
                  )}

                  <form onSubmit={handleSubmit}>
                    {!isLogin && (
                      <div className="mb-3">
                        <label className="form-label fw-semibold">
                          Full Name
                        </label>
                        <div className="input-group">
                          <span className="input-group-text bg-light border-end-0">
                            <User size={20} />
                          </span>
                          <input
                            type="text"
                            className="form-control border-start-0"
                            name="name"
                            value={formData.name}
                            onChange={handleChange}
                            required={!isLogin}
                            placeholder="Enter your full name"
                          />
                        </div>
                      </div>
                    )}

                    <div className="mb-3">
                      <label className="form-label fw-semibold">
                        Email Address
                      </label>
                      <div className="input-group">
                        <span className="input-group-text bg-light border-end-0">
                          <Mail size={20} />
                        </span>
                        <input
                          type="email"
                          className="form-control border-start-0"
                          name="email"
                          value={formData.email}
                          onChange={handleChange}
                          required
                          placeholder="Enter your email"
                        />
                      </div>
                    </div>

                    <div className="mb-3">
                      <label className="form-label fw-semibold">Password</label>
                      <div className="input-group">
                        <span className="input-group-text bg-light border-end-0">
                          <Lock size={20} />
                        </span>
                        <input
                          type="password"
                          className="form-control border-start-0"
                          name="password"
                          value={formData.password}
                          onChange={handleChange}
                          required
                          placeholder="Enter your password"
                        />
                      </div>
                    </div>

                    {isLogin && (
                      <div className="mb-3 text-end">
                        <a
                          href="#"
                          className="text-decoration-none"
                          style={{ color: "#667eea" }}
                        >
                          Forgot Password?
                        </a>
                      </div>
                    )}

                    <button
                      type="submit"
                      className="btn btn-gradient w-100 btn-lg mb-3"
                      disabled={loading}
                    >
                      {loading ? (
                        <span
                          className="spinner-border spinner-border-sm me-2"
                          role="status"
                          aria-hidden="true"
                        ></span>
                      ) : null}
                      {isLogin ? "Login" : "Register"}
                    </button>

                    <div className="text-center">
                      <p className="text-muted mb-0">
                        {isLogin
                          ? "Don't have an account? "
                          : "Already have an account? "}
                        <button
                          type="button"
                          className="btn btn-link p-0 text-decoration-none"
                          onClick={() => {
                            setIsLogin(!isLogin);
                            setError("");
                            setFormData({ email: "", password: "", name: "" });
                          }}
                          style={{ color: "#667eea", fontWeight: "600" }}
                        >
                          {isLogin ? "Register here" : "Login here"}
                        </button>
                      </p>
                    </div>
                  </form>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Login;
