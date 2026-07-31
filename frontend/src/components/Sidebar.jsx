import { NavLink } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

import {
    FaHome,
    FaBoxOpen,
    FaPlusCircle,
    FaMapMarkedAlt,
    FaClipboardList,
    FaUserTie,
    FaUserPlus,
    FaTruck,
    FaUserCircle
} from "react-icons/fa";

import "../styles/sidebar.css";

function Sidebar() {

    const { user } = useAuth();

    const role = user?.role;

    const dashboardRoute = {
        ADMIN: "/admin/dashboard",
        CUSTOMER: "/customer/dashboard",
        DELIVERY_AGENT: "/agent/dashboard"
    }[role];

    return (

        <aside className="sidebar">

            <div className="sidebar-header">

                <FaUserCircle size={60} />

                <h4>{user?.name}</h4>

                <p>{role?.replace("_", " ")}</p>

            </div>



            <nav className="sidebar-menu">

                <NavLink to={dashboardRoute}>
                    <FaHome className="menu-icon" />
                    Dashboard
                </NavLink>

                {role === "CUSTOMER" && (
                    <>
                        <NavLink to="/customer/create-order">
                            <FaPlusCircle className="menu-icon" />
                            Create Order
                        </NavLink>

                        <NavLink to="/customer/orders">
                            <FaBoxOpen className="menu-icon" />
                            My Orders
                        </NavLink>

                        <NavLink to="/customer/track-order">
                            <FaMapMarkedAlt className="menu-icon" />
                            Track Order
                        </NavLink>
                    </>
                )}

                {role === "ADMIN" && (
                    <>
                        <NavLink to="/admin/orders">
                            <FaClipboardList className="menu-icon" />
                            All Orders
                        </NavLink>

                        <NavLink to="/admin/assign-agent">
                            <FaTruck className="menu-icon" />
                            Assign Agent
                        </NavLink>

                        <NavLink to="/admin/register-agent">
                            <FaUserTie className="menu-icon" />
                            Register Agent
                        </NavLink>

                        <NavLink to="/admin/register-admin">
                            <FaUserPlus className="menu-icon" />
                            Register Admin
                        </NavLink>
                    </>
                )}

                {role === "DELIVERY_AGENT" && (
                    <NavLink to="/agent/orders">
                        <FaTruck className="menu-icon" />
                        Assigned Orders
                    </NavLink>
                )}

            </nav>

        </aside>

    );

}

export default Sidebar;