import React, { useEffect, useState } from 'react';
import { useUser } from '../../contexts/UserContext';
import { fetchPlayerById } from '../../services/api';
import './Profile.css';

function Profile() {
    const { user } = useUser();
    const [playerData, setPlayerData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        if (user && user.id) {
            fetchPlayerById(user.id)
                .then(data => {
                    setPlayerData(data);
                    setLoading(false);
                })
                .catch(err => {
                    setError(err.message);
                    setLoading(false);
                });
        }
    }, [user]);

    if (loading) return <p>Loading profile...</p>;
    if (error) return <p>Error loading profile: {error}</p>;

    return (
        <div className="profile-container">
            <h1>Profile</h1>
            {playerData && (
                <>
                    <p><strong>Name:</strong> {playerData.name}</p>
                    <p><strong>Username:</strong> {playerData.username}</p>
                    <p><strong>Email:</strong> {playerData.email}</p>
                    <p><strong>Rank Points:</strong> {playerData.rankPoints}</p>
                    <p><strong>Winrate:</strong> {playerData.winrate}%</p>
                    <p><strong>Deck:</strong> {playerData.deck ? playerData.deck.deckName : 'No deck'}</p>
                    <p><strong>Applied Events:</strong> {playerData.appliedEventsId.length}</p>
                </>
            )}
        </div>
    );
}

export default Profile;
