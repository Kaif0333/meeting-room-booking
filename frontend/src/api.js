const API_BASE_URL = (process.env.REACT_APP_API_BASE_URL || "/api").replace(/\/+$/, "");
const AUTH_TOKEN_KEY = "mrb_auth_token";

const parseError = async (response) => {
  const contentType = response.headers.get("content-type") || "";

  if (contentType.includes("application/json")) {
    const body = await response.json();
    if (typeof body === "string") {
      return body;
    }
    if (body?.message) {
      return body.message;
    }
    return JSON.stringify(body);
  }

  return response.text();
};

const getToken = () => localStorage.getItem(AUTH_TOKEN_KEY);
export const setToken = (token) => localStorage.setItem(AUTH_TOKEN_KEY, token);
export const clearToken = () => localStorage.removeItem(AUTH_TOKEN_KEY);

const request = async (path, options = {}) => {
  const { skipAuth = false, ...requestOptions } = options;
  const token = getToken();
  const headers = {
    "Content-Type": "application/json",
    ...(!skipAuth && token ? { Authorization: `Bearer ${token}` } : {}),
    ...(requestOptions.headers || {}),
  };

  const response = await fetch(`${API_BASE_URL}${path}`, {
    headers,
    ...requestOptions,
  });

  if (!response.ok) {
    const errorText = await parseError(response);
    throw new Error(errorText || `Request failed with status ${response.status}`);
  }

  if (response.status === 204) {
    return { data: null };
  }

  return { data: await response.json() };
};

export const getRooms = () => request("/rooms");
export const getUsers = () => request("/users");
export const getBookings = () => request("/bookings");
export const createUser = (userData) =>
  request("/users", { method: "POST", body: JSON.stringify(userData) });
export const createRoom = (roomData) =>
  request("/rooms", { method: "POST", body: JSON.stringify(roomData) });
export const createBooking = (bookingData) =>
  request("/bookings", { method: "POST", body: JSON.stringify(bookingData) });
export const cancelBooking = (id) => request(`/bookings/${id}`, { method: "DELETE" });
export const joinBooking = (bookingId, userId) =>
  request(`/bookings/${bookingId}/join`, { method: "POST", body: JSON.stringify({ userId }) });
export const leaveBooking = (bookingId, userId) =>
  request(`/bookings/${bookingId}/leave`, { method: "POST", body: JSON.stringify({ userId }) });
export const getAuditLogs = () => request("/audit");

export const register = (payload) =>
  request("/auth/register", { method: "POST", body: JSON.stringify(payload), skipAuth: true });
export const login = (payload) =>
  request("/auth/login", { method: "POST", body: JSON.stringify(payload), skipAuth: true });
export const getMe = () => request("/auth/me");
