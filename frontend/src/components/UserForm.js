import React, { useState } from "react";
import { createUser } from "../api";

function UserForm() {
  const [user, setUser] = useState({ name: "", email: "" });

  const handleChange = (e) => setUser({ ...user, [e.target.name]: e.target.value });

  const handleSubmit = (e) => {
    e.preventDefault();
    createUser(user)
      .then(() => {
        alert("User created!");
        setUser({ name: "", email: "" });
      })
      .catch(() => alert("Error creating user"));
  };

  return (
    <form onSubmit={handleSubmit} className="mb-4">
      <h4>Add User</h4>
      <input name="name" value={user.name} onChange={handleChange} placeholder="User Name" className="form-control mb-2" />
      <input name="email" value={user.email} onChange={handleChange} placeholder="Email" type="email" className="form-control mb-2" />
      <button type="submit" className="btn btn-success">Create User</button>
    </form>
  );
}

export default UserForm;
