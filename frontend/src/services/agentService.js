import api from "../api/axios";

export const getAssignedOrders = () => {
    return api.get("/api/v1/agent/orders");
};

export const updateOrderStatus = (orderId, status) => {
    return api.patch(
        `/api/v1/agent/orders/${orderId}/status`,
        {
            orderStatus: status
        }
    );
};