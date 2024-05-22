import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { fetchEventPairings, fetchPlayerById } from '../../services/api';
import './EventPairing.css';

function EventPairing() {
    const { eventId } = useParams();
    const [pairings, setPairings] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [playerNames, setPlayerNames] = useState({});

    useEffect(() => {
        fetchEventPairings(eventId)
            .then(pairingData => {
                setPairings(pairingData);
                const playerIds = pairingData.reduce((ids, pairing) => {
                    if (pairing.playerOneId !== 'Bye') ids.add(pairing.playerOneId);
                    if (pairing.playerTwoId !== 'Bye') ids.add(pairing.playerTwoId);
                    return ids;
                }, new Set());
                return Promise.all([...playerIds].map(playerId => fetchPlayerById(playerId)));
            })
            .then(players => {
                const names = players.reduce((acc, player) => {
                    acc[player.id] = player.name;
                    return acc;
                }, {});
                setPlayerNames(names);
                setLoading(false);
            })
            .catch(err => {
                setError(err);
                setLoading(false);
            });
    }, [eventId]);

    if (loading) return <p>Loading pairings...</p>;
    if (error) return <p>Error loading pairings: {error.message}</p>;

    return (
        <div className="event-pairing-container">
            <h1>Pairings for Event</h1>
            <ul className="pairing-list">
                {pairings.map((pairing, index) => (
                    <li key={index} className="pairing-item">
                        {pairing.playerOneId === 'Bye' ? (
                            'Bye'
                        ) : (
                            <>
                                {playerNames[pairing.playerOneId] || pairing.playerOneId}
                                {' vs '}
                                {pairing.playerTwoId === 'Bye'
                                    ? 'Bye'
                                    : playerNames[pairing.playerTwoId] || pairing.playerTwoId}
                            </>
                        )}
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default EventPairing;
