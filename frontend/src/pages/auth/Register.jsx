import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import authService from "../../services/authService";


function Register() {

    const navigate = useNavigate();


    const [form, setForm] = useState({

        name: "",
        email: "",
        password: "",
        role: "CUSTOMER"

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


            await authService.register(form);


            toast.success(
                "Registration successful. Please login."
            );


            navigate("/login");


        }
        catch (error) {


            toast.error(

                error.response?.data?.message
                ||
                "Registration failed"

            );


        }
        finally {

            setLoading(false);

        }

    };



    return (

        <div className="container mt-5">


            <div className="row justify-content-center">


                <div className="col-md-5">


                    <div className="card shadow">


                        <div className="card-body">


                            <h3 className="text-center mb-4">

                                Create Account

                            </h3>



                            <form onSubmit={handleSubmit}>


                                <div className="mb-3">

                                    <label>Name</label>

                                    <input

                                        type="text"

                                        className="form-control"

                                        name="name"

                                        value={form.name}

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





                                <div className="mb-3">


                                    <label>Role</label>


                                    <select

                                        className="form-control"

                                        name="role"

                                        value={form.role}

                                        onChange={handleChange}


                                    >


                                        <option value="CUSTOMER">

                                            Customer

                                        </option>


                                    </select>


                                </div>





                                <button

                                    className="btn btn-primary w-100"

                                    disabled={loading}


                                >

                                    {

                                        loading

                                            ?

                                            "Creating Account..."

                                            :

                                            "Register"

                                    }


                                </button>



                            </form>




                            <div className="text-center mt-3">


                                <span>

                                    Already have an account?

                                </span>


                                {" "}


                                <Link to="/login">

                                    Login

                                </Link>


                            </div>



                        </div>


                    </div>


                </div>


            </div>


        </div>

    );

}


export default Register;