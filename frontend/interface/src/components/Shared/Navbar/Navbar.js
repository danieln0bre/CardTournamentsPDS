// src/components/Shared/Navbar/Navbar.js
import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import './Navbar.css';

function Navbar() {
  const location = useLocation();

  return (
    <nav>
      <div className="navbar">
        <h1>CardTournament</h1>
        <div>
          <Link to="/rankings" className={location.pathname === '/rankings' ? 'active' : ''}>Ver Ranking Geral</Link>
          <Link to="/profile" className={location.pathname === '/profile' ? 'active' : ''}>Perfil do Usuário</Link>
          <Link to="/events" className={location.pathname === '/events' ? 'active' : ''}>Ver Eventos</Link>
          <Link to="/my-events" className={location.pathname === '/my-events' ? 'active' : ''}>Meus Eventos</Link>
          <Link to="/select-deck" className={location.pathname === '/select-deck' ? 'active' : ''}>Selecionar Deck</Link>
        </div>
      </div>
    </nav>
  );
}

export default Navbar;
