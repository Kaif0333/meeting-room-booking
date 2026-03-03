import React, { useState } from "react";
import { createBooking } from "../api";

function BookingForm({ rooms, currentUser, onBookingCreated, onError }) {
  const [booking, setBooking] = useState({
    roomId: "",
    start: "",
    end: "",
    purpose: ""
  });
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleChange = (e) => setBooking({ ...booking, [e.target.name]: e.target.value });
  const toIsoDateTime = (value) => {
    if (!value) {
      return "";
    }
    return value.length === 16 ? `${value}:00` : value;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const payload = {
      ...booking,
      userId: currentUser.userId,
      start: toIsoDateTime(booking.start),
      end: toIsoDateTime(booking.end),
    };

    if (!payload.start || !payload.end || payload.start >= payload.end) {
      onError("Please enter a valid start and end time.");
      return;
    }

    setIsSubmitting(true);
    try {
      await createBooking(payload);
      setBooking({ roomId: "", start: "", end: "", purpose: "" });
      onBookingCreated();
    } catch (error) {
      onError(error.message || "Error creating booking");
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="mb-4">
      <h4>Create Booking</h4>
      <select name="roomId" value={booking.roomId} onChange={handleChange} required className="form-select mb-2">
        <option value="">Select Room</option>
        {rooms.map((room) => (
          <option key={room.id} value={room.id}>
            {room.name} ({room.location}, cap {room.capacity})
          </option>
        ))}
      </select>
      <input name="start" value={booking.start} onChange={handleChange} type="datetime-local" required className="form-control mb-2" />
      <input name="end" value={booking.end} onChange={handleChange} type="datetime-local" required className="form-control mb-2" />
      <input name="purpose" value={booking.purpose} onChange={handleChange} placeholder="Purpose" required className="form-control mb-2" />
      <button type="submit" className="btn btn-warning" disabled={isSubmitting || rooms.length === 0}>
        {isSubmitting ? "Booking..." : "Book Room"}
      </button>
    </form>
  );
}

export default BookingForm;
