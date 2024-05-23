import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import { useUser } from '../../../contexts/UserContext';
import './Navbar.css';

function Navbar() {
    const location = useLocation();
    const { user } = useUser();

    console.log('Navbar user:', user); // Debugging log

    if (!user) {
        return null; // Or a loading spinner
    }

    return (
        <nav>
            <div className="navbar">
                <h1>CardTournament</h1>
                <div>
                    {user.role === 'ROLE_PLAYER' && (
                        <>
                            <Link to="/select-deck" className={location.pathname === '/select-deck' ? 'active' : ''}>Selecionar Deck</Link>
                            <Link to="/rankings" className={location.pathname === '/rankings' ? 'active' : ''}>Ver Ranking Geral</Link>
                            <Link to="/my-events" className={location.pathname === '/my-events' ? 'active' : ''}>Meus Eventos</Link>
                            <Link to="/events" className={location.pathname === '/events' ? 'active' : ''}>Ver Eventos</Link>
                            <Link to="/profile" className={location.pathname === '/profile' ? 'active' : ''}>Perfil do Usuário</Link>
                        </>
                    )}
                    {user.role === 'ROLE_MANAGER' && (
                        <>
                            <Link to="/my-events" className={location.pathname === '/my-events' ? 'active' : ''}>Meus Eventos</Link>
                            <Link to="/create-event" className={location.pathname === '/create-event' ? 'active' : ''}>Criar Evento</Link>
                            <Link to="/profile" className={location.pathname === '/profile' ? 'active' : ''}>Perfil do Usuário</Link>
                        </>
                    )}
                </div>
            </div>
        </nav>
    );
}

export default Navbar;
