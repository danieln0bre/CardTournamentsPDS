// src/components/MyEvents/MyEvents.js
import React, { useEffect, useState } from 'react';
import { fetchPlayerEvents } from '../../services/api';
import { useUser } from '../../contexts/UserContext';
import './MyEvents.css';

function MyEvents() {
    const { user } = useUser();
    const [events, setEvents] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        if (user && user.id) {
            fetchPlayerEvents(user.id)
                .then(data => {
                    setEvents(data);
                    setLoading(false);
                })
                .catch(err => {
                    setError(err);
                    setLoading(false);
                });
        }
    }, [user]);

    if (loading) return <p>Loading events...</p>;
    if (error) return <p>Error loading events: {error.message}</p>;

    return (
        <div className="my-events-container">
            <h1>Meus Eventos</h1>
            <ul className="event-list">
                {events.map((event) => (
                    <li key={event.id} className="event-item">
                        <div className="event-header">
                            <div className="event-name">{event.name}</div>
                            <div className="event-date">{event.date || 'Data não definida'}</div>
                        </div>
                        <div className="event-details">
                            <div><strong>Local:</strong> {event.location}</div>
                            <div><strong>Rodadas:</strong> {event.currentRound}/{event.numberOfRounds}</div>
                            <div><strong>Finalizado:</strong> {event.finished ? 'Sim' : 'Não'}</div>
                            <div><strong>Iniciado:</strong> {event.hasStarted ? 'Sim' : 'Não'}</div>
                        </div>
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default MyEvents;
