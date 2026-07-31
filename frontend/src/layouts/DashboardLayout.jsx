import Navbar from "../components/Navbar";
import Sidebar from "../components/Sidebar";

import "../styles/layout.css";

function DashboardLayout({ children }) {

    return (
        <>

            <Navbar />

            <div className="dashboard-layout">

                <Sidebar />

                <main className="main-content">
                    {children}
                </main>

            </div>

        </>

    );
}

export default DashboardLayout;