import React, { useEffect, useState } from "react";
import { getRooms } from "./api";
import RoomForm from "./components/RoomForm";
import UserForm from "./components/UserForm";
import BookingForm from "./components/BookingForm";

function App() {
  const [rooms, setRooms] = useState([]);

  const loadRooms = () => {
    getRooms().then(res => setRooms(res.data)).catch(err => console.error(err));
  };

  useEffect(() => {
    loadRooms();
  }, []);

  return (
    <div className="container mt-4">
      <h1 className="mb-4">🏢 Meeting Room Booking System</h1>

      <div className="row">
        <div className="col-md-6">
          <RoomForm onRoomCreated={loadRooms} />
          <UserForm />
        </div>

        <div className="col-md-6">
          <BookingForm />
          <h4>Available Rooms:</h4>
          <ul className="list-group">
            {rooms.map((room) => (
              <li key={room.id} className="list-group-item">
                <strong>{room.name}</strong> — {room.location} — Capacity: {room.capacity}
              </li>
            ))}
          </ul>
        </div>
      </div>
    </div>
  );
}

export default App;
