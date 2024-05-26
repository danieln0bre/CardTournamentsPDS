// EventStatistics.js

import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { fetchDeckMatchups, fetchDeckById } from '../../services/api';
import './EventStatistics.css';

function EventStatistics() {
    const { eventId } = useParams();
    const [statistics, setStatistics] = useState({});
    const [deckNames, setDeckNames] = useState({});
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetchDeckMatchups(eventId)
            .then(data => {
                setStatistics(data);
                const deckIds = new Set();
                Object.keys(data).forEach(deckId => {
                    deckIds.add(deckId);
                    Object.keys(data[deckId]).forEach(opponentId => {
                        deckIds.add(opponentId);
                    });
                });
                return Promise.all(Array.from(deckIds).map(deckId => fetchDeckById(deckId)));
            })
            .then(decks => {
                const deckNameMap = decks.reduce((acc, deck) => {
                    acc[deck.id] = deck.deckName;
                    return acc;
                }, {});
                setDeckNames(deckNameMap);
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
                            {Object.keys(deckNames).map(deckId => (
                                <th key={deckId}>{deckNames[deckId] || deckId}</th>
                            ))}
                        </tr>
                    </thead>
                    <tbody>
                        {Object.keys(statistics).map(deckId => (
                            <tr key={deckId}>
                                <td>{deckNames[deckId] || deckId}</td>
                                {Object.keys(deckNames).map(opponentId => (
                                    <td key={`${deckId}-${opponentId}`}>
                                        {statistics[deckId][opponentId] !== undefined
                                            ? statistics[deckId][opponentId].toFixed(2) + '%'
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
