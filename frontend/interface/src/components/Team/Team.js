import React, { useState, useEffect } from 'react';
import { createTeam, addPlayerToTeam, removePlayerFromTeam, fetchPlayerTeam, fetchPlayerById, getUserIdByName, fetchTeamById, fetchWinningDeckNames, updatePlayerDeck } from '../../services/api';
import { useUser } from '../../contexts/UserContext';
import './Team.css';

function Teams() {
    const { user } = useUser();
    const [team, setTeam] = useState(null);
    const [teamName, setTeamName] = useState('');
    const [newPlayerNickname, setNewPlayerNickname] = useState('');
    const [error, setError] = useState('');
    const [players, setPlayers] = useState([]);
    const [decks, setDecks] = useState([]);
    const [selectedDecks, setSelectedDecks] = useState({});
    const [success, setSuccess] = useState(null);

    useEffect(() => {
        fetchPlayerTeam(user.id)
            .then(teamId => {
                if (teamId) {
                    fetchTeamById(teamId)
                        .then(fetchedTeam => {
                            setTeam(fetchedTeam);
                            fetchTeamPlayers(fetchedTeam.playerIds);
                        })
                        .catch(err => console.error('Failed to fetch team by ID:', err));
                } else {
                    console.error('No team found for user.');
                }
            })
            .catch(err => console.error('Failed to fetch team:', err));

        fetchWinningDeckNames()
            .then(data => setDecks(data))
            .catch(err => setError('Failed to fetch decks: ' + err.message));
    }, [user.id]);

    const fetchTeamPlayers = (playerIds) => {
        if (!playerIds) {
            console.error('playerIds is undefined');
            return;
        }
        Promise.all(playerIds.map(playerId => fetchPlayerById(playerId)))
            .then(playerDetails => setPlayers(playerDetails))
            .catch(err => console.error('Failed to fetch players:', err));
    };

    const handleCreateTeam = () => {
        if (teamName.trim() === '') {
            setError('Nome do time não pode estar vazio');
            return;
        }
        createTeam(teamName, user.id)
            .then(newTeam => {
                setTeam(newTeam);
                fetchTeamPlayers(newTeam.playerIds);
            })
            .catch(err => setError('Erro ao criar time: ' + err.message));
    };

    const handleAddPlayer = () => {
        if (players.length >= 5) {
            setError('O time já possui o número máximo de jogadores');
            return;
        }
        getUserIdByName(newPlayerNickname)
            .then(playerId => addPlayerToTeam(team.id, playerId))
            .then(updatedTeam => {
                setTeam(updatedTeam);
                fetchTeamPlayers(updatedTeam.playerIds);
                setNewPlayerNickname('');
            })
            .catch(err => setError('Erro ao adicionar jogador: ' + err.message));
    };

    const handleRemovePlayer = (playerId) => {
        removePlayerFromTeam(team.id, playerId)
            .then(updatedTeam => {
                setTeam(updatedTeam);
                fetchTeamPlayers(updatedTeam.playerIds);
            })
            .catch(err => setError('Erro ao remover jogador: ' + err.message));
    };

    const handleDeckChange = (playerId, deckId) => {
        setSelectedDecks(prev => ({ ...prev, [playerId]: deckId }));
    };

    const handleSaveTeam = () => {
        Promise.all(players.map(player => {
            const selectedDeckId = selectedDecks[player.id];
            if (selectedDeckId) {
                return updatePlayerDeck(player.id, selectedDeckId);
            }
            return Promise.resolve();
        }))
        .then(() => setSuccess('Team decks updated successfully!'))
        .catch(err => setError('Error updating team decks: ' + err.message));
    };

    return (
        <div className="team-container">
            <h1>Gerenciar Times</h1>
            {team ? (
                <div>
                    <div className="team-header">
                        <h2 className="team-name">{team.name}</h2>
                    </div>
                    <ul className="team-list">
                        {players.map(player => (
                            <li key={player.id} className="team-item">
                                {player.username}
                                {player.id !== user.id && (
                                    <div className="team-controls">
                                        <button onClick={() => handleRemovePlayer(player.id)}>Remover</button>
                                    </div>
                                )}
                                <div className="deck-selection">
                                    <select
                                        value={selectedDecks[player.id] || ''}
                                        onChange={(e) => handleDeckChange(player.id, e.target.value)}
                                    >
                                        <option value="">Select Deck</option>
                                        {decks.map(deck => (
                                            <option key={deck.id} value={deck.id}>{deck.deckName}</option>
                                        ))}
                                    </select>
                                </div>
                            </li>
                        ))}
                    </ul>
                    {players.length < 5 && (
                        <div className="add-player">
                            <input
                                type="text"
                                value={newPlayerNickname}
                                onChange={(e) => setNewPlayerNickname(e.target.value)}
                                placeholder="Nickname do jogador para adicionar"
                            />
                            <button onClick={handleAddPlayer}>Adicionar Jogador</button>
                        </div>
                    )}
                    <button onClick={handleSaveTeam}>Salvar Time</button>
                </div>
            ) : (
                <div className="create-team">
                    <input
                        type="text"
                        value={teamName}
                        onChange={(e) => setTeamName(e.target.value)}
                        placeholder="Nome do Time"
                    />
                    <button onClick={handleCreateTeam}>Criar Time</button>
                </div>
            )}
            {success && <p className="success-message">{success}</p>}
            {error && <p className="error-message">{error}</p>}
        </div>
    );
}

export default Teams;
