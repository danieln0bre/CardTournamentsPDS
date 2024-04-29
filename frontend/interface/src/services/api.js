// src/services/api.js
const baseUrl = 'http://localhost:8080/cardtournament/api';

// Função para verificar o status da resposta e lançar um erro se não for uma resposta bem-sucedida
const checkResponseStatus = res => {
    if (!res.ok) {
        throw new Error(`Failed to fetch data: ${res.status} ${res.statusText}`);
    }
    return res.json();
};

export const fetchEvents = () => {
    return fetch(`${baseUrl}/events/`).then(checkResponseStatus);
};

export const fetchEventById = (eventId) => {
    return fetch(`${baseUrl}/events/${eventId}`).then(checkResponseStatus);
};

export const fetchPlayerById = (playerId) => {
    return fetch(`${baseUrl}/players/${playerId}`).then(checkResponseStatus);
};

export const registerUser = (userDetails) => {
    return fetch(`${baseUrl}/users/register/player`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(userDetails)
    }).then(checkResponseStatus);
};

export const loginUser = (credentials) => {
    return fetch(`${baseUrl}/users/login`, {  // Assumindo que há um endpoint de login
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(credentials)
    }).then(checkResponseStatus);
};

export const fetchGeneralRankings = () => {
    return fetch(`${baseUrl}/players/rankings`).then(checkResponseStatus);
};
