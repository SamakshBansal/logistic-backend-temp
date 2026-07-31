import { BrowserRouter, Routes, Route } from "react-router-dom";

import Login from "../pages/auth/Login";
import Register from "../pages/auth/Register";

import AdminDashboard from "../pages/admin/Dashboard";
import CustomerDashboard from "../pages/customer/Dashboard";
import AgentDashboard from "../pages/agent/Dashboard";
import CreateOrder from "../pages/customer/CreateOrder";
import MyOrders from "../pages/customer/MyOrders";
import OrderDetails from "../pages/customer/OrderDetails";
import Orders from "../pages/admin/Orders";
import AssignAgent from "../pages/admin/AssignAgent";
import RegisterAgent from "../pages/admin/RegisterAgent";
import RegisterAdmin from "../pages/admin/RegisterAdmin";
import AgentOrders from "../pages/agent/Orders";
import TrackOrder from "../pages/customer/TrackOrder";
import AdminOrderDetails from "../pages/admin/OrderDetails";
import Home from "../pages/Home";

import ProtectedRoute from "../components/ProtectedRoute";

function AppRoutes() {
    return (
        <BrowserRouter>

            <Routes>

                {/* Public Routes */}

                <Route path="/" element={<Home />} />

                <Route path="/login" element={<Login />} />

                <Route path="/register" element={<Register />} />


                {/* Admin Routes */}

                <Route
                    path="/admin/dashboard"
                    element={
                        <ProtectedRoute roles={["ADMIN"]}>
                            <AdminDashboard />
                        </ProtectedRoute>
                    }
                />

                <Route
                    path="/admin/orders"
                    element={
                        <ProtectedRoute roles={["ADMIN"]}>
                            <Orders />
                        </ProtectedRoute>
                    }
                />

                <Route
                    path="/admin/orders/:orderId"
                    element={
                        <ProtectedRoute roles={["ADMIN"]}>
                            <AdminOrderDetails />
                        </ProtectedRoute>
                    }
                />

                <Route
                    path="/admin/assign-agent"
                    element={
                        <ProtectedRoute roles={["ADMIN"]}>
                            <AssignAgent />
                        </ProtectedRoute>
                    }
                />

                <Route
                    path="/admin/register-agent"
                    element={
                        <ProtectedRoute roles={["ADMIN"]}>
                            <RegisterAgent />
                        </ProtectedRoute>
                    }
                />

                <Route
                    path="/admin/register-admin"
                    element={
                        <ProtectedRoute roles={["ADMIN"]}>
                            <RegisterAdmin />
                        </ProtectedRoute>
                    }
                />



                {/* Customer Routes */}

                <Route
                    path="/customer/dashboard"
                    element={
                        <ProtectedRoute roles={["CUSTOMER"]}>
                            <CustomerDashboard />
                        </ProtectedRoute>
                    }
                />

                <Route
                    path="/customer/create-order"
                    element={
                        <ProtectedRoute roles={["CUSTOMER"]}>
                            <CreateOrder />
                        </ProtectedRoute>
                    }
                />

                <Route
                    path="/customer/orders"
                    element={
                        <ProtectedRoute roles={["CUSTOMER"]}>
                            <MyOrders />
                        </ProtectedRoute>
                    }
                />

                <Route
                    path="/customer/orders/:orderId"
                    element={
                        <ProtectedRoute roles={["CUSTOMER"]}>
                            <OrderDetails />
                        </ProtectedRoute>
                    }
                />

                <Route
                    path="/customer/track-order"
                    element={
                        <ProtectedRoute roles={["CUSTOMER"]}>
                            <TrackOrder />
                        </ProtectedRoute>
                    }
                />



                {/* Agent Routes */}

                <Route
                    path="/agent/dashboard"
                    element={
                        <ProtectedRoute roles={["DELIVERY_AGENT"]}>
                            <AgentDashboard />
                        </ProtectedRoute>
                    }
                />

                <Route
                    path="/agent/orders"
                    element={
                        <ProtectedRoute roles={["DELIVERY_AGENT"]}>
                            <AgentOrders />
                        </ProtectedRoute>
                    }
                />

            </Routes>

        </BrowserRouter>
    );
}

export default AppRoutes;