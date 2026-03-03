import React, { useState } from "react";
import { createUser } from "../api";

function UserForm({ onUserCreated, onError }) {
  const [user, setUser] = useState({ name: "", email: "", passwordHash: "", role: "USER" });
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleChange = (e) => setUser({ ...user, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsSubmitting(true);
    try {
      await createUser(user);
      setUser({ name: "", email: "", passwordHash: "", role: "USER" });
      onUserCreated();
    } catch (error) {
      onError(error.message || "Error creating user");
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="mb-4">
      <h4>Add User</h4>
      <input name="name" value={user.name} onChange={handleChange} placeholder="User Name" required className="form-control mb-2" />
      <input name="email" value={user.email} onChange={handleChange} placeholder="Email" type="email" required className="form-control mb-2" />
      <input name="passwordHash" value={user.passwordHash} onChange={handleChange} placeholder="Temporary Password" type="password" required className="form-control mb-2" />
      <select name="role" value={user.role} onChange={handleChange} className="form-select mb-2">
        <option value="USER">USER</option>
        <option value="ADMIN">ADMIN</option>
      </select>
      <button type="submit" className="btn btn-success" disabled={isSubmitting}>
        {isSubmitting ? "Creating..." : "Create User"}
      </button>
    </form>
  );
}

export default UserForm;
