import DashboardLayout from "../../layouts/DashboardLayout";
import { Link } from "react-router-dom";

function Dashboard() {

    return (

        <DashboardLayout>

            <h2>Welcome Delivery Agent</h2>

            <div className="row mt-4">

                <div className="col-md-4">

                    <div className="card shadow">

                        <div className="card-body text-center">

                            <h5>Assigned Orders</h5>

                            <p>
                                View all orders assigned to you.
                            </p>

                            <Link
                                to="/agent/orders"
                                className="btn btn-primary"
                            >
                                View Orders
                            </Link>

                        </div>

                    </div>

                </div>

            </div>

        </DashboardLayout>

    );

}

export default Dashboard;