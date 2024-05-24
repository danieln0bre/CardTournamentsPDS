import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { fetchEventById, fetchEventPairings, generatePairings, fetchPlayerById, savePairings, recalculateWinrates, finalizeRound, updateEvent } from '../../services/api';
import { useUser } from '../../contexts/UserContext';
import './EventPairing.css';

function EventPairing() {
    const { eventId } = useParams();
    const navigate = useNavigate();
    const { user } = useUser();
    const [pairings, setPairings] = useState([]);
    const [players, setPlayers] = useState({});
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [generateError, setGenerateError] = useState(null);
    const [saveError, setSaveError] = useState(null);
    const [saveSuccess, setSaveSuccess] = useState(null);
    const [eventDetails, setEventDetails] = useState(null);

    useEffect(() => {
        fetchEventById(eventId)
            .then(event => {
                setEventDetails(event);
                return fetchEventPairings(eventId);
            })
            .then(data => {
                setPairings(data);
                const playerIds = new Set();
                data.forEach(pairing => {
                    playerIds.add(pairing.playerOneId);
                    playerIds.add(pairing.playerTwoId);
                });
                return Promise.all(Array.from(playerIds).map(playerId => fetchPlayerById(playerId)));
            })
            .then(playersData => {
                const playersMap = playersData.reduce((acc, player) => {
                    acc[player.id] = player.username;
                    return acc;
                }, {});
                setPlayers(playersMap);
                setLoading(false);
            })
            .catch(err => {
                setError(err.message);
                setLoading(false);
            });
    }, [eventId]);

    const handleGeneratePairings = () => {
        generatePairings(eventId)
            .then(() => {
                fetchEventPairings(eventId)
                    .then(data => setPairings(data))
                    .catch(err => setError(err.message));
                setGenerateError(null);
            })
            .catch(err => {
                setGenerateError(err.message);
            });
    };

    const handleResultChange = (index, result) => {
        setPairings(prevPairings => {
            const updatedPairings = [...prevPairings];
            updatedPairings[index].result = result;
            return updatedPairings;
        });
    };

    const handleSaveResults = async () => {
        try {
            const validPairings = pairings.filter(pairing => pairing.result !== -1);

            await savePairings(eventId, validPairings);
            const recalculatePromises = validPairings.flatMap(pairing => [
                recalculateWinrates(pairing.playerOneId),
                recalculateWinrates(pairing.playerTwoId)
            ]);

            await Promise.all(recalculatePromises);
            await finalizeRound(eventId);

            if (eventDetails) {
                const updatedEventDetails = { ...eventDetails, currentRound: eventDetails.currentRound + 1 };
                await updateEvent(eventId, updatedEventDetails);
            }

            setSaveSuccess("Pairings and results saved successfully. Round finalized.");
            setSaveError(null);
        } catch (err) {
            setSaveError("Error saving pairings and results: " + err.message);
            setSaveSuccess(null);
        }
    };

    if (loading) return <p>Loading pairings...</p>;
    if (error) return <p>Error loading pairings: {error}</p>;

    return (
        <div className="pairing-container">
            <h1>Event Pairings</h1>
            {user.role === 'ROLE_MANAGER' && !pairings.length && (
                <button onClick={handleGeneratePairings}>Generate Pairings</button>
            )}
            {generateError && <p className="error-message">{generateError}</p>}
            {saveError && <p className="error-message">{saveError}</p>}
            {saveSuccess && <p className="success-message">{saveSuccess}</p>}
            <ul>
                {pairings.map((pairing, index) => (
                    <li key={index} className="pairing-item">
                        <div>{players[pairing.playerOneId]} vs {players[pairing.playerTwoId]}</div>
                        {user.role === 'ROLE_MANAGER' && (
                            <div>
                                <label>
                                    <input
                                        type="radio"
                                        name={`pairing-${index}`}
                                        value="0"
                                        checked={pairing.result === 0}
                                        onChange={() => handleResultChange(index, 0)}
                                    />
                                    {players[pairing.playerOneId]} venceu
                                </label>
                                <label>
                                    <input
                                        type="radio"
                                        name={`pairing-${index}`}
                                        value="1"
                                        checked={pairing.result === 1}
                                        onChange={() => handleResultChange(index, 1)}
                                    />
                                    {players[pairing.playerTwoId]} venceu
                                </label>
                            </div>
                        )}
                    </li>
                ))}
            </ul>
            {user.role === 'ROLE_MANAGER' && pairings.length > 0 && (
                <button onClick={handleSaveResults}>Save Results</button>
            )}
        </div>
    );
}

export default EventPairing;
