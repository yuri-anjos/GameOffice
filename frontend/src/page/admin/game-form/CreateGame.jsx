import { useNavigate } from "react-router-dom";
import useApi from "../../../hook/useApi";
import GameForm from "./GameForm";

function CreateGame() {
	const { createGame } = useApi();
	const navigate = useNavigate();

	const onSubmit = (form) => {
		createGame(form).then((id) => {
			navigate(`/game/${id}`);
		});
	};

	return (
		<section>
			<GameForm onSubmit={onSubmit} submitText="Cadastrar" />
		</section>
	);
}

export default CreateGame;
