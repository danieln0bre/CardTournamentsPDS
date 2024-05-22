import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Navbar from './components/Shared/Navbar/Navbar';
import SignUp from './components/SignUp/SignUp';
import Login from './components/Login/Login';
import Rankings from './components/Rankings/Rankings';
import Profile from './components/Perfil/Profile';
import EventsList from './components/EventList/EventList';
import MyEvents from './components/MyEvents/MyEvents';
import EventDetail from './components/EventDetails/EventDetail';
import EventRanking from './components/EventRanking/EventRanking';
import EventPairing from './components/EventPairing/EventPairing';

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
        <Route path="/events/:eventName" element={<EventDetail />} />
        <Route path="/events/:eventId/ranking" element={<EventRanking />} />
        <Route path="/events/:eventId/pairing" element={<EventPairing />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
