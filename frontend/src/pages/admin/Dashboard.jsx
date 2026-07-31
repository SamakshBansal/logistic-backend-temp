import { useEffect, useState } from "react";
import DashboardLayout from "../../layouts/DashboardLayout";
import { Link } from "react-router-dom";
import { getDashboard } from "../../services/adminService";
import LoadingSpinner from "../../components/LoadingSpinner";

function Dashboard() {

    const [stats, setStats] = useState(null);

    useEffect(() => {
        loadDashboard();
    }, []);

    const loadDashboard = async () => {

        try {

            const response = await getDashboard();

            setStats(response.data.data);

        } catch (error) {

            alert("Failed to load dashboard");

        }

    };

    if (!stats) {

        return (

            <DashboardLayout>

                <div className="container mt-5">

                    <LoadingSpinner />

                </div>

            </DashboardLayout>

        );

    }

    return (

        <DashboardLayout>

            <div className="container mt-4">

                <div className="mb-4">

                    <h2 className="fw-bold">

                        Admin Dashboard

                    </h2>

                    <p className="text-muted">

                        Welcome! Monitor logistics operations and manage the system.

                    </p>

                </div>

                <div className="row g-3">

                    <StatCard
                        title="Total Orders"
                        value={stats.totalOrders}
                        color="primary"
                    />

                    <StatCard
                        title="Pending Payments"
                        value={stats.pendingPayments}
                        color="warning"
                    />

                    <StatCard
                        title="Successful Payments"
                        value={stats.successfulPayments}
                        color="success"
                    />

                    <StatCard
                        title="Created"
                        value={stats.createdOrders}
                        color="secondary"
                    />

                    <StatCard
                        title="Assigned"
                        value={stats.assignedOrders}
                        color="info"
                    />

                    <StatCard
                        title="Picked Up"
                        value={stats.pickedUpOrders}
                        color="primary"
                    />

                    <StatCard
                        title="In Transit"
                        value={stats.inTransitOrders}
                        color="warning"
                    />

                    <StatCard
                        title="Delivered"
                        value={stats.deliveredOrders}
                        color="success"
                    />

                    <StatCard
                        title="Cancelled"
                        value={stats.cancelledOrders}
                        color="danger"
                    />

                </div>

                <hr className="my-5" />

                <h4 className="mb-4">

                    Quick Actions

                </h4>

                <div className="row g-4">

                    <ActionCard
                        title="View Orders"
                        description="See all customer orders."
                        button="Open"
                        link="/admin/orders"
                        color="primary"
                    />

                    <ActionCard
                        title="Assign Agent"
                        description="Assign delivery agents."
                        button="Assign"
                        link="/admin/assign-agent"
                        color="success"
                    />

                    <ActionCard
                        title="Register Agent"
                        description="Create delivery agent accounts."
                        button="Register"
                        link="/admin/register-agent"
                        color="warning"
                    />

                    <ActionCard
                        title="Register Admin"
                        description="Create administrator accounts."
                        button="Register"
                        link="/admin/register-admin"
                        color="dark"
                    />

                </div>

            </div>

        </DashboardLayout>

    );

}

function StatCard({ title, value, color }) {

    return (

        <div className="col-lg-4 col-md-6">

            <div className={`card border-${color} shadow-sm`}>

                <div className="card-body text-center">

                    <h6>{title}</h6>

                    <h2 className={`text-${color}`}>

                        {value}

                    </h2>

                </div>

            </div>

        </div>

    );

}

function ActionCard({ title, description, button, link, color }) {

    return (

        <div className="col-md-6">

            <div className="card shadow-sm h-100">

                <div className="card-body">

                    <h5>{title}</h5>

                    <p className="text-muted">

                        {description}

                    </p>

                    <Link

                        to={link}

                        className={`btn btn-${color}`}

                    >

                        {button}

                    </Link>

                </div>

            </div>

        </div>

    );

}

export default Dashboard;