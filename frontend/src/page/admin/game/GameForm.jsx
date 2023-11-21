import { useState } from "react";

function GameForm({ gameData, onSubmit, submitText }) {
	const [game, setGame] = useState(gameData || {});

	function handleFormChange(e) {
		setGame({ ...game, [e.target.name]: e.target.value });
	}
	const submit = (e) => {
		e.preventDefault();
		onSubmit(game);
	};

	return (
		<form>
			<button type="submit" onClick={submit}>
				{submitText}
			</button>
		</form>
	);
}

export default GameForm;
