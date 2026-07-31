import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import DashboardLayout from "../../layouts/DashboardLayout";
import { getOrderById } from "../../services/orderService";
import {
    createPayment,
    verifyPayment
} from "../../services/paymentService";

function OrderDetails() {

    const { orderId } = useParams();

    const [order, setOrder] = useState(null);

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

        }

    };

    if (!order) {

        return (
            <DashboardLayout>
                <div className="container mt-4">
                    Loading...
                </div>
            </DashboardLayout>
        );

    }

    const handlePayment = async () => {

        try {

            const response = await createPayment(order.id);

            const payment = response.data.data;

            console.log(payment);

            const options = {

                key: payment.razorpayKey,

                amount: payment.amount * 100,

                currency: payment.currency,

                name: "Logistics Management System",

                description: "Order Payment",

                order_id: payment.gatewayOrderId,

                handler: async function (razorpayResponse) {

                    try {

                        await verifyPayment({

                            gatewayOrderId: razorpayResponse.razorpay_order_id,

                            gatewayPaymentId: razorpayResponse.razorpay_payment_id,

                            signature: razorpayResponse.razorpay_signature

                        });

                        alert("Payment Successful");

                        loadOrder();

                    } catch (error) {

                        alert("Payment Verification Failed");

                    }

                },

                theme: {

                    color: "#0d6efd"

                }

            };

            const razorpay = new window.Razorpay(options);

            razorpay.open();

        } catch (error) {

            alert(

                error.response?.data?.message ||

                "Payment Failed"

            );

        }

    };

    return (

        <DashboardLayout>

            <div className="container mt-4">

                <h3>Order Details</h3>

                <table className="table table-bordered">

                    <tbody>

                        <tr>
                            <th>Tracking Number</th>
                            <td>{order.trackingNumber}</td>
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
                            <th>Receiver</th>
                            <td>{order.receiverName}</td>
                        </tr>

                        <tr>
                            <th>Receiver Phone</th>
                            <td>{order.receiverPhone}</td>
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
                            <td>{order.paymentStatus}</td>
                        </tr>

                        <tr>
                            <th>Order Status</th>
                            <td>{order.orderStatus}</td>
                        </tr>

                        <tr>
                            <th>Created</th>
                            <td>{order.createdAt}</td>
                        </tr>

                    </tbody>

                </table>

                {order.paymentStatus === "PENDING" && order.orderStatus !== "CANCELLED" ? (
                    <button
                        className="btn btn-success mt-3"
                        onClick={handlePayment}
                    >
                        Pay Now
                    </button>
                ) : order.orderStatus === "CANCELLED" ? (
                    <div className="alert alert-warning mt-3">
                        This order has been cancelled. Payment is no longer accepted.
                    </div>
                ) : null}

            </div>

        </DashboardLayout>

    );

}

export default OrderDetails;