import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { fetchEventById, fetchEventPairings, generatePairings, fetchTeamById, savePairings, recalculateWinrates, finalizeRound } from '../../services/api';
import { useUser } from '../../contexts/UserContext';
import './EventPairing.css';

function EventPairing() {
    const { eventId } = useParams();
    const navigate = useNavigate();
    const { user } = useUser();
    const [pairings, setPairings] = useState([]);
    const [entities, setEntities] = useState({});
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
                const entityIds = new Set();
                data.forEach(pairing => {
                    if (pairing.entityOneId !== 'Bye') entityIds.add(pairing.entityOneId);
                    if (pairing.entityTwoId !== 'Bye') entityIds.add(pairing.entityTwoId);
                });
                return Promise.all(Array.from(entityIds).map(entityId => fetchTeamById(entityId)));
            })
            .then(entitiesData => {
                const entitiesMap = entitiesData.reduce((acc, entity) => {
                    acc[entity.id] = entity;
                    return acc;
                }, {});
                setEntities(entitiesMap);
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
                ...entities[pairing.entityOneId]?.playerIds.map(playerId => recalculateWinrates(playerId)) || [],
                ...entities[pairing.entityTwoId]?.playerIds.map(playerId => recalculateWinrates(playerId)) || []
            ]);

            await Promise.all(recalculatePromises);
            await finalizeRound(eventId);

            const updatedEventDetails = await fetchEventById(eventId);

            if (updatedEventDetails.currentRound >= updatedEventDetails.numberOfRounds) {
                navigate(`/events/${eventId}/ranking`);
            } else {
                setEventDetails(updatedEventDetails);
            }

            setSaveSuccess("Pairings and results saved successfully. Round finalized.");
            setSaveError(null);

            window.location.reload();
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
                        <div>{entities[pairing.entityOneId]?.name || 'Unknown'} vs {pairing.entityTwoId === 'Bye' ? 'Bye' : entities[pairing.entityTwoId]?.name || 'Unknown'}</div>
                        {user.role === 'ROLE_MANAGER' && pairing.entityTwoId !== 'Bye' && (
                            <div>
                                <label>
                                    <input
                                        type="radio"
                                        name={`pairing-${index}`}
                                        value="0"
                                        checked={pairing.result === 0}
                                        onChange={() => handleResultChange(index, 0)}
                                    />
                                    {entities[pairing.entityOneId]?.name} venceu
                                </label>
                                <label>
                                    <input
                                        type="radio"
                                        name={`pairing-${index}`}
                                        value="1"
                                        checked={pairing.result === 1}
                                        onChange={() => handleResultChange(index, 1)}
                                    />
                                    {entities[pairing.entityTwoId]?.name} venceu
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
