import GameForm from "./GameForm";

function CreateGame() {
	const onSubmit = (form) => {};

	return (
		<section>
			<GameForm onSubmit={onSubmit} submitText="Cadastrar" />
		</section>
	);
}

export default CreateGame;
