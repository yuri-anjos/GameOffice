import css from "./Login.module.css";
import { useContext, useState } from "react";
import { Input } from "../../component";
import { useNavigate } from "react-router-dom";
import { UserContext } from "../../context/UserContext";

function Login() {
	const { login } = useContext(UserContext);
	const navigate = useNavigate();
	const [form, setForm] = useState({});

	function submitLogin(e) {
		e.preventDefault();
		login(form)
			.then(() => {
				navigate("/");
			})
			.catch(() => {});
	}

	function handleFormChange(e) {
		setForm({ ...form, [e.target.name]: e.target.value });
	}

	return (
		<section>
			<form>
				<Input
					name="email"
					placeholder="Digite o seu e-mail..."
					text="Email"
					type="email"
					value={form.email}
					handleOnChange={handleFormChange}
				/>

				<Input
					name="password"
					placeholder="Digite a sua senha..."
					text="Senha"
					type="password"
					value={form.password}
					handleOnChange={handleFormChange}
				/>

				<button type="submit" onClick={submitLogin}>
					Entrar
				</button>
			</form>
		</section>
	);
}

export default Login;
