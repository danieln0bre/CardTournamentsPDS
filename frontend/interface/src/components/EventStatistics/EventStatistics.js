import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { fetchDeckMatchups, fetchGameObjectById, fetchTeamById, fetchPlayerById } from '../../services/api';
import './EventStatistics.css';

function EventStatistics() {
    const { eventId } = useParams();
    const [statistics, setStatistics] = useState({});
    const [gameObjects, setGameObjects] = useState({});
    const [teamNames, setTeamNames] = useState({});
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetchDeckMatchups(eventId)
            .then(data => {
                setStatistics(data);
                const teamIds = new Set(Object.keys(data));
                return Promise.all(Array.from(teamIds).map(teamId => fetchTeamById(teamId)));
            })
            .then(teams => {
                const playerIds = new Set();
                const teamNameMap = teams.reduce((acc, team) => {
                    acc[team.id] = team.name;
                    team.playerIds.forEach(playerId => {
                        playerIds.add(playerId);
                    });
                    return acc;
                }, {});
                setTeamNames(teamNameMap);
                return Promise.all(Array.from(playerIds).map(playerId => fetchPlayerById(playerId)));
            })
            .then(players => {
                const gameObjectIds = new Set(players.map(player => player.gameObjectId));
                return Promise.all(Array.from(gameObjectIds).map(gameObjectId => fetchGameObjectById(gameObjectId)));
            })
            .then(gameObjects => {
                const gameObjectMap = gameObjects.reduce((acc, gameObject) => {
                    acc[gameObject.id] = gameObject;
                    return acc;
                }, {});
                setGameObjects(gameObjectMap);
                setLoading(false);
            })
            .catch(err => {
                setError(err.message);
                setLoading(false);
            });
    }, [eventId]);

    if (loading) return <p>Loading statistics...</p>;
    if (error) return <p>Error loading statistics: {error}</p>;

    return (
        <div className="event-statistics-container">
            <h1>Game Object Matchup Statistics</h1>
            {Object.keys(statistics).length === 0 ? (
                <p>No statistics available.</p>
            ) : (
                <table>
                    <thead>
                        <tr>
                            <th>Team</th>
                            {Object.keys(gameObjects).map(gameObjectId => (
                                <th key={gameObjectId}>{gameObjects[gameObjectId].deckName || gameObjectId}</th>
                            ))}
                        </tr>
                    </thead>
                    <tbody>
                        {Object.keys(statistics).map(teamId => (
                            <tr key={teamId}>
                                <td>{teamNames[teamId] || teamId}</td>
                                {Object.keys(gameObjects).map(opponentId => (
                                    <td key={`${teamId}-${opponentId}`}>
                                        {statistics[teamId][opponentId] !== undefined
                                            ? statistics[teamId][opponentId].toFixed(2) + '%'
                                            : '-'}
                                    </td>
                                ))}
                            </tr>
                        ))}
                    </tbody>
                </table>
            )}
        </div>
    );
}

export default EventStatistics;
