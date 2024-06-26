import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { fetchPlayerEvents, fetchManagerEvents, fetchEventById } from '../../services/api';
import { useUser } from '../../contexts/UserContext';
import { generateUrlFriendlyName } from '../utils/utils';
import './MyEvents.css';

function MyEvents() {
    const { user } = useUser();
    const [events, setEvents] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        if (user && user.id) {
            setLoading(true);
            setError(null);

            const fetchEvents = user.role === 'ROLE_MANAGER' ? fetchManagerEvents : fetchPlayerEvents;
            fetchEvents(user.id)
                .then(data => {
                    if (user.role === 'ROLE_MANAGER') {
                        const eventPromises = data.map(event => fetchEventById(event.id));
                        return Promise.all(eventPromises);
                    } else {
                        return data; // Player events are already fully fetched
                    }
                })
                .then(fetchedEvents => {
                    const uniqueEvents = filterUniqueEvents(fetchedEvents);
                    const sortedEvents = uniqueEvents.sort((a, b) => a.finished - b.finished);
                    setEvents(sortedEvents);
                    setLoading(false);
                })
                .catch(err => {
                    setError(err.message);
                    setLoading(false);
                });
        } else {
            setLoading(false);
        }
    }, [user]);

    const filterUniqueEvents = (events) => {
        const eventMap = new Map();
        events.forEach(event => {
            if (!eventMap.has(event.id)) {
                eventMap.set(event.id, event);
            }
        });
        return Array.from(eventMap.values());
    };

    const handleEventClick = (event) => {
        const eventUrlName = generateUrlFriendlyName(event.name);
        navigate(`/events/${eventUrlName}`);
    };

    if (loading) return <p>Loading events...</p>;
    if (error) return <p>Error loading events: {error.message}</p>;

    return (
        <div className="my-events-container">
            <h1>Meus Eventos</h1>
            <ul className="event-list">
                {events.map((event) => (
                    <li key={event.id} className={`event-item ${event.finished ? 'finished' : ''}`} onClick={() => handleEventClick(event)}>
                        <div className="event-header">
                            <div className="event-name">
                                {event.name}
                                {event.finished && <span className="tag">Finalizado</span>}
                            </div>
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
