import { useState } from "react";
import DashboardLayout from "../../layouts/DashboardLayout";
import { registerAdmin } from "../../services/adminService";

function RegisterAdmin() {

    const [formData, setFormData] = useState({
        name: "",
        email: "",
        password: ""
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

            const response = await registerAdmin(formData);

            alert(response.data.message);

            setFormData({
                name: "",
                email: "",
                password: ""
            });

        } catch (error) {

            alert(
                error.response?.data?.message ||
                "Failed to register admin"
            );

        }

    };

    return (

        <DashboardLayout>

            <div className="container mt-4">

                <div className="card shadow">

                    <div className="card-body">

                        <h3 className="mb-4">
                            Register Admin
                        </h3>

                        <form onSubmit={handleSubmit}>

                            <div className="mb-3">

                                <label>Name</label>

                                <input
                                    className="form-control"
                                    name="name"
                                    value={formData.name}
                                    onChange={handleChange}
                                    required
                                />

                            </div>

                            <div className="mb-3">

                                <label>Email</label>

                                <input
                                    type="email"
                                    className="form-control"
                                    name="email"
                                    value={formData.email}
                                    onChange={handleChange}
                                    required
                                />

                            </div>

                            <div className="mb-3">

                                <label>Password</label>

                                <input
                                    type="password"
                                    className="form-control"
                                    name="password"
                                    value={formData.password}
                                    onChange={handleChange}
                                    required
                                />

                            </div>

                            <button
                                className="btn btn-primary"
                            >
                                Register Admin
                            </button>

                        </form>

                    </div>

                </div>

            </div>

        </DashboardLayout>

    );

}

export default RegisterAdmin;