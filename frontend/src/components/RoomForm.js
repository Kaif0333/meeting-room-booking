import React, { useState } from "react";
import { createRoom } from "../api";

function RoomForm({ onRoomCreated, onError }) {
  const [room, setRoom] = useState({ name: "", location: "", capacity: "", description: "" });
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleChange = (e) => setRoom({ ...room, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsSubmitting(true);
    try {
      await createRoom({ ...room, capacity: Number(room.capacity) });
      setRoom({ name: "", location: "", capacity: "", description: "" });
      onRoomCreated();
    } catch (error) {
      onError(error.message || "Error creating room");
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="mb-4">
      <h4>Add Room</h4>
      <input name="name" value={room.name} onChange={handleChange} placeholder="Room Name" required className="form-control mb-2" />
      <input name="location" value={room.location} onChange={handleChange} placeholder="Location" required className="form-control mb-2" />
      <input name="capacity" value={room.capacity} onChange={handleChange} placeholder="Capacity" type="number" min="1" required className="form-control mb-2" />
      <input name="description" value={room.description} onChange={handleChange} placeholder="Description" className="form-control mb-2" />
      <button type="submit" className="btn btn-primary" disabled={isSubmitting}>
        {isSubmitting ? "Creating..." : "Create Room"}
      </button>
    </form>
  );
}

export default RoomForm;
