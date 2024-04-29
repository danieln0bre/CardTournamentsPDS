// src/App.js
import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Navbar from './components/Shared/Navbar/Navbar';
import SignUp from './components/SignUp/SignUp';
import Login from './components/Login/Login';
import Rankings from './components/Rankings/Rankings';
import Profile from './components/Perfil/Profile';
import EventsList from './components/Events/EventList';
import MyEvents from './components/Events/MyEvents';

function App() {
  return (
    <BrowserRouter>
      <Navbar />
      <Routes>
        <Route path="/signup" element={<SignUp />} />
        <Route path="/login" element={<Login />} />
        <Route path="/rankings" element={<Rankings />} />
        <Route path="/profile" element={<Profile />} />
        <Route path="/events" element={<EventsList />} />
        <Route path="/my-events" element={<MyEvents />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
