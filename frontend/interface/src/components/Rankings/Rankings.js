// src/components/Rankings/Rankings.js
import React, { useEffect, useState } from 'react';
import { fetchGeneralRankings } from '../../services/api';

function Rankings() {
    const [rankings, setRankings] = useState([]);

    useEffect(() => {
        fetchGeneralRankings().then(data => {
            setRankings(data);
        }).catch(error => {
            console.error('Failed to fetch rankings', error);
        });
    }, []);

    return (
        <div>
            <h1>Ranking Geral</h1>
            <ul>
                {rankings.map(player => (
                    <li key={player.id}>{player.username} - Pontos: {player.rankPoints}</li>
                ))}
            </ul>
        </div>
    );
}

export default Rankings;
