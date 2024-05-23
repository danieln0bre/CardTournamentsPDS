import React, { createContext, useContext, useState, useEffect } from 'react';
import { loginUser } from '../services/api';

const UserContext = createContext();

export const UserProvider = ({ children }) => {
    const [user, setUser] = useState(() => {
        const savedUser = localStorage.getItem('user');
        return savedUser ? JSON.parse(savedUser) : null;
    });

    useEffect(() => {
        if (user) {
            localStorage.setItem('user', JSON.stringify(user));
        } else {
            localStorage.removeItem('user');
        }
    }, [user]);

    const login = async (username, password) => {
        try {
            const userData = await loginUser({ username, password });
            setUser(userData);
        } catch (error) {
            console.error('Login error', error);
            throw error;
        }
    };

    return (
        <UserContext.Provider value={{ user, setUser, login }}>
            {children}
        </UserContext.Provider>
    );
};

export const useUser = () => useContext(UserContext);
