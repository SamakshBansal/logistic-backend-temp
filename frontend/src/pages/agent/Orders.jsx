import { useEffect, useState } from "react";
import DashboardLayout from "../../layouts/DashboardLayout";
import {
    getAssignedOrders,
    updateOrderStatus
} from "../../services/agentService";
import LoadingSpinner from "../../components/LoadingSpinner";


function Orders() {

    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);
    const [selectedStatus, setSelectedStatus] = useState({});

    useEffect(() => {
        loadOrders();
    }, []);

    const loadOrders = async () => {

        try {

            const response = await getAssignedOrders();

            setOrders(response.data.data);

            const statusMap = {};

            response.data.data.forEach(order => {
                statusMap[order.id] = order.orderStatus;
            });

            setSelectedStatus(statusMap);

        } catch (error) {

            alert(
                error.response?.data?.message ||
                "Failed to load orders"
            );

        } finally {

            setLoading(false);

        }

    };

    const changeStatus = async (orderId) => {

        try {

            await updateOrderStatus(
                orderId,
                selectedStatus[orderId]
            );

            alert("Status updated successfully");

            loadOrders();

        } catch (error) {

            alert(
                error.response?.data?.message ||
                "Failed to update status"
            );

        }

    };

    const badgeClass = (status) => {

        switch (status) {

            case "ASSIGNED":
                return "bg-secondary";

            case "PICKED_UP":
                return "bg-warning text-dark";

            case "IN_TRANSIT":
                return "bg-primary";

            case "DELIVERED":
                return "bg-success";

            default:
                return "bg-dark";

        }

    };

    return (

        <DashboardLayout>

            <div className="container mt-4">

                <h3>Assigned Orders</h3>

                {loading ? (

                    <LoadingSpinner />

                ) : (

                    <table className="table table-bordered table-hover mt-3">

                        <thead className="table-dark">

                            <tr>

                                <th>Tracking</th>
                                <th>Receiver</th>
                                <th>Delivery Address</th>
                                <th>Current Status</th>
                                <th>New Status</th>
                                <th>Action</th>

                            </tr>

                        </thead>

                        <tbody>

                            {orders.length === 0 ? (

                                <tr>

                                    <td
                                        colSpan="6"
                                        className="text-center"
                                    >
                                        No Assigned Orders
                                    </td>

                                </tr>

                            ) : (

                                orders.map((order) => (

                                    <tr key={order.id}>

                                        <td>{order.trackingNumber}</td>

                                        <td>{order.receiverName}</td>

                                        <td>{order.deliveryAddress}</td>

                                        <td>

                                            <span className={`badge ${badgeClass(order.orderStatus)}`}>
                                                {order.orderStatus}
                                            </span>

                                        </td>

                                        <td>

                                            <select
                                                className="form-select"
                                                value={selectedStatus[order.id]}
                                                onChange={(e) =>
                                                    setSelectedStatus({
                                                        ...selectedStatus,
                                                        [order.id]: e.target.value
                                                    })
                                                }
                                            >

                                                <option value="ASSIGNED">
                                                    ASSIGNED
                                                </option>

                                                <option value="PICKED_UP">
                                                    PICKED UP
                                                </option>

                                                <option value="IN_TRANSIT">
                                                    IN TRANSIT
                                                </option>

                                                <option value="DELIVERED">
                                                    DELIVERED
                                                </option>

                                            </select>

                                        </td>

                                        <td>

                                            <button
                                                className="btn btn-success btn-sm"
                                                onClick={() => changeStatus(order.id)}
                                            >
                                                Update
                                            </button>

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

export default Orders;