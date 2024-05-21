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
    return axiosInstance.get(`/events/${eventId}`)
        .then(checkResponseStatus);
};

export const fetchPlayerById = (playerId) => {
    return axiosInstance.get(`/players/${playerId}`)
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
                console.log('Login response data:', response.data); // Log the response data
                return response.data; // Login successful, return user data
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
