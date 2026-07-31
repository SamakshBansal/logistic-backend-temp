import api from "../api/axios";

export const getDashboard = () => {
    return api.get("/api/v1/admin/dashboard");
};

export const getOrderById = (orderId) => {
    return api.get(`/api/v1/admin/orders/${orderId}`);
};


export const getAllOrders = () => {
    return api.get("/api/v1/admin/orders");
};

// export const getOrder = (id) => {
//     return api.get(`/api/v1/admin/orders/${id}`);
// };

export const getAllAgents = () => {
    return api.get("/api/v1/admin/users/agents");
};

export const assignAgent = (orderId, agentId) => {
    return api.patch(`/api/v1/orders/${orderId}/assign`, {
        agentId
    });
};

// Register Delivery Agent
export const registerAgent = (data) => {
    return api.post("/api/v1/admin/users/agents", data);
};

// Register Admin
export const registerAdmin = (data) => {
    return api.post("/api/v1/admin/users/admins", data);
};