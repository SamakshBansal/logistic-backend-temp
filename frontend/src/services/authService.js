import api from "../api/axios";


const register = async (userData) => {

    const response = await api.post(
        "/api/v1/auth/register",
        userData
    );

    return response.data;

};


const login = async (credentials) => {

    const response = await api.post(
        "/api/v1/auth/login",
        credentials
    );

    return response.data;

};


const refreshToken = async (refreshToken) => {

    const response = await api.post(
        "/api/v1/auth/refresh",
        {
            refreshToken
        }
    );

    return response.data;

};


const logout = async (refreshToken) => {

    const response = await api.post(
        "/api/v1/auth/logout",
        {
            refreshToken
        }
    );

    return response.data;

};


const getCurrentUser = async () => {

    const response = await api.get(
        "/api/v1/auth/me"
    );

    return response.data;

};


export default {
    register,
    login,
    refreshToken,
    logout,
    getCurrentUser
};