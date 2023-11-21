import { useEffect, useState } from "react";
import GameForm from "./GameForm";
import useApi from "../../../hook/useApi";
import { useParams } from "react-router-dom";

function UpdateGame() {
	const { findGame } = useApi();
	const { gameId } = useParams();
	const [game, setGame] = useState({});

	useEffect(() => {
		getData();
	}, [gameId]);

	async function getData() {
		findGame(gameId).then((data) => {
			setGame(data);
		});
	}

	const onSubmit = (form) => {};

	return (
		<section>
			<GameForm gameData={game} onSubmit={onSubmit} submitText="Salvar" />
		</section>
	);
}

export default UpdateGame;
