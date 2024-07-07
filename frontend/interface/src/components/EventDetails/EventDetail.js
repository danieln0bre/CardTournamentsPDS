import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { fetchEventByName, fetchPlayerTeam, fetchTeamById, fetchPlayerById, addEventToEntity, startEvent, finalizeEvent, fetchEventRankings, fetchEventPairings, fetchDeckMatchups } from '../../services/api';
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
        fetchEventByName(eventName)
            .then(async eventData => {
                setEvent(eventData);

                if (eventData.isTeamEvent) {
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

                    setTeams(teamsWithPlayers.filter(team => team !== null));
                } else {
                    const playerDataArray = await Promise.all(
                        eventData.entityIds.map(entityId => fetchPlayerById(entityId).catch(() => null))
                    );
                    setPlayers(playerDataArray.filter(player => player !== null));
                }

                setLoading(false);
            })
            .catch(err => {
                setError(err.message);
                setLoading(false);
            });

        fetchPlayerTeam(user.id)
            .then(teamId => setTeamId(teamId))
            .catch(err => console.error('Failed to fetch team:', err));
    }, [eventName, user.id]);

    const handleRegistration = () => {
        if (user && event && teamId) {
            addEventToEntity(teamId, event.id)
                .then(() => {
                    setEvent(prevEvent => ({
                        ...prevEvent,
                        entityIds: [...prevEvent.entityIds, teamId]
                    }));
                    setRegistrationError(null);
                })
                .catch(err => {
                    setRegistrationError(err.message);
                });
        } else if (user && event) {
            addEventToEntity(user.id, event.id)
                .then(() => {
                    setEvent(prevEvent => ({
                        ...prevEvent,
                        entityIds: [...prevEvent.entityIds, user.id]
                    }));
                    setPlayers(prevPlayers => [...prevPlayers, user]);
                    setRegistrationError(null);
                })
                .catch(err => {
                    setRegistrationError(err.message);
                });
        }
    };

    const handleStartEvent = () => {
        if (user && event) {
            startEvent(event.id)
                .then(() => {
                    setEvent(prevEvent => ({
                        ...prevEvent,
                        hasStarted: true
                    }));
                    setStartError(null);
                    navigate(`/events/${event.id}/pairing`); // Redirect to Pairing page after starting event
                })
                .catch(err => {
                    setStartError(err.message);
                });
        }
    };

    const handleFinalizeEvent = () => {
        if (user && event) {
            finalizeEvent(event.id)
                .then(() => {
                    setEvent(prevEvent => ({
                        ...prevEvent,
                        finished: true
                    }));
                    setFinalizeError(null);
                    navigate(`/events/${event.id}/ranking`); // Redirect to Ranking page after finalizing event
                })
                .catch(err => {
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

    return (
        <div className="event-detail-container">
            <h1>{event.name}</h1>
            <div><strong>Date:</strong> {event.date || 'Data não definida'}</div>
            <div><strong>Location:</strong> {event.location}</div>
            <div><strong>Rounds:</strong> {event.currentRound}/{event.numberOfRounds}</div>
            <div><strong>Finished:</strong> {event.finished ? 'Sim' : 'Não'}</div>
            <div><strong>Started:</strong> {event.hasStarted ? 'Sim' : 'Não'}</div>
            <div><strong>Current Round:</strong> {event.currentRound}</div>

            <h2 className="entity-list-title">{event.isTeamEvent ? 'Teams' : 'Players'}</h2>
            <ul>
                {event.isTeamEvent ? (
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
                {user.role === 'ROLE_PLAYER' && !event.entityIds.includes(user.id) && !event.entityIds.includes(teamId) && !event.hasStarted && (
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
