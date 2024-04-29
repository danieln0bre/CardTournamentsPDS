// src/components/Shared/Navbar/Navbar.js
import React from 'react';
import { Link } from 'react-router-dom';

function Navbar() {
  // Aqui você pode verificar se o usuário está inscrito em um evento específico
  const userIsRegisteredForEvent = true; // Isso seria dinâmico com base na autenticação e lógica específica

  return (
    <nav>
      <div className="navbar">
        <h1>CardTournament</h1>
        <Link to="/rankings">Ver Ranking Geral</Link>
        <Link to="/profile">Perfil do Usuário</Link>
        <Link to="/events">Ver Eventos</Link>
        <Link to="/my-events">Meus Eventos</Link>
        {userIsRegisteredForEvent && (
          <>
            <Link to="/event-details">Ver Evento Específico</Link>
            <Link to="/event-rankings">Ranking do Evento</Link>
            <Link to="/event-pairings">Pairings do Evento</Link>
          </>
        )}
      </div>
    </nav>
  );
}

export default Navbar;
