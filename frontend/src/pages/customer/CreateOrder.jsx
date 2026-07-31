import { useState } from "react";
import { createOrder } from "../../services/orderService";
import DashboardLayout from "../../layouts/DashboardLayout";

function CreateOrder() {

    const [formData, setFormData] = useState({
        pickupAddress: "",
        deliveryAddress: "",
        receiverName: "",
        receiverPhone: "",
        packageType: "DOCUMENT",
        weight: ""
    });

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {

        e.preventDefault();

        try {

            const response = await createOrder(formData);

            alert(response.data.message);

            setFormData({
                pickupAddress: "",
                deliveryAddress: "",
                receiverName: "",
                receiverPhone: "",
                packageType: "DOCUMENT",
                weight: ""
            });

        } catch (error) {

            alert(
                error.response?.data?.message ||
                "Failed to create order"
            );

        }

    };

    return (
        <DashboardLayout>

            {

                <div className="container mt-4">

                    <h3>Create Order</h3>

                    <form onSubmit={handleSubmit}>

                        <div className="mb-3">
                            <label>Pickup Address</label>

                            <input
                                className="form-control"
                                name="pickupAddress"
                                value={formData.pickupAddress}
                                onChange={handleChange}
                                required
                            />
                        </div>

                        <div className="mb-3">
                            <label>Delivery Address</label>

                            <input
                                className="form-control"
                                name="deliveryAddress"
                                value={formData.deliveryAddress}
                                onChange={handleChange}
                                required
                            />
                        </div>

                        <div className="mb-3">
                            <label>Receiver Name</label>

                            <input
                                className="form-control"
                                name="receiverName"
                                value={formData.receiverName}
                                onChange={handleChange}
                                required
                            />
                        </div>

                        <div className="mb-3">
                            <label>Receiver Phone</label>

                            <input
                                className="form-control"
                                name="receiverPhone"
                                value={formData.receiverPhone}
                                onChange={handleChange}
                                required
                            />
                        </div>

                        <div className="mb-3">

                            <label>Package Type</label>

                            <select
                                className="form-select"
                                name="packageType"
                                value={formData.packageType}
                                onChange={handleChange}
                            >
                                <option value="">Select Package Type</option>
                                <option value="DOCUMENT">Document</option>
                                <option value="PARCEL">Parcel</option>
                                <option value="ELECTRONICS">Electronics</option>
                                <option value="CLOTHING">Clothing</option>
                                <option value="FOOD">Food</option>
                                <option value="OTHER">Other</option>
                            </select>

                        </div>

                        <div className="mb-3">

                            <label>Weight (kg)</label>

                            <input
                                type="number"
                                step="0.1"
                                className="form-control"
                                name="weight"
                                value={formData.weight}
                                onChange={handleChange}
                                required
                            />

                        </div>

                        <button className="btn btn-primary">

                            Create Order

                        </button>

                    </form>

                </div>

            } </DashboardLayout>);

}

export default CreateOrder;