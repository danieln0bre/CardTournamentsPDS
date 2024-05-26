import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { createEvent } from '../../services/api';
import { useUser } from '../../contexts/UserContext';
import './CreateEvent.css';

function CreateEvent() {
    const { user } = useUser();
    const [eventName, setEventName] = useState('');
    const [eventDate, setEventDate] = useState('');
    const [eventLocation, setEventLocation] = useState('');
    const [numberOfRounds, setNumberOfRounds] = useState(0);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);
    const navigate = useNavigate();

    const handleSubmit = async (event) => {
        event.preventDefault();
        setError(null);
        setSuccess(null);

        try {
            const newEvent = { name: eventName, date: eventDate, location: eventLocation, numberOfRounds };
            await createEvent(user.id, newEvent);
            setSuccess('Event created successfully!');
            navigate('/my-events'); // Redirect to my events page after creation
        } catch (err) {
            setError('Error creating event: ' + err.message);
        }
    };

    return (
        <div className="create-event-container">
            <h1>Create Event</h1>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="eventName">Event Name</label>
                    <input
                        type="text"
                        id="eventName"
                        value={eventName}
                        onChange={(e) => setEventName(e.target.value)}
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="eventDate">Event Date</label>
                    <input
                        type="text"
                        id="eventDate"
                        value={eventDate}
                        onChange={(e) => setEventDate(e.target.value)}
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="eventLocation">Event Location</label>
                    <input
                        type="text"
                        id="eventLocation"
                        value={eventLocation}
                        onChange={(e) => setEventLocation(e.target.value)}
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="numberOfRounds">Number of Rounds</label>
                    <input
                        type="number"
                        id="numberOfRounds"
                        value={numberOfRounds}
                        onChange={(e) => setNumberOfRounds(e.target.value)}
                        min="0"
                    />
                </div>
                <button type="submit">Create Event</button>
            </form>
            {error && <p className="error-message">{error}</p>}
            {success && <p className="success-message">{success}</p>}
        </div>
    );
}

export default CreateEvent;
