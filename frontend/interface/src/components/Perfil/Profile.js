import React, { useEffect, useState } from 'react';
import { useUser } from '../../contexts/UserContext';
import { fetchPlayerById, fetchManagerById, fetchDeckById } from '../../services/api';
import './Profile.css';

function Profile() {
    const { user } = useUser();
    const [userData, setUserData] = useState(null);
    const [deckName, setDeckName] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        if (user && user.id) {
            const fetchData = async () => {
                try {
                    let data;
                    if (user.role === 'ROLE_PLAYER') {
                        data = await fetchPlayerById(user.id);
                        if (data.deckId) {
                            const deck = await fetchDeckById(data.deckId);
                            setDeckName(deck.deckName);
                        }
                    } else if (user.role === 'ROLE_MANAGER') {
                        data = await fetchManagerById(user.id);
                    }
                    setUserData(data);
                } catch (err) {
                    setError(err.message);
                } finally {
                    setLoading(false);
                }
            };

            fetchData();
        }
    }, [user]);

    if (loading) return <p>Loading profile...</p>;
    if (error) return <p>Error loading profile: {error}</p>;

    return (
        <div className="profile-container">
            <h1>Profile</h1>
            {userData && (
                <>
                    <p><strong>Name:</strong> {userData.name}</p>
                    <p><strong>Username:</strong> {userData.username}</p>
                    <p><strong>Email:</strong> {userData.email}</p>
                    {user.role === 'ROLE_PLAYER' && (
                        <>
                            <p><strong>Rank Points:</strong> {userData.rankPoints}</p>
                            <p><strong>Winrate:</strong> {userData.winrate}%</p>
                            <p><strong>Deck:</strong> {deckName || 'No deck'}</p>
                            <p><strong>Applied Events:</strong> {userData.appliedEventsId.length}</p>
                        </>
                    )}
                    {user.role === 'ROLE_MANAGER' && (
                        <>
                            <p><strong>Managed Events:</strong> {userData.events.length}</p>
                        </>
                    )}
                </>
            )}
        </div>
    );
}

export default Profile;
