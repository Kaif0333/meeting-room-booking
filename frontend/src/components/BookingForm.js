import React, { useState } from "react";
import { createBooking } from "../api";

function BookingForm() {
  const [booking, setBooking] = useState({
    roomId: "",
    userId: "",
    start: "",
    end: "",
    purpose: ""
  });

  const handleChange = (e) => setBooking({ ...booking, [e.target.name]: e.target.value });

  const handleSubmit = (e) => {
    e.preventDefault();
    createBooking(booking)
      .then(() => {
        alert("Booking created!");
        setBooking({ roomId: "", userId: "", start: "", end: "", purpose: "" });
      })
      .catch(() => alert("Error creating booking"));
  };

  return (
    <form onSubmit={handleSubmit} className="mb-4">
      <h4>Create Booking</h4>
      <input name="roomId" value={booking.roomId} onChange={handleChange} placeholder="Room ID" className="form-control mb-2" />
      <input name="userId" value={booking.userId} onChange={handleChange} placeholder="User ID" className="form-control mb-2" />
      <input name="start" value={booking.start} onChange={handleChange} placeholder="Start (YYYY-MM-DDTHH:MM:SS)" className="form-control mb-2" />
      <input name="end" value={booking.end} onChange={handleChange} placeholder="End (YYYY-MM-DDTHH:MM:SS)" className="form-control mb-2" />
      <input name="purpose" value={booking.purpose} onChange={handleChange} placeholder="Purpose" className="form-control mb-2" />
      <button type="submit" className="btn btn-warning">Book Room</button>
    </form>
  );
}

export default BookingForm;
