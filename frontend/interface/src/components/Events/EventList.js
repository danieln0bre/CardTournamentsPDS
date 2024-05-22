import React, { useEffect, useState } from 'react';
import { fetchEvents } from '../../services/api';
import { useNavigate } from 'react-router-dom';
import './EventList.css';
import { generateUrlFriendlyName } from '../utils/utils';

function EventList() {
    const [events, setEvents] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        fetchEvents()
            .then(data => {
                setEvents(data);
                setLoading(false);
            })
            .catch(err => {
                setError(err);
                setLoading(false);
            });
    }, []);

    if (loading) return <p>Loading events...</p>;
    if (error) return <p>Error loading events: {error.message}</p>;

    const handleEventClick = (event) => {
        const eventUrlName = generateUrlFriendlyName(event.name);
        navigate(`/events/${eventUrlName}`);
    };

    return (
        <div className="event-list-container">
            <h1>Events</h1>
            <ul className="event-list">
                {events.map((event) => (
                    <li key={event.id} className="event-item" onClick={() => handleEventClick(event)}>
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

export default EventList;
