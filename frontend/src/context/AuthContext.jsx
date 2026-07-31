import { createContext, useContext, useState } from "react";

const AuthContext = createContext();


export function AuthProvider({ children }) {


    const [user, setUser] = useState(() => {

        const storedUser = localStorage.getItem("user");

        try {

            return storedUser && storedUser !== "undefined"
                ? JSON.parse(storedUser)
                : null;

        }
        catch (error) {

            console.error("Invalid user data in storage");

            localStorage.removeItem("user");

            return null;
        }

    });



    const login = (userData) => {

        if (!userData) {
            console.error("Login failed: user data missing");
            return;
        }


        localStorage.setItem(
            "user",
            JSON.stringify(userData)
        );


        if (userData.accessToken) {
            localStorage.setItem(
                "token",
                userData.accessToken
            );
        }


        if (userData.refreshToken) {
            localStorage.setItem(
                "refreshToken",
                userData.refreshToken
            );
        }


        setUser(userData);

    };



    const logout = () => {


        localStorage.removeItem("user");

        localStorage.removeItem("token");

        localStorage.removeItem("refreshToken");


        setUser(null);

    };



    const hasRole = (role) => {

        return user?.role === role;

    };



    return (

        <AuthContext.Provider

            value={{

                user,

                login,

                logout,

                hasRole,

                isAuthenticated: !!user

            }}

        >

            {children}

        </AuthContext.Provider>

    );

}



export const useAuth = () => useContext(AuthContext);