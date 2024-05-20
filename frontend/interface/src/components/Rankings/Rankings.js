import React, { useEffect, useState } from 'react';
import { fetchGeneralRankings } from '../../services/api';
import './Rankings.css';

function Rankings() {
    const [rankings, setRankings] = useState([]);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetchGeneralRankings().then(data => {
            if (Array.isArray(data)) {
                setRankings(data);
            } else {
                setError('Unexpected response format');
                console.error('Expected an array but got:', data);
            }
        }).catch(error => {
            setError(error.message);
            console.error('Failed to fetch rankings', error);
        });
    }, []);

    if (error) {
        return <div>Error: {error}</div>;
    }

    return (
        <div className="rankings-container">
            <h1>Ranking Geral</h1>
            <ul>
                {Array.isArray(rankings) && rankings.map(player => (
                    <li key={player.id}>{player.username} - Pontos: {player.rankPoints}</li>
                ))}
            </ul>
        </div>
    );
}

export default Rankings;