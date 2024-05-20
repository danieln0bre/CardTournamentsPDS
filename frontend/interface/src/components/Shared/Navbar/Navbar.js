// src/components/Shared/Navbar/Navbar.js
import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import './Navbar.css';

function Navbar() {
  const location = useLocation();
  const userIsRegisteredForEvent = true; // This should be dynamic based on authentication and specific logic

  return (
    <nav>
      <div className="navbar">
        <h1>CardTournament</h1>
        <div>
          <Link to="/rankings" className={location.pathname === '/rankings' ? 'active' : ''}>Ver Ranking Geral</Link>
          <Link to="/profile" className={location.pathname === '/profile' ? 'active' : ''}>Perfil do Usuário</Link>
          <Link to="/events" className={location.pathname === '/events' ? 'active' : ''}>Ver Eventos</Link>
          <Link to="/my-events" className={location.pathname === '/my-events' ? 'active' : ''}>Meus Eventos</Link>
          {userIsRegisteredForEvent && (
            <>
              <Link to="/event-details" className={location.pathname === '/event-details' ? 'active' : ''}>Ver Evento Específico</Link>
              <Link to="/event-rankings" className={location.pathname === '/event-rankings' ? 'active' : ''}>Ranking do Evento</Link>
              <Link to="/event-pairings" className={location.pathname === '/event-pairings' ? 'active' : ''}>Pairings do Evento</Link>
            </>
          )}
        </div>
      </div>
    </nav>
  );
}

export default Navbar;
