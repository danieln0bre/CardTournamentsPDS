import axios from 'axios';

const baseUrl = 'http://localhost:8080/cardtournament/api';

// Função para verificar o status da resposta e lançar um erro se não for uma resposta bem-sucedida
const checkResponseStatus = (res) => {
    if (res.status >= 200 && res.status < 300) {
        return res.data;
    } else {
        throw new Error(`Failed to fetch data: ${res.status} ${res.statusText}`);
    }
};

export const fetchEvents = () => {
    return axios.get(`${baseUrl}/events/`).then(checkResponseStatus);
};

export const fetchEventById = (eventId) => {
    return axios.get(`${baseUrl}/events/${eventId}`).then(checkResponseStatus);
};

export const fetchPlayerById = (playerId) => {
    return axios.get(`${baseUrl}/players/${playerId}`).then(checkResponseStatus);
};

export const registerUser = (userDetails) => {
    return axios.post(`${baseUrl}/users/register/player`, userDetails, {
        headers: { 'Content-Type': 'application/json' }
    }).then(checkResponseStatus);
};

export const loginUser = (credentials) => {
    const params = new URLSearchParams(credentials).toString();
    return axios.post(`${baseUrl}/users/login?${params}`, {}, {
        headers: { 'Content-Type': 'application/json' }
    }).then(checkResponseStatus);
};


export const fetchGeneralRankings = () => {
    return axios.get(`${baseUrl}/players/rankings`).then(checkResponseStatus);
};
