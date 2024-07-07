import axios from 'axios';

const baseUrl = 'http://localhost:8080/cardtournament/api';

// Create an axios instance for common configuration
const axiosInstance = axios.create({
    baseURL: baseUrl,
    headers: { 'Content-Type': 'application/json' },
    withCredentials: true // Include credentials to send the session cookie
});

// Function to check response status and throw an error if the response is not successful
const checkResponseStatus = (res) => {
    if (res.status >= 200 && res.status < 300) {
        return res.data;
    } else {
        throw new Error(`Failed to fetch data: ${res.status} ${res.statusText}`);
    }
};

export const fetchEvents = () => {
    return axiosInstance.get('/events/')
        .then(checkResponseStatus);
};

export const finalizeEvent = (eventId) => {
    return axiosInstance.put(`/events/${eventId}/finalize`)
        .then(checkResponseStatus);
};

export const fetchEventById = (eventId) => {
    return axiosInstance.get(`/events/${eventId}/get`)
        .then(checkResponseStatus);
};

export const fetchPlayerById = (playerId) => {
    return axiosInstance.get(`/players/${playerId}`)
        .then(checkResponseStatus);
};

export const fetchEventByName = (eventName) => {
    return axiosInstance.get(`/events/name/${encodeURIComponent(eventName)}`)
        .then(checkResponseStatus);
};

export const fetchPlayerEvents = (playerId) => {
    return axiosInstance.get(`/players/${playerId}/events`)
        .then(checkResponseStatus);
};

export const registerUser = (userDetails) => {
    return axiosInstance.post('/users/register/player', userDetails)
        .then(checkResponseStatus);
};

export const fetchManagerEvents = (managerId) => {
    return axiosInstance.get(`/events/manager/${managerId}/events`)
        .then(response => response.data);
};

export const fetchWinningDeckNames = () => {
    return axiosInstance.get('/players/game-objects').then(response => response.data);
};

export const updatePlayerDeck = (playerId, deckId) => {
    return axiosInstance.put(`/players/${playerId}/update-game-object`, deckId, {
        headers: {
            'Content-Type': 'text/plain'
        }
    }).then(response => response.data);
};

export const fetchDeckMatchups = (eventId) => {
    return axiosInstance.get(`/events/${eventId}/gameobject-matchups`).then(response => response.data);
};

export const fetchEventRankings = (eventId) => {
    return axiosInstance.get(`/events/${eventId}/rankings`).then(response => response.data);
};

export const fetchEventResults = (eventId) => {
    return axiosInstance.get(`/events/${eventId}/results`).then(checkResponseStatus);
};

export const fetchEventResultRanking = (eventId) => {
    return axiosInstance.get(`/events/${eventId}/result-ranking`).then(checkResponseStatus);
};

export const fetchEventPairings = (eventId) => {
    return axiosInstance.get(`/event-pairings/${eventId}/pairings`).then(response => response.data);
};

export const loginUser = (credentials) => {
    return axiosInstance.post('/users/login', credentials)
        .then(response => {
            if (response.status === 200) {
                console.log('Login response data:', response.data);
                const userData = response.data;
                // Save user data to context and local storage
                localStorage.setItem('user', JSON.stringify(userData));
                return userData;
            } else {
                throw new Error('Login failed');
            }
        }).catch(error => {
            throw error;
        });
};

export const createEvent = (managerId, eventData) => {
    return axiosInstance.post(`/events/createEvent?managerId=${managerId}`, eventData)
        .then(response => response.data);
};

export const updateEvent = (eventId, eventDetails) => {
    return axiosInstance.put(`/events/${eventId}/update`, eventDetails)
        .then(response => response.data);
};

export const fetchLoggedInPlayerEvents = () => {
    const user = JSON.parse(localStorage.getItem('user'));
    const playerId = user && user.id;
    if (playerId) {
        return axiosInstance.get(`/players/${playerId}/events`)
            .then(checkResponseStatus);
    } else {
        return Promise.reject(new Error('No logged in user found'));
    }
};

export const startEvent = (eventId) => {
    return axiosInstance.post(`/event-pairings/${eventId}/start`).then(response => response.data);
};

export const addEventToEntity = (entityId, eventId) => {
    return axiosInstance.put(`/players/${entityId}/entities/add`, eventId, {
        headers: {
            'Content-Type': 'text/plain'
        }
    }).then(response => response.data);
};

export const fetchGeneralRankings = async () => {
    return axiosInstance.get('/players/rankings')
        .then(checkResponseStatus);
};

export const generatePairings = (eventId) => {
    return axiosInstance.post(`/event-pairings/${eventId}/generate-pairings`).then(response => response.data);
};

export const savePairings = (eventId, pairings) => {
    return axiosInstance.post(`/event-pairings/${eventId}/save-pairings`, pairings)
        .then(response => response.data);
};

export const recalculateWinrates = (playerId) => {
    return axiosInstance.post(`/players/${playerId}/recalculate-winrates`)
        .then(response => response.data);
};

export const finalizeRound = (eventId) => {
    return axiosInstance.post(`/event-pairings/${eventId}/finalize-round`)
        .then(response => response.data);
};

export const fetchManagerById = managerId => {
    return axiosInstance.get(`/users/manager/${managerId}`).then(checkResponseStatus);
};

export const fetchGameObjectById = async (gameObjectId) => {
    const response = await axiosInstance.get(`/players/game-objects/${gameObjectId}`);
    return response.data;
};

export const fetchDeckById = (deckId) => {
    return axiosInstance.get(`/players/game-objects/${deckId}`).then(checkResponseStatus);
};
export const createTeam = (name, ownerId) => {
    return axiosInstance.post(`/teams/create`, null, {
        params: { name, ownerId }
    })
    .then(checkResponseStatus);
};

export const addPlayerToTeam = (teamId, playerId) => {
    return axiosInstance.post(`/teams/${teamId}/add-player`, null, {
        params: { playerId }
    })
    .then(checkResponseStatus);
};

export const removePlayerFromTeam = async (teamId, playerId) => {
    const response = await axiosInstance.delete(`/teams/${teamId}/remove-player/${playerId}`);
    return response.data;
};

export const fetchPlayerTeam = (playerId) => {
    return axiosInstance.get(`/teams/${playerId}/team`)
        .then(checkResponseStatus);
};

export const getUserIdByName = (username) => {
    return axiosInstance.get(`/users/getUserIdByName`, {
        params: { username }
    }).then(response => response.data);
};

export const fetchTeamById = (teamId) => {
    return axiosInstance.get(`/teams/${teamId}`)
        .then(checkResponseStatus);
};
