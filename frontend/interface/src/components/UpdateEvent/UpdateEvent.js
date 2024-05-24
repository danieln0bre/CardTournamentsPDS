import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { fetchEventById, updateEvent } from '../../services/api';
import './UpdateEvent.css';

function UpdateEvent() {
    const { eventId } = useParams();
    const [eventDetails, setEventDetails] = useState(null);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        fetchEventById(eventId)
            .then(data => setEventDetails(data))
            .catch(err => setError('Error fetching event details: ' + err.message));
    }, [eventId]);

    const handleSubmit = async (event) => {
        event.preventDefault();
        setError(null);
        setSuccess(null);

        if (eventDetails.numberOfRounds < 0) {
            setError('Number of rounds cannot be negative.');
            return;
        }

        try {
            await updateEvent(eventId, eventDetails);
            setSuccess('Event updated successfully!');
            navigate('/my-events'); // Redirect to my events page after update
        } catch (err) {
            setError('Error updating event: ' + err.message);
        }
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setEventDetails(prevDetails => ({
            ...prevDetails,
            [name]: value
        }));
    };

    if (!eventDetails) return <p>Loading event details...</p>;
    if (error) return <p className="error-message">{error}</p>;

    return (
        <div className="update-event-container">
            <h1>Update Event</h1>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="eventName">Event Name</label>
                    <input
                        type="text"
                        id="eventName"
                        name="name"
                        value={eventDetails.name}
                        onChange={handleChange}
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="eventDate">Event Date</label>
                    <input
                        type="date"
                        id="eventDate"
                        name="date"
                        value={eventDetails.date}
                        onChange={handleChange}
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="eventLocation">Event Location</label>
                    <input
                        type="text"
                        id="eventLocation"
                        name="location"
                        value={eventDetails.location}
                        onChange={handleChange}
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="numberOfRounds">Number of Rounds</label>
                    <input
                        type="number"
                        id="numberOfRounds"
                        name="numberOfRounds"
                        value={eventDetails.numberOfRounds}
                        min="0"
                        onChange={handleChange}
                    />
                </div>
                <button type="submit">Update Event</button>
            </form>
            {success && <p className="success-message">{success}</p>}
            {error && <p className="error-message">{error}</p>}
        </div>
    );
}

export default UpdateEvent;
