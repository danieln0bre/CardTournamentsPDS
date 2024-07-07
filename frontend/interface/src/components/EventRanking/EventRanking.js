import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { fetchEventResultRanking, fetchEventRankings, fetchEventById, fetchTeamById } from '../../services/api';
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
                const teamIds = data.map(result => result.teamId);
                const teams = await Promise.all(teamIds.map(teamId => fetchTeamById(teamId)));
                const teamMap = teams.reduce((acc, team) => {
                    acc[team.id] = team.name;
                    return acc;
                }, {});
                const rankingWithTeamNames = data.map(result => ({
                    ...result,
                    teamName: teamMap[result.teamId] || 'Unknown'
                }));
                setRanking(rankingWithTeamNames);
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
                        <th>Team</th>
                        <th>Event Points</th>
                        <th>Winrate</th>
                    </tr>
                </thead>
                <tbody>
                    {ranking.map((teamResult, index) => (
                        <tr key={teamResult.teamId}>
                            <td>{index + 1}</td>
                            <td>{teamResult.teamName}</td>
                            <td>{teamResult.eventPoints}</td>
                            <td>{teamResult.winrate}%</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}

export default EventRanking;
