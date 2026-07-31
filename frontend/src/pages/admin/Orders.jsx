import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import DashboardLayout from "../../layouts/DashboardLayout";
import { getAllOrders } from "../../services/adminService";

function Orders() {

    const [orders, setOrders] = useState([]);

    useEffect(() => {
        loadOrders();
    }, []);

    const loadOrders = async () => {

        try {

            const response = await getAllOrders();

            setOrders(response.data.data);

        } catch (error) {

            alert(
                error.response?.data?.message ||
                "Failed to load orders"
            );

        }

    };

    return (

        <DashboardLayout>

            <div className="container mt-4">

                <h3>All Orders</h3>

                <table className="table table-bordered table-striped table-hover">

                    <thead className="table-dark">

                        <tr>

                            <th>Tracking</th>

                            <th>Customer</th>

                            <th>Receiver</th>

                            <th>Status</th>

                            <th>Payment</th>

                            <th>Amount</th>

                            <th>Actions</th>

                        </tr>

                    </thead>

                    <tbody>

                        {

                            orders.length === 0 ? (

                                <tr>

                                    <td
                                        colSpan="7"
                                        className="text-center"
                                    >
                                        No Orders Found
                                    </td>

                                </tr>

                            ) : (

                                orders.map(order => (

                                    <tr key={order.id}>

                                        <td>{order.trackingNumber}</td>

                                        <td>{order.customerId}</td>

                                        <td>{order.receiverName}</td>

                                        <td>

                                            <span className="badge bg-primary">
                                                {order.orderStatus}
                                            </span>

                                        </td>

                                        <td>

                                            <span
                                                className={`badge ${order.paymentStatus === "SUCCESS"
                                                    ? "bg-success"
                                                    : "bg-warning text-dark"
                                                    }`}
                                            >
                                                {order.paymentStatus}
                                            </span>

                                        </td>

                                        <td>₹ {order.amount}</td>

                                        <td>

                                            <Link
                                                to={`/admin/orders/${order.id}`}
                                                className="btn btn-info btn-sm"
                                            >
                                                View Details
                                            </Link>

                                        </td>

                                    </tr>

                                ))

                            )

                        }

                    </tbody>

                </table>

            </div>

        </DashboardLayout>

    );

}

export default Orders;