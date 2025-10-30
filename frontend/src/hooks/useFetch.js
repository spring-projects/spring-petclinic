import { useState, useEffect } from "react";
import axios from "axios";
import { useAuth } from "../context/AuthContext"; // To potentially use the JWT token

/**
 * Custom hook for fetching data from an API endpoint.
 * Handles loading, data, and error states, and includes JWT authorization.
 * * @param {string} url - The API endpoint URL to fetch from.
 * @param {object} options - Optional Axios request configuration (e.g., method, data).
 * @returns {{data: any, loading: boolean, error: Error | null, refetch: () => void}}
 */
const useFetch = (url, options = {}) => {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Get the current JWT token from context (or local storage directly)
  // NOTE: This assumes useAuth is working and provides the token or access to it.
  const { token } = useAuth();

  const fetchApiData = async (signal) => {
    setLoading(true);
    setError(null);

    try {
      const headers = {
        "Content-Type": "application/json",
        ...options.headers,
      };

      // If a token exists, add the Authorization header
      if (token) {
        headers["Authorization"] = `Bearer ${token}`;
      }

      const response = await axios({
        method: "GET", // Default to GET
        url,
        signal,
        ...options,
        headers: headers,
      });

      setData(response.data);
    } catch (err) {
      if (axios.isCancel(err)) {
        // Ignore AbortController cancellation errors
        return;
      }
      setError(err);
      console.error(`Error fetching ${url}:`, err);
    } finally {
      // Ensure loading is set to false, even on error
      if (!signal.aborted) {
        setLoading(false);
      }
    }
  };

  useEffect(() => {
    // AbortController prevents state updates on unmounted components
    const controller = new AbortController();
    const signal = controller.signal;

    fetchApiData(signal);

    // Cleanup function to cancel the request if the component unmounts
    return () => {
      controller.abort();
    };
  }, [url, token]); // Re-run if URL or token changes

  // Function to manually trigger a refetch
  const refetch = () => {
    fetchApiData(new AbortController().signal);
  };

  return { data, loading, error, refetch };
};

export default useFetch;
