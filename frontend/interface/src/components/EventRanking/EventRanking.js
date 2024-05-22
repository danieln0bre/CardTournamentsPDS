import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { fetchEventRankings } from '../../services/api';
import './EventRanking.css';

function EventRanking() {
    const { eventId } = useParams();
    const [rankings, setRankings] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetchEventRankings(eventId)
            .then(data => {
                setRankings(data);
                setLoading(false);
            })
            .catch(err => {
                setError(err);
                setLoading(false);
            });
    }, [eventId]);

    if (loading) return <p>Loading rankings...</p>;
    if (error) return <p>Error loading rankings: {error.message}</p>;

    return (
        <div className="event-ranking-container">
            <h1>Ranking for Event</h1>
            <ul className="ranking-list">
                {rankings.map((player, index) => (
                    <li key={player.id} className="ranking-item">
                        #{index + 1} {player.name} - Points: {player.rankPoints}
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default EventRanking;
