import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import LoadingSpinner from "../../components/LoadingSpinner";
import DashboardLayout from "../../layouts/DashboardLayout";
import { getOrderById } from "../../services/adminService";

function OrderDetails() {

    const { orderId } = useParams();

    const [order, setOrder] = useState(null);

    const [loading, setLoading] = useState(true);

    useEffect(() => {

        loadOrder();

    }, []);

    const loadOrder = async () => {

        try {

            const response = await getOrderById(orderId);

            setOrder(response.data.data);

        } catch (error) {

            alert(
                error.response?.data?.message ||
                "Failed to load order"
            );

        } finally {

            setLoading(false);

        }

    };

    const orderBadge = (status) => {

        switch (status) {

            case "CREATED":
                return "bg-secondary";

            case "ASSIGNED":
                return "bg-info";

            case "PICKED_UP":
                return "bg-warning text-dark";

            case "IN_TRANSIT":
                return "bg-primary";

            case "DELIVERED":
                return "bg-success";

            case "CANCELLED":
                return "bg-danger";

            default:
                return "bg-dark";

        }

    };

    const paymentBadge = (status) => {

        switch (status) {

            case "SUCCESS":
                return "bg-success";

            case "PENDING":
                return "bg-warning text-dark";

            case "FAILED":
                return "bg-danger";

            default:
                return "bg-secondary";

        }

    };

    if (loading) {

        return (

            <DashboardLayout>

                <div className="container mt-4">

                    <LoadingSpinner />

                </div>

            </DashboardLayout>

        );

    }

    return (

        <DashboardLayout>

            <div className="container mt-4">

                <h3>Order Details</h3>

                <div className="card shadow mt-3">

                    <div className="card-body">

                        <table className="table table-bordered">

                            <tbody>

                                <tr>
                                    <th>Tracking Number</th>
                                    <td>{order.trackingNumber}</td>
                                </tr>

                                <tr>
                                    <th>Customer ID</th>
                                    <td>{order.customerId}</td>
                                </tr>

                                <tr>
                                    <th>Receiver Name</th>
                                    <td>{order.receiverName}</td>
                                </tr>

                                <tr>
                                    <th>Receiver Phone</th>
                                    <td>{order.receiverPhone}</td>
                                </tr>

                                <tr>
                                    <th>Pickup Address</th>
                                    <td>{order.pickupAddress}</td>
                                </tr>

                                <tr>
                                    <th>Delivery Address</th>
                                    <td>{order.deliveryAddress}</td>
                                </tr>

                                <tr>
                                    <th>Package Type</th>
                                    <td>{order.packageType}</td>
                                </tr>

                                <tr>
                                    <th>Weight</th>
                                    <td>{order.weight} kg</td>
                                </tr>

                                <tr>
                                    <th>Amount</th>
                                    <td>₹ {order.amount}</td>
                                </tr>

                                <tr>

                                    <th>Payment Status</th>

                                    <td>

                                        <span className={`badge ${paymentBadge(order.paymentStatus)}`}>

                                            {order.paymentStatus}

                                        </span>

                                    </td>

                                </tr>

                                <tr>

                                    <th>Order Status</th>

                                    <td>

                                        <span className={`badge ${orderBadge(order.orderStatus)}`}>

                                            {order.orderStatus}

                                        </span>

                                    </td>

                                </tr>

                                <tr>
                                    <th>Assigned Agent</th>
                                    <td>{order.assignedAgentId || "Not Assigned"}</td>
                                </tr>

                                <tr>
                                    <th>Created At</th>
                                    <td>{order.createdAt}</td>
                                </tr>

                            </tbody>

                        </table>

                    </div>

                </div>

            </div>

        </DashboardLayout>

    );

}

export default OrderDetails;