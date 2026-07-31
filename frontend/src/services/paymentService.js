import api from "../api/axios";

export const createPayment = (orderId) => {

    return api.post("/api/v1/payments", {

        orderId

    });

};

export const verifyPayment = (paymentData) => {

    return api.post(

        "/api/v1/payments/verify",

        paymentData

    );

};