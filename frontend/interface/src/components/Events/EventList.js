// src/components/Events/EventsList.js
import React, { useEffect, useState } from 'react';
import { fetchEvents } from '../../services/api';

function EventsList() {
    const [events, setEvents] = useState([]);

    useEffect(() => {
        fetchEvents().then(data => setEvents(data));
    }, []);

    return (
        <div>
            <h1>Eventos Disponíveis</h1>
            <ul>
                {events.map(event => (
                    <li key={event.id}>{event.name}</li>
                ))}
            </ul>
        </div>
    );
}

export default EventsList;
