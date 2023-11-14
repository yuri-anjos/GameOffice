import css from "./UserForm.module.css";
import { useContext, useEffect, useState } from "react";
import { Input } from "../../component";
import { useNavigate } from "react-router-dom";
import useApi from "../../hook/useApi";
import { UserContext } from "../../context/UserContext";

function Profile() {
	const { updateUser } = useApi();
	const { user, refreshUser } = useContext(UserContext);

	const navigate = useNavigate();
	const [form, setForm] = useState(user);

	useEffect(() => {
		setForm(user);
	}, [user]);

	function submitUpdate(e) {
		e.preventDefault();

		updateUser(form).then(() => {
			refreshUser();
			navigate("/user/profile");
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
					value={form?.email}
					handleOnChange={handleFormChange}
				/>

				<Input
					name="name"
					placeholder="Digite o seu nome..."
					text="Nome"
					type="text"
					value={form?.name}
					handleOnChange={handleFormChange}
				/>

				<Input
					name="password"
					placeholder="Digite a sua senha..."
					text="Senha"
					type="password"
					value={form?.password}
					handleOnChange={handleFormChange}
				/>

				<Input
					name="confirmPassword"
					placeholder="Digite a sua confirmação de senha..."
					text="Confirmação de Senha"
					type="password"
					value={form?.confirmPassword}
					handleOnChange={handleFormChange}
				/>
				<button type="submit" onClick={submitUpdate}>
					Salvar
				</button>
			</form>
		</section>
	);
}

export default Profile;
