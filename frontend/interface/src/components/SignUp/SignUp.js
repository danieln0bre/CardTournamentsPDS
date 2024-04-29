// src/components/SignUp/SignUp.js
import React, { useState } from 'react';
import { registerUser } from '../../services/api';

function SignUp() {
    const [formData, setFormData] = useState({
        username: '',
        email: '',
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
        registerUser(formData).then(response => {
            console.log('Registration successful', response);
        }).catch(error => {
            console.error('Registration failed', error);
        });
    };

    return (
        <div>
            <h1>Cadastro</h1>
            <form onSubmit={handleSubmit}>
                <input type="text" name="username" value={formData.username} onChange={handleChange} placeholder="Nome de usuário" required />
                <input type="email" name="email" value={formData.email} onChange={handleChange} placeholder="Email" required />
                <input type="password" name="password" value={formData.password} onChange={handleChange} placeholder="Senha" required />
                <button type="submit">Registrar</button>
            </form>
        </div>
    );
}

export default SignUp;
