import { Link } from "react-router-dom";
import DashboardLayout from "../../layouts/DashboardLayout";

function Dashboard() {

    return (

        <DashboardLayout>

            <h2 className="mb-4">Customer Dashboard</h2>

            <div className="row">

                <div className="col-md-4">

                    <div className="card p-3 shadow">

                        <h5>Create Order</h5>

                        <Link
                            to="/customer/create-order"
                            className="btn btn-primary"
                        >
                            Open
                        </Link>

                    </div>

                </div>

                <div className="col-md-4">

                    <div className="card p-3 shadow">

                        <h5>My Orders</h5>

                        <Link
                            to="/customer/orders"
                            className="btn btn-success"
                        >
                            Open
                        </Link>

                    </div>

                </div>

            </div>

        </DashboardLayout>

    );

}

export default Dashboard;