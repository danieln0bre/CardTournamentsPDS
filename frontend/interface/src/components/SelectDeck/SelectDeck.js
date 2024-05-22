import React, { useEffect, useState } from 'react';
import { useUser } from '../../contexts/UserContext';
import { fetchWinningDeckNames, updatePlayerDeck } from '../../services/api';
import './SelectDeck.css';

function SelectDeck() {
    const { user } = useUser();
    const [decks, setDecks] = useState([]);
    const [selectedDeckId, setSelectedDeckId] = useState('');
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);

    useEffect(() => {
        fetchWinningDeckNames()
            .then(data => {
                setDecks(data);
                setLoading(false);
            })
            .catch(err => {
                setError(err.message);
                setLoading(false);
            });
    }, []);

    const handleSubmit = (event) => {
        event.preventDefault();
        if (selectedDeckId) {
            updatePlayerDeck(user.id, selectedDeckId)
                .then(() => {
                    setSuccess('Deck updated successfully!');
                })
                .catch(err => {
                    setError('Error updating deck: ' + err.message);
                });
        }
    };

    const handleDeckChange = (event) => {
        setSelectedDeckId(event.target.value);
    };

    if (loading) return <p>Loading decks...</p>;
    if (error) return <p>Error loading decks: {error}</p>;

    return (
        <div className="select-deck-container">
            <h1>Select Your Deck</h1>
            <form onSubmit={handleSubmit}>
                {decks.map(deck => (
                    <div key={deck.id} className="deck-option">
                        <input
                            type="radio"
                            id={deck.id}
                            name="deck"
                            value={deck.id}
                            checked={selectedDeckId === deck.id}
                            onChange={handleDeckChange}
                        />
                        <label htmlFor={deck.id}>{deck.deckName}</label>
                    </div>
                ))}
                <button type="submit">Update Deck</button>
            </form>
            {success && <p className="success-message">{success}</p>}
            {error && <p className="error-message">{error}</p>}
        </div>
    );
}

export default SelectDeck;
