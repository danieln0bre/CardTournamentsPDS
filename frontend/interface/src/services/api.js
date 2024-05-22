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

// src/services/api.js
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




export const fetchGeneralRankings = async () => {
    return axiosInstance.get('/players/rankings')
        .then(checkResponseStatus);
};
