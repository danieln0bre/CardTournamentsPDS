// src/components/Login/Login.js
import React, { useState } from 'react';
import { loginUser } from '../../services/api';
import { useNavigate } from 'react-router-dom';
import { useUser } from '../../contexts/UserContext';
import './Login.css';

function Login() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState(null);
    const navigate = useNavigate();
    const { setUser } = useUser();

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const credentials = { username, password };
            const response = await loginUser(credentials);
            console.log('Login successful', response);
            setUser(response);  // Store user data in context
            navigate('/my-events');  // Redirect to events
        } catch (error) {
            setError('Login failed. Please check your username and password.');
            console.error('Login error', error);
        }
    };

    return (
        <div className="login-container">
            <h1>Login</h1>
            <form onSubmit={handleLogin}>
                <input
                    type="text"
                    placeholder="Username"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    required
                />
                <input
                    type="password"
                    placeholder="Password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                />
                <button type="submit">Login</button>
            </form>
            {error && <div className="error">{error}</div>}
        </div>
    );
}

export default Login;
