import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import authService from "../../services/authService";
import { useAuth } from "../../context/AuthContext";
import { toast } from "react-toastify";


function Login() {

    const navigate = useNavigate();
    const { login } = useAuth();

    const [form, setForm] = useState({
        email: "",
        password: ""
    });

    const [loading, setLoading] = useState(false);

    const handleChange = (e) => {
        setForm({
            ...form,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {

        e.preventDefault();

        try {

            setLoading(true);

            const response = await authService.login(form);

            console.log("Login API Response:", response);

            const userData = response.data;

            console.log("User Data:", userData);

            login(userData);

            toast.success("Login Successful");

            const role = userData.role;

            if (role === "ADMIN") {
                navigate("/admin/dashboard");
            }
            else if (role === "CUSTOMER") {
                navigate("/customer/dashboard");
            }
            else if (role === "DELIVERY_AGENT") {
                navigate("/agent/dashboard");
            }

        } catch (error) {

            toast.error(
                error.response?.data?.message || "Login Failed"
            );

        } finally {

            setLoading(false);

        }

    };

    return (

        <div className="container mt-5">

            <div className="row justify-content-center">

                <div className="col-md-4">

                    <div className="card shadow">

                        <div className="card-body">

                            <h3 className="text-center mb-4">
                                Logistics Login
                            </h3>

                            <form onSubmit={handleSubmit}>

                                <div className="mb-3">

                                    <label>Email</label>

                                    <input
                                        type="email"
                                        className="form-control"
                                        name="email"
                                        value={form.email}
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
                                        value={form.password}
                                        onChange={handleChange}
                                        required
                                    />

                                </div>

                                <button
                                    className="btn btn-primary w-100"
                                    disabled={loading}
                                >

                                    {loading ? "Logging in..." : "Login"}

                                </button>

                            </form>

                            <div className="text-center mt-3">
                                <span>
                                    Don't have an account?
                                </span>
                                {" "}
                                <Link to="/register">
                                    Register
                                </Link>

                            </div>

                        </div>

                    </div>

                </div>

            </div>

        </div>

    );

}

export default Login;