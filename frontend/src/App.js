import React, { useCallback, useEffect, useMemo, useState } from "react";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import {
  cancelBooking,
  clearToken,
  getAuditLogs,
  getBookings,
  getMe,
  getRooms,
  getUsers,
  joinBooking,
  leaveBooking,
  login,
  register,
  setToken,
} from "./api";
import RoomForm from "./components/RoomForm";
import UserForm from "./components/UserForm";
import BookingForm from "./components/BookingForm";

function App() {
  const [rooms, setRooms] = useState([]);
  const [users, setUsers] = useState([]);
  const [bookings, setBookings] = useState([]);
  const [auditLogs, setAuditLogs] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const [currentUser, setCurrentUser] = useState(null);
  const [authMode, setAuthMode] = useState("login");
  const [authForm, setAuthForm] = useState({ name: "", email: "", password: "" });
  const [authLoading, setAuthLoading] = useState(false);

  const isAdmin = currentUser?.role === "ADMIN";

  const notifySuccess = useCallback((message) => {
    setSuccess(message);
    setTimeout(() => setSuccess(""), 3000);
  }, []);

  const handleError = useCallback((message) => {
    setError(message);
  }, []);

  const loadData = useCallback(async () => {
    if (!currentUser) {
      return;
    }
    setIsLoading(true);
    setError("");
    try {
      const [roomsRes, usersRes, bookingsRes] = await Promise.all([
        getRooms(),
        getUsers(),
        getBookings(),
      ]);
      setRooms(roomsRes.data);
      setUsers(usersRes.data);
      setBookings(bookingsRes.data);
      if (isAdmin) {
        const auditRes = await getAuditLogs();
        setAuditLogs(auditRes.data);
      } else {
        setAuditLogs([]);
      }
    } catch (err) {
      setError(err.message || "Failed to load data");
    } finally {
      setIsLoading(false);
    }
  }, [currentUser, isAdmin]);

  useEffect(() => {
    const bootstrap = async () => {
      try {
        const me = await getMe();
        setCurrentUser(me.data);
      } catch {
        clearToken();
      }
    };
    bootstrap();
  }, []);

  useEffect(() => {
    loadData();
  }, [loadData]);

  useEffect(() => {
    if (!currentUser) {
      return undefined;
    }

    const client = new Client({
      webSocketFactory: () => new SockJS("/ws"),
      reconnectDelay: 5000,
      onConnect: () => {
        client.subscribe("/topic/bookings", () => {
          loadData();
        });
      },
    });
    client.activate();

    return () => {
      client.deactivate();
    };
  }, [currentUser, loadData]);

  const userMap = useMemo(() => {
    const map = new Map();
    users.forEach((u) => map.set(u.id, u));
    return map;
  }, [users]);

  const roomMap = useMemo(() => {
    const map = new Map();
    rooms.forEach((r) => map.set(r.id, r));
    return map;
  }, [rooms]);

  const getUserName = (id) => userMap.get(id)?.name || id;
  const getRoomName = (id) => roomMap.get(id)?.name || id;

  const formatDateTime = (value) => {
    const parsed = new Date(value);
    return Number.isNaN(parsed.getTime()) ? value : parsed.toLocaleString();
  };

  const handleAuthSubmit = async (e) => {
    e.preventDefault();
    setAuthLoading(true);
    setError("");
    try {
      const response = authMode === "login"
        ? await login({ email: authForm.email, password: authForm.password })
        : await register(authForm);
      setToken(response.data.token);
      setCurrentUser(response.data);
      setAuthForm({ name: "", email: "", password: "" });
      notifySuccess(authMode === "login" ? "Logged in successfully." : "Registered successfully.");
    } catch (err) {
      setError(err.message || "Authentication failed");
    } finally {
      setAuthLoading(false);
    }
  };

  const handleLogout = () => {
    clearToken();
    setCurrentUser(null);
    setRooms([]);
    setUsers([]);
    setBookings([]);
    setAuditLogs([]);
    setSuccess("");
  };

  const handleCancelBooking = async (id) => {
    try {
      await cancelBooking(id);
      await loadData();
      notifySuccess("Booking canceled.");
    } catch (err) {
      setError(err.message || "Failed to cancel booking");
    }
  };

  const handleJoinMeeting = async (bookingId) => {
    try {
      await joinBooking(bookingId, currentUser.userId);
      await loadData();
      notifySuccess("Joined meeting successfully.");
    } catch (err) {
      setError(err.message || "Failed to join meeting");
    }
  };

  const handleLeaveMeeting = async (bookingId) => {
    try {
      await leaveBooking(bookingId, currentUser.userId);
      await loadData();
      notifySuccess("Left meeting successfully.");
    } catch (err) {
      setError(err.message || "Failed to leave meeting");
    }
  };

  if (!currentUser) {
    return (
      <div className="container mt-5" style={{ maxWidth: "500px" }}>
        <h2 className="mb-4">Meeting Room Platform</h2>
        {error && <div className="alert alert-danger">{error}</div>}
        <div className="card p-4">
          <div className="btn-group mb-3">
            <button
              type="button"
              className={`btn ${authMode === "login" ? "btn-primary" : "btn-outline-primary"}`}
              onClick={() => setAuthMode("login")}
            >
              Login
            </button>
            <button
              type="button"
              className={`btn ${authMode === "register" ? "btn-primary" : "btn-outline-primary"}`}
              onClick={() => setAuthMode("register")}
            >
              Register
            </button>
          </div>
          <form onSubmit={handleAuthSubmit}>
            {authMode === "register" && (
              <input
                className="form-control mb-2"
                placeholder="Name"
                value={authForm.name}
                onChange={(e) => setAuthForm({ ...authForm, name: e.target.value })}
                required
              />
            )}
            <input
              className="form-control mb-2"
              placeholder="Email"
              type="email"
              value={authForm.email}
              onChange={(e) => setAuthForm({ ...authForm, email: e.target.value })}
              required
            />
            <input
              className="form-control mb-3"
              placeholder="Password"
              type="password"
              value={authForm.password}
              onChange={(e) => setAuthForm({ ...authForm, password: e.target.value })}
              required
            />
            <button className="btn btn-success w-100" disabled={authLoading}>
              {authLoading ? "Please wait..." : authMode === "login" ? "Login" : "Create Account"}
            </button>
          </form>
          <div className="mt-3 text-muted small">
            Default admin: <code>admin@meeting.local / Admin@12345</code>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="container mt-4">
      <div className="d-flex justify-content-between align-items-center mb-3">
        <h1 className="mb-0">Meeting Room Booking System</h1>
        <div>
          <span className="me-3">{currentUser.name} ({currentUser.role})</span>
          <button type="button" className="btn btn-outline-dark btn-sm" onClick={handleLogout}>Logout</button>
        </div>
      </div>

      {error && <div className="alert alert-danger">{error}</div>}
      {success && <div className="alert alert-success">{success}</div>}
      {isLoading && <div className="alert alert-info">Loading dashboard...</div>}

      <div className="row">
        <div className="col-md-6">
          {isAdmin && (
            <>
              <RoomForm
                onRoomCreated={async () => {
                  await loadData();
                  notifySuccess("Room created.");
                }}
                onError={handleError}
              />
              <UserForm
                onUserCreated={async () => {
                  await loadData();
                  notifySuccess("User created.");
                }}
                onError={handleError}
              />
            </>
          )}
          {!isAdmin && (
            <div className="alert alert-secondary">
              Standard user access: create bookings, join/leave meetings, and view room availability.
            </div>
          )}
        </div>

        <div className="col-md-6">
          <BookingForm
            rooms={rooms}
            currentUser={currentUser}
            onBookingCreated={async () => {
              await loadData();
              notifySuccess("Booking created.");
            }}
            onError={handleError}
          />
          <h4>Bookings</h4>
          <div className="table-responsive mb-4">
            <table className="table table-bordered table-striped">
              <thead>
                <tr>
                  <th>Room</th>
                  <th>Organizer</th>
                  <th>Start</th>
                  <th>End</th>
                  <th>Purpose</th>
                  <th>Participants</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {bookings.map((booking) => {
                  const participants = booking.participantIds || [];
                  const joined = participants.includes(currentUser.userId);
                  const isOrganizer = booking.userId === currentUser.userId;
                  return (
                    <tr key={booking.id}>
                      <td>{getRoomName(booking.roomId)}</td>
                      <td>{getUserName(booking.userId)}</td>
                      <td>{formatDateTime(booking.start)}</td>
                      <td>{formatDateTime(booking.end)}</td>
                      <td>{booking.purpose}</td>
                      <td>{participants.map((id) => getUserName(id)).join(", ") || "-"}</td>
                      <td>
                        {!isOrganizer && !joined && (
                          <button type="button" className="btn btn-sm btn-outline-primary me-1" onClick={() => handleJoinMeeting(booking.id)}>
                            Join
                          </button>
                        )}
                        {!isOrganizer && joined && (
                          <button type="button" className="btn btn-sm btn-outline-secondary me-1" onClick={() => handleLeaveMeeting(booking.id)}>
                            Leave
                          </button>
                        )}
                        {isAdmin && (
                          <button type="button" className="btn btn-sm btn-outline-danger" onClick={() => handleCancelBooking(booking.id)}>
                            Cancel
                          </button>
                        )}
                      </td>
                    </tr>
                  );
                })}
                {bookings.length === 0 && (
                  <tr><td colSpan="7" className="text-center text-muted">No bookings yet.</td></tr>
                )}
              </tbody>
            </table>
          </div>

          {isAdmin && (
            <>
              <h4>Audit Log</h4>
              <div className="table-responsive">
                <table className="table table-sm table-hover">
                  <thead>
                    <tr>
                      <th>Time</th>
                      <th>Actor</th>
                      <th>Action</th>
                      <th>Entity</th>
                      <th>Details</th>
                    </tr>
                  </thead>
                  <tbody>
                    {auditLogs.map((log) => (
                      <tr key={log.id}>
                        <td>{formatDateTime(log.timestamp)}</td>
                        <td>{log.actorEmail}</td>
                        <td>{log.action}</td>
                        <td>{log.entityType}:{log.entityId}</td>
                        <td>{log.details}</td>
                      </tr>
                    ))}
                    {auditLogs.length === 0 && (
                      <tr><td colSpan="5" className="text-center text-muted">No logs.</td></tr>
                    )}
                  </tbody>
                </table>
              </div>
            </>
          )}
        </div>
      </div>
    </div>
  );
}

export default App;
