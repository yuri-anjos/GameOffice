import { useEffect, useState } from "react";
import { Input, MultipleSelect } from "../../../component";
import useApi from "../../../hook/useApi";

function GameForm({ gameData, onSubmit, submitText }) {
	const { getGenres, getConsoles } = useApi();
	const [game, setGame] = useState(gameData || {});
	const [genres, setGenres] = useState([]);
	const [consoles, setConsoles] = useState([]);

	useEffect(() => {
		async function fetchData() {
			getConsoles().then((data) => setConsoles(data));
			getGenres().then((data) => setGenres(data));
		}

		fetchData();
	}, []);

	function handleFormChange(e) {
		setGame({ ...game, [e.target.name]: e.target.value });
	}

	const submit = (e) => {
		e.preventDefault();
		onSubmit(game);
	};

	return (
		<form>
			<Input
				placeholder="Nome..."
				name="name"
				text="Nome do Jogo"
				value={game.name}
				type="text"
				handleOnChange={handleFormChange}
			/>

			<Input
				placeholder="20xx"
				name="year"
				text="Ano"
				value={game.year}
				type="number"
				handleOnChange={handleFormChange}
			/>

			<Input
				placeholder="100.00"
				name="price"
				text="Preço"
				value={game.price}
				type="number"
				handleOnChange={handleFormChange}
				min={0}
				max={1000}
				step={0.01}
			/>

			<Input
				placeholder="0..."
				name="totalUnits"
				text="Total de unidades"
				value={game.totalUnits}
				type="number"
				handleOnChange={handleFormChange}
				min={0}
			/>

			<Input
				placeholder="100..."
				name="hoursLength"
				text="Tempo de jogo"
				value={game.hoursLength}
				type="number"
				handleOnChange={handleFormChange}
				min={0}
				step={1}
			/>

			<MultipleSelect
				text="Gêneros"
				options={genres}
				handleOnChange={(val) => {
					setGame({ ...game, genres: val });
				}}
				value={game.genres || []}
			/>

			<MultipleSelect
				text="Console"
				options={consoles}
				handleOnChange={(val) => {
					setGame({ ...game, console: val });
				}}
				value={game.console || []}
			/>

			<button type="submit" onClick={submit}>
				{submitText}
			</button>
		</form>
	);
}

export default GameForm;
