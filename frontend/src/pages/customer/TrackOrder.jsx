import { useState } from "react";
import DashboardLayout from "../../layouts/DashboardLayout";
import { trackOrder } from "../../services/orderService";

function TrackOrder() {

    const [trackingNumber, setTrackingNumber] = useState("");

    const [order, setOrder] = useState(null);

    const [loading, setLoading] = useState(false);

    const handleTrack = async () => {

        if (!trackingNumber.trim()) {

            alert("Please enter a tracking number");

            return;

        }

        try {

            setLoading(true);

            const response = await trackOrder(trackingNumber);

            setOrder(response.data.data);

        } catch (error) {

            setOrder(null);

            alert(
                error.response?.data?.message ||
                "Tracking number not found"
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

    return (

        <DashboardLayout>

            <div className="container mt-4">

                <h3>Track Shipment</h3>

                <div className="card shadow-sm mt-3">

                    <div className="card-body">

                        <div className="row">

                            <div className="col-md-9">

                                <input
                                    type="text"
                                    className="form-control"
                                    placeholder="Enter Tracking Number"
                                    value={trackingNumber}
                                    onChange={(e) =>
                                        setTrackingNumber(e.target.value)
                                    }
                                />

                            </div>

                            <div className="col-md-3">

                                <button
                                    className="btn btn-primary w-100"
                                    onClick={handleTrack}
                                    disabled={loading}
                                >

                                    {loading ? "Searching..." : "Track"}

                                </button>

                            </div>

                        </div>

                    </div>

                </div>

                {order && (

                    <div className="card shadow mt-4">

                        <div className="card-header">

                            <h5>Shipment Details</h5>

                        </div>

                        <div className="card-body">

                            <table className="table">

                                <tbody>

                                    <tr>
                                        <th>Tracking Number</th>
                                        <td>{order.trackingNumber}</td>
                                    </tr>

                                    <tr>
                                        <th>Receiver</th>
                                        <td>{order.receiverName}</td>
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

                                </tbody>

                            </table>

                        </div>

                    </div>

                )}

            </div>

        </DashboardLayout>

    );

}

export default TrackOrder;