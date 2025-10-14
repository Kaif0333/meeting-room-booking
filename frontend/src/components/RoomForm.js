import React, { useState } from "react";
import { createRoom } from "../api";

function RoomForm({ onRoomCreated }) {
  const [room, setRoom] = useState({ name: "", location: "", capacity: "", description: "" });

  const handleChange = (e) => setRoom({ ...room, [e.target.name]: e.target.value });

  const handleSubmit = (e) => {
    e.preventDefault();
    createRoom(room)
      .then(() => {
        alert("Room created!");
        onRoomCreated();
        setRoom({ name: "", location: "", capacity: "", description: "" });
      })
      .catch(() => alert("Error creating room"));
  };

  return (
    <form onSubmit={handleSubmit} className="mb-4">
      <h4>Add Room</h4>
      <input name="name" value={room.name} onChange={handleChange} placeholder="Room Name" className="form-control mb-2" />
      <input name="location" value={room.location} onChange={handleChange} placeholder="Location" className="form-control mb-2" />
      <input name="capacity" value={room.capacity} onChange={handleChange} placeholder="Capacity" type="number" className="form-control mb-2" />
      <input name="description" value={room.description} onChange={handleChange} placeholder="Description" className="form-control mb-2" />
      <button type="submit" className="btn btn-primary">Create Room</button>
    </form>
  );
}

export default RoomForm;
