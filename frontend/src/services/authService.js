import api from './api';

export const loginUser = async (credentials) => {
    const response = await api.post('/auth/login', credentials);
    return response.data; // Should return { token: "..." }
};

export const registerUser = async (userData) => {
    const response = await api.post('/auth/register', userData);
    return response.data;
};
