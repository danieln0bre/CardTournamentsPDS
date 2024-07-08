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
        const fetchRanking = async () => {
            try {
                const event = await fetchEventById(eventId);
                console.log('Fetched Event:', event);
                let rankingData;

                if (event.finished) {
                    rankingData = await fetchEventResultRanking(eventId);
                } else {
                    rankingData = await fetchEventRankings(eventId);
                }

                console.log('Fetched Ranking Data:', rankingData);

                const teamIds = rankingData.map(result => result.teamId).filter(Boolean);
                console.log('Team IDs:', teamIds);

                const teams = await Promise.all(teamIds.map(teamId => fetchTeamById(teamId)));
                console.log('Fetched Teams:', teams);

                const teamMap = teams.reduce((acc, team) => {
                    acc[team.id] = team;
                    return acc;
                }, {});
                console.log('Team Map:', teamMap);

                const rankingWithTeamNames = rankingData.map(result => ({
                    ...result,
                    teamName: teamMap[result.teamId]?.name || 'Unknown'
                }));
                console.log('Ranking with Team Names:', rankingWithTeamNames);

                setRanking(rankingWithTeamNames);
                setLoading(false);
            } catch (err) {
                setError(err.message);
                setLoading(false);
            }
        };

        fetchRanking();
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
