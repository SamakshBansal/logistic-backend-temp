import { Link } from "react-router-dom";
import "./Home.css";

function Home() {
    return (
        <div className="home">
            {/* Minimalist Top Navbar */}
            <header className="navbar">
                <div className="nav-brand">
                    📦 Logistics<span className="brand-accent">Hub</span>
                </div>
            </header>

            {/* Hero Section */}
            <section className="hero">
                <div className="hero-glow"></div>
                <div className="hero-content">
                    <span>🚀 Next-Gen Logistics Platform</span>
                    <h1>Smart Logistics & Live Shipment Tracking</h1>
                    <p>
                        Manage end-to-end shipments, track agent deliveries in real-time,
                        process payments effortlessly, and keep customers informed.
                    </p>

                    <div className="hero-buttons">
                        <Link to="/login" className="btn btn-primary btn-lg">
                            Go to Dashboard
                        </Link>
                        <Link to="/register" className="btn btn-secondary btn-lg">
                            Create Free Account
                        </Link>
                    </div>
                </div>
            </section>

            {/* Core Features Grid */}
            <section className="features-container">
                <div className="features-header">
                    <h2>Everything you need to ship faster</h2>
                </div>

                <div className="features">
                    <div className="card">
                        <div className="card-icon">📦</div>
                        <h3>Order Management</h3>
                        <p>Create, customize, and manage active shipments with dynamic distance and weight pricing.</p>
                    </div>

                    <div className="card">
                        <div className="card-icon">🚚</div>
                        <h3>Live Tracking</h3>
                        <p>Monitor real-time status transitions from dispatch to final delivery instantly.</p>
                    </div>

                    <div className="card">
                        <div className="card-icon">💳</div>
                        <h3>Seamless Payments</h3>
                        <p>Integrated sandbox payment processing for fast, safe, and transparent transactions.</p>
                    </div>

                    <div className="card">
                        <div className="card-icon">🔔</div>
                        <h3>Instant Alerts</h3>
                        <p>Asynchronous event-driven updates keep agents, admins, and customers in sync.</p>
                    </div>
                </div>
            </section>

            {/* Footer */}
            <footer>
                <p>© {new Date().getFullYear()} Logistic & Shipment Tracking System. All rights reserved.</p>
            </footer>
        </div>
    );
}

export default Home;