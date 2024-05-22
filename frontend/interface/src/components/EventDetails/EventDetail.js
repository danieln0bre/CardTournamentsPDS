import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { fetchEventByName, fetchPlayerById, addEventToPlayer } from '../../services/api';
import { useUser } from '../../contexts/UserContext';
import './EventDetail.css';

function EventDetail() {
    const { eventName } = useParams();
    const navigate = useNavigate();
    const { user } = useUser();
    const [event, setEvent] = useState(null);
    const [players, setPlayers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [registrationError, setRegistrationError] = useState(null);

    useEffect(() => {
        fetchEventByName(eventName)
            .then(eventData => {
                setEvent(eventData);
                return Promise.all(eventData.playerIds.map(playerId => fetchPlayerById(playerId)));
            })
            .then(playerDataArray => {
                setPlayers(playerDataArray);
                setLoading(false);
            })
            .catch(err => {
                setError(err);
                setLoading(false);
            });
    }, [eventName]);

    const handleRegistration = () => {
        if (user && event) {
            addEventToPlayer(user.id, event.id)
                .then(() => {
                    setEvent(prevEvent => ({
                        ...prevEvent,
                        playerIds: [...prevEvent.playerIds, user.id]
                    }));
                    setPlayers(prevPlayers => [...prevPlayers, user]);
                    setRegistrationError(null);
                })
                .catch(err => {
                    setRegistrationError(err.message);
                });
        }
    };

    if (loading) return <p>Loading event...</p>;
    if (error) return <p>Error loading event: {error.message}</p>;
    if (!event) return <p>No event found</p>;

    return (
        <div className="event-detail-container">
            <h1>{event.name}</h1>
            <div><strong>Date:</strong> {event.date || 'Data não definida'}</div>
            <div><strong>Location:</strong> {event.location}</div>
            <div><strong>Rounds:</strong> {event.currentRound}/{event.numberOfRounds}</div>
            <div><strong>Finished:</strong> {event.finished ? 'Sim' : 'Não'}</div>
            <div><strong>Started:</strong> {event.hasStarted ? 'Sim' : 'Não'}</div>

            <h2 className="player-list-title">Players</h2>
            <ul>
                {players.map(player => (
                    <li key={player.id}>{player.name}</li>
                ))}
            </ul>

            <div className="button-group">
                {event.playerIds.includes(user.id) ? (
                    <div>
                        <button onClick={() => navigate(`/events/${event.id}/ranking`)}>Ranking</button>
                        <button onClick={() => navigate(`/events/${event.id}/pairing`)}>Pareamento</button>
                        <button onClick={() => navigate(`/events/${event.id}/statistics`)}>Statistics</button>
                    </div>
                ) : (
                    <button onClick={handleRegistration}>Se Inscrever</button>
                )}
                {registrationError && <p className="error-message">{registrationError}</p>}
            </div>
        </div>
    );
}

export default EventDetail;
