import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { fetchEventResultRanking, fetchEventRankings, fetchEventById, fetchPlayerById } from '../../services/api';
import './EventRanking.css';

function EventRanking() {
    const { eventId } = useParams();
    const [ranking, setRanking] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetchEventById(eventId)
            .then(event => {
                if (event.finished) {
                    return fetchEventResultRanking(eventId);
                } else {
                    return fetchEventRankings(eventId);
                }
            })
            .then(async data => {
                const playerIds = data.map(result => result.playerId);
                const players = await Promise.all(playerIds.map(playerId => fetchPlayerById(playerId)));
                const playerMap = players.reduce((acc, player) => {
                    acc[player.id] = player.username;
                    return acc;
                }, {});
                const rankingWithUsernames = data.map(result => ({
                    ...result,
                    username: playerMap[result.playerId] || 'Unknown'
                }));
                setRanking(rankingWithUsernames);
                setLoading(false);
            })
            .catch(err => {
                setError(err.message);
                setLoading(false);
            });
    }, [eventId]);

    if (loading) return <p>Loading ranking...</p>;
    if (error) return <p>Error loading ranking: {error}</p>;

    return (
        <div className="event-ranking-container">
            <h1>Event Ranking</h1>
            <table>
                <thead>
                    <tr>
                        <th>Position</th>
                        <th>Player</th>
                        <th>Event Points</th>
                        <th>Winrate</th>
                    </tr>
                </thead>
                <tbody>
                    {ranking.map((playerResult, index) => (
                        <tr key={playerResult.playerId}>
                            <td>{index + 1}</td>
                            <td>{playerResult.username}</td>
                            <td>{playerResult.eventPoints}</td>
                            <td>{playerResult.winrate}%</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}

export default EventRanking;
