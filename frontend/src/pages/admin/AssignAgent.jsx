import { useEffect, useState } from "react";
import DashboardLayout from "../../layouts/DashboardLayout";
import {
    getAllOrders,
    getAllAgents,
    assignAgent
} from "../../services/adminService";

function AssignAgent() {

    const [orders, setOrders] = useState([]);
    const [agents, setAgents] = useState([]);
    const [selectedAgents, setSelectedAgents] = useState({});

    useEffect(() => {
        loadData();
    }, []);

    const loadData = async () => {

        try {

            const orderResponse = await getAllOrders();
            const agentResponse = await getAllAgents();

            setOrders(orderResponse.data.data);
            setAgents(agentResponse.data.data);

        } catch (error) {

            alert("Failed to load data");

        }

    };

    const handleAssign = async (orderId) => {

        const agentId = selectedAgents[orderId];

        if (!agentId) {

            alert("Please select an agent");
            return;

        }

        try {

            await assignAgent(orderId, agentId);

            alert("Agent assigned successfully");

            loadData();

        } catch (error) {

            alert(
                error.response?.data?.message ||
                "Failed to assign agent"
            );

        }

    };

    return (

        <DashboardLayout>

            <div className="container mt-4">

                <h3>Assign Delivery Agent</h3>

                <table className="table table-bordered table-striped mt-3">

                    <thead>

                        <tr>

                            <th>Tracking</th>
                            <th>Receiver</th>
                            <th>Status</th>
                            <th>Assign Agent</th>
                            <th>Action</th>

                        </tr>

                    </thead>

                    <tbody>

                        {orders.map(order => (

                            <tr key={order.id}>

                                <td>{order.trackingNumber}</td>

                                <td>{order.receiverName}</td>

                                <td>{order.orderStatus}</td>

                                <td>

                                    <select

                                        className="form-select"

                                        value={selectedAgents[order.id] || ""}

                                        onChange={(e) =>
                                            setSelectedAgents({
                                                ...selectedAgents,
                                                [order.id]: e.target.value
                                            })
                                        }

                                    >

                                        <option value="">
                                            Select Agent
                                        </option>

                                        {agents.map(agent => (

                                            <option
                                                key={agent.id}
                                                value={agent.id}
                                            >
                                                {agent.name}
                                            </option>

                                        ))}

                                    </select>

                                </td>

                                <td>

                                    <button

                                        className="btn btn-success"

                                        onClick={() =>
                                            handleAssign(order.id)
                                        }

                                    >

                                        Assign

                                    </button>

                                </td>

                            </tr>

                        ))}

                    </tbody>

                </table>

            </div>

        </DashboardLayout>

    );

}

export default AssignAgent;