import api from "../api/axios";

export const createOrder = (order) => {
    return api.post("/api/v1/orders", order);
};

export const getMyOrders = () => {
    return api.get("/api/v1/orders");
};

export const getOrderById = (orderId) => {
    return api.get(`/api/v1/orders/${orderId}`);
};

export const trackOrder = (trackingNumber) => {
    return api.get(`/api/v1/orders/tracking/${trackingNumber}`);
};

export const cancelOrder = (orderId) => {
    return api.patch(`/api/v1/orders/${orderId}/cancel`);
};