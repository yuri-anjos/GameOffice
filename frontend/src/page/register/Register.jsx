import css from "./UserForm.module.css";
import { useContext, useState } from "react";
import { Input } from "../../component";
import { useNavigate } from "react-router-dom";
import { UserContext } from "../../context/UserContext";

function Register() {
	const { register } = useContext(UserContext);
	const navigate = useNavigate();
	const [form, setForm] = useState({});

	function submitSignup(e) {
		e.preventDefault();
		register(form).then(() => {
			navigate("/");
		});
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
					name="name"
					placeholder="Digite o seu nome..."
					text="Nome"
					type="text"
					value={form.name}
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

				<Input
					name="confirmPassword"
					placeholder="Digite a sua confirmação de senha..."
					text="Confirmação de Senha"
					type="password"
					value={form.confirmPassword}
					handleOnChange={handleFormChange}
				/>
				<button type="submit" onClick={submitSignup}>
					Cadastrar
				</button>
			</form>
		</section>
	);
}

export default Register;
