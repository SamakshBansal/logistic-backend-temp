import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { toast } from "react-toastify";
import api from "../api/axios";

import { FaBell, FaTruck } from "react-icons/fa";

import "../styles/navbar.css";

function Navbar() {

    const navigate = useNavigate();

    const { user, logout } = useAuth();

    const handleLogout = async () => {

        try {

            const refreshToken = localStorage.getItem("refreshToken");

            if (refreshToken) {

                await api.post("/api/v1/auth/logout", {
                    refreshToken
                });

            }

        } catch (error) {

            console.log(error);

        } finally {

            logout();

            toast.success("Logged out successfully");

            navigate("/login");

        }

    };

    const initials = user?.name
        ?.split(" ")
        .map(word => word[0])
        .join("")
        .toUpperCase();

    return (

        <nav className="top-navbar">

            <div className="navbar-left">

                <FaTruck className="logo-icon" />

                <div>

                    <h4 className="logo-title">
                        Logistics System
                    </h4>

                    <p className="logo-subtitle">
                        Shipment Management
                    </p>

                </div>

            </div>

            <div className="navbar-right">

                <div className="notification">

                    <FaBell />

                </div>

                <div className="user-box">

                    <div className="avatar">

                        {initials}

                    </div>

                    <div className="user-info">

                        <h6>{user?.name}</h6>

                        <p>{user?.role?.replace("_", " ")}</p>

                    </div>

                </div>

                <button
                    className="btn btn-outline-danger logout-btn"
                    onClick={handleLogout}
                >
                    Logout
                </button>

            </div>

        </nav>

    );

}

export default Navbar;