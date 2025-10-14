import axios from 'axios';

const API_BASE_URL = "http://localhost:8080/api";

export const getRooms = () => axios.get(`${API_BASE_URL}/rooms`);
export const createUser = (userData) => axios.post(`${API_BASE_URL}/users`, userData);
export const createRoom = (roomData) => axios.post(`${API_BASE_URL}/rooms`, roomData);
export const createBooking = (bookingData) => axios.post(`${API_BASE_URL}/bookings`, bookingData);
export const cancelBooking = (id) => axios.delete(`${API_BASE_URL}/bookings/${id}`);
