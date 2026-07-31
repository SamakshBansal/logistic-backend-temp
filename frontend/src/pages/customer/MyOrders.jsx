import { useEffect, useState } from "react";
import DashboardLayout from "../../layouts/DashboardLayout";
import LoadingSpinner from "../../components/LoadingSpinner";
import { getMyOrders, cancelOrder } from "../../services/orderService";
import { Link } from "react-router-dom";

function MyOrders() {
    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);
    const [cancellingId, setCancellingId] = useState(null);

    useEffect(() => {
        loadOrders();
    }, []);

    const loadOrders = async () => {
        try {
            const response = await getMyOrders();
            setOrders(response.data.data);
        } catch (error) {
            alert(
                error.response?.data?.message || "Failed to load orders"
            );
        } finally {
            setLoading(false);
        }
    };

    const handleCancelOrder = async (orderId) => {
        const confirmCancel = window.confirm(
            "Are you sure you want to cancel this order?"
        );
        if (!confirmCancel) return;

        setCancellingId(orderId);
        try {
            await cancelOrder(orderId);
            alert("Order cancelled successfully");
            loadOrders(); // Refresh table to show updated status
        } catch (error) {
            alert(
                error.response?.data?.message || "Failed to cancel order"
            );
        } finally {
            setCancellingId(null);
        }
    };

    return (
        <DashboardLayout>
            <div className="container mt-4">
                <h3>My Orders</h3>

                {loading ? (
                    <LoadingSpinner />
                ) : (
                    <table className="table table-bordered table-striped mt-3">
                        <thead>
                            <tr>
                                <th>Tracking No.</th>
                                <th>Receiver</th>
                                <th>Package</th>
                                <th>Status</th>
                                <th>Payment</th>
                                <th>Amount</th>
                                <th>Actions</th>
                            </tr>
                        </thead>

                        <tbody>
                            {orders.length === 0 ? (
                                <tr>
                                    <td colSpan="7" className="text-center">
                                        You haven't created any orders yet.
                                    </td>
                                </tr>
                            ) : (
                                orders.map((order) => (
                                    <tr key={order.id}>
                                        <td>{order.trackingNumber}</td>
                                        <td>{order.receiverName}</td>
                                        <td>{order.packageType}</td>
                                        <td>
                                            <span
                                                className={`badge ${order.orderStatus === "CANCELLED"
                                                    ? "bg-danger"
                                                    : order.orderStatus === "DELIVERED"
                                                        ? "bg-success"
                                                        : "bg-primary"
                                                    }`}
                                            >
                                                {order.orderStatus}
                                            </span>
                                        </td>
                                        <td>{order.paymentStatus}</td>
                                        <td>₹ {order.amount ?? 0}</td>
                                        <td>
                                            <Link
                                                to={`/customer/orders/${order.id}`}
                                                className="btn btn-sm btn-primary me-2"
                                            >
                                                View
                                            </Link>

                                            {/* Show Cancel button only for active/cancellable statuses */}
                                            {["CREATED", "ASSIGNED", "PENDING"].includes(order.orderStatus) && (
                                                <button
                                                    onClick={() => handleCancelOrder(order.id)}
                                                    className="btn btn-sm btn-outline-danger"
                                                    disabled={cancellingId === order.id}
                                                >
                                                    {cancellingId === order.id ? "Cancelling..." : "Cancel"}
                                                </button>
                                            )}
                                        </td>
                                    </tr>
                                ))
                            )}
                        </tbody>
                    </table>
                )}
            </div>
        </DashboardLayout>
    );
}

export default MyOrders;