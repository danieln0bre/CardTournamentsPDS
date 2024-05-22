import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { fetchDeckMatchups } from '../../services/api';
import './EventStatistics.css';

function EventStatistics() {
    const { eventId } = useParams();
    const [statistics, setStatistics] = useState({});
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetchDeckMatchups(eventId)
            .then(data => {
                setStatistics(data);
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
            <h1>Deck Matchup Statistics</h1>
            {Object.keys(statistics).length === 0 ? (
                <p>No statistics available.</p>
            ) : (
                <table>
                    <thead>
                        <tr>
                            <th>Deck</th>
                            <th>Opponent Deck</th>
                            <th>Win Percentage</th>
                        </tr>
                    </thead>
                    <tbody>
                        {Object.entries(statistics).map(([deck, opponents]) => (
                            Object.entries(opponents).map(([opponent, winPercentage]) => (
                                <tr key={`${deck}-${opponent}`}>
                                    <td>{deck}</td>
                                    <td>{opponent}</td>
                                    <td>{winPercentage.toFixed(2)}%</td>
                                </tr>
                            ))
                        ))}
                    </tbody>
                </table>
            )}
        </div>
    );
}

export default EventStatistics;
