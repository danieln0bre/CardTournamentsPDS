// src/components/Login/Login.js
import React, { useState } from 'react';
import { loginUser } from '../../services/api';

function Login() {
    const [formData, setFormData] = useState({
        username: '',
        password: ''
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        loginUser(formData).then(response => {
            console.log('Login successful', response);
            // Aqui você pode redirecionar o usuário para a página principal ou de perfil
            // ou lidar com o armazenamento de tokens de autenticação, se aplicável.
        }).catch(error => {
            console.error('Login failed', error);
            // Lidar com erros de login, como mostrar mensagens ao usuário
        });
    };

    return (
        <div>
            <h1>Login</h1>
            <form onSubmit={handleSubmit}>
                <input
                    type="text"
                    name="username"
                    value={formData.username}
                    onChange={handleChange}
                    placeholder="Username"
                    required
                />
                <input
                    type="password"
                    name="password"
                    value={formData.password}
                    onChange={handleChange}
                    placeholder="Senha"
                    required
                />
                <button type="submit">Entrar</button>
            </form>
        </div>
    );
}

export default Login;
