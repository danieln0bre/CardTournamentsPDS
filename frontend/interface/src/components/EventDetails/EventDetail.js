import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { fetchEventByName, fetchTeamById, fetchPlayerById, addEventToEntity, startEvent, finalizeEvent } from '../../services/api';
import { useUser } from '../../contexts/UserContext';
import './EventDetail.css';

function EventDetail() {
    const { eventName } = useParams();
    const navigate = useNavigate();
    const { user } = useUser();
    const [event, setEvent] = useState(null);
    const [players, setPlayers] = useState([]);
    const [teams, setTeams] = useState([]);
    const [teamId, setTeamId] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [registrationError, setRegistrationError] = useState(null);
    const [startError, setStartError] = useState(null);
    const [finalizeError, setFinalizeError] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                console.log('Fetching event by name:', eventName);
                const eventData = await fetchEventByName(eventName);
                console.log('Event data:', eventData);
                setEvent(eventData);

                if (eventData.teamEvent) {
                    const teamDataArray = await Promise.all(
                        eventData.entityIds.map(entityId => fetchTeamById(entityId).catch(() => null))
                    );

                    const teamsWithPlayers = await Promise.all(teamDataArray.map(async team => {
                        if (team) {
                            const playerDetails = await Promise.all(team.playerIds.map(playerId => fetchPlayerById(playerId)));
                            return { ...team, players: playerDetails };
                        }
                        return null;
                    }));

                    console.log('Teams with players:', teamsWithPlayers);
                    setTeams(teamsWithPlayers.filter(team => team !== null));
                } else {
                    const playerDataArray = await Promise.all(
                        eventData.entityIds.map(entityId => fetchPlayerById(entityId).catch(() => null))
                    );
                    console.log('Player data array:', playerDataArray);
                    setPlayers(playerDataArray.filter(player => player !== null));
                }

                console.log('Fetching player by ID:', user.id);
                const playerData = await fetchPlayerById(user.id);
                console.log('Player data:', playerData);
                if (playerData && playerData.teamId) {
                    setTeamId(playerData.teamId);
                    console.log('Team ID set to:', playerData.teamId);
                } else {
                    console.log('No team ID found for player');
                }

                setLoading(false);
            } catch (err) {
                console.error('Error fetching data:', err);
                setError(err.message);
                setLoading(false);
            }
        };

        fetchData();
    }, [eventName, user.id]);

    const handleRegistration = () => {
        console.log('Handling registration:', { user, event, teamId });
        if (user && event && teamId) {
            console.log('Adding event to entity:', { teamId, eventId: event.id });
            addEventToEntity(teamId, event.id)
                .then(() => {
                    setEvent(prevEvent => ({
                        ...prevEvent,
                        entityIds: [...prevEvent.entityIds, teamId]
                    }));
                    setRegistrationError(null);
                    console.log('Registration successful');
                })
                .catch(err => {
                    console.error('Error registering for event:', err);
                    setRegistrationError(err.message);
                });
        } else {
            console.error('Cannot register for the event:', { user, event, teamId });
            setRegistrationError("Cannot register for the event.");
        }
    };

    const handleStartEvent = () => {
        console.log('Starting event:', event.id);
        if (user && event) {
            startEvent(event.id)
                .then(() => {
                    setEvent(prevEvent => ({
                        ...prevEvent,
                        hasStarted: true
                    }));
                    setStartError(null);
                    navigate(`/events/${event.id}/pairing`); // Redirect to Pairing page after starting event
                    console.log('Event started successfully');
                })
                .catch(err => {
                    console.error('Error starting event:', err);
                    setStartError(err.message);
                });
        }
    };

    const handleFinalizeEvent = () => {
        console.log('Finalizing event:', event.id);
        if (user && event) {
            finalizeEvent(event.id)
                .then(() => {
                    setEvent(prevEvent => ({
                        ...prevEvent,
                        finished: true
                    }));
                    setFinalizeError(null);
                    navigate(`/events/${event.id}/ranking`); // Redirect to Ranking page after finalizing event
                    console.log('Event finalized successfully');
                })
                .catch(err => {
                    console.error('Error finalizing event:', err);
                    setFinalizeError(err.message);
                });
        }
    };

    const handleViewRankings = () => {
        if (event) {
            navigate(`/events/${event.id}/ranking`);
        }
    };

    const handleViewPairings = () => {
        if (event) {
            navigate(`/events/${event.id}/pairing`);
        }
    };

    const handleViewStatistics = () => {
        if (event) {
            navigate(`/events/${event.id}/statistics`);
        }
    };

    if (loading) return <p>Loading event...</p>;
    if (error) return <p>Error loading event: {error}</p>;
    if (!event) return <p>No event found</p>;

    const isRegistered = event.entityIds.includes(user.id) || (teamId && event.entityIds.includes(teamId));

    return (
        <div className="event-detail-container">
            <h1>{event.name}</h1>
            <div><strong>Date:</strong> {event.date || 'Data não definida'}</div>
            <div><strong>Location:</strong> {event.location}</div>
            <div><strong>Rounds:</strong> {event.currentRound}/{event.numberOfRounds}</div>
            <div><strong>Finished:</strong> {event.finished ? 'Sim' : 'Não'}</div>
            <div><strong>Started:</strong> {event.hasStarted ? 'Sim' : 'Não'}</div>
            <div><strong>Current Round:</strong> {event.currentRound}</div>

            <h2 className="entity-list-title">{event.teamEvent ? 'Teams' : 'Players'}</h2>
            <ul>
                {event.teamEvent ? (
                    teams.map(team => (
                        <li key={team.id}>
                            <strong>{team.name}</strong>
                            <ul>
                                {team.players.map(player => (
                                    <li key={player.id}>{player.username}</li>
                                ))}
                            </ul>
                        </li>
                    ))
                ) : (
                    players.map(player => (
                        <li key={player.id}>{player.username}</li>
                    ))
                )}
            </ul>

            <div className="button-group">
                {(user.role === 'ROLE_MANAGER' || user.role === 'ROLE_PLAYER') && (
                    <div>
                        <button onClick={handleViewRankings}>Ranking</button>
                        <button onClick={handleViewPairings}>Pareamento</button>
                        {event.finished && <button onClick={handleViewStatistics}>Statistics</button>}
                    </div>
                )}
                {user.role === 'ROLE_MANAGER' && (
                    <div>
                        <button onClick={() => navigate(`/update-event/${event.id}`)}>Update Event</button>
                        {!event.hasStarted && <button onClick={handleStartEvent}>Start Event</button>}
                        {event.currentRound >= event.numberOfRounds && !event.finished && (
                            <button onClick={handleFinalizeEvent}>Finalize Event</button>
                        )}
                    </div>
                )}
                {user.role === 'ROLE_PLAYER' && !isRegistered && !event.hasStarted && (
                    <button onClick={handleRegistration}>Se Inscrever</button>
                )}
                {registrationError && <p className="error-message">{registrationError}</p>}
                {startError && <p className="error-message">{startError}</p>}
                {finalizeError && <p className="error-message">{finalizeError}</p>}
            </div>
        </div>
    );
}

export default EventDetail;
