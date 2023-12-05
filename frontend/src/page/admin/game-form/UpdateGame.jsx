import { useEffect, useState } from "react";
import GameForm from "./GameForm";
import useApi from "../../../hook/useApi";
import { useNavigate, useParams } from "react-router-dom";

function UpdateGame() {
	const navigate = useNavigate();
	const { findGame, updateGame } = useApi();
	const { gameId } = useParams();
	const [game, setGame] = useState({});

	useEffect(() => {
		async function getData() {
			findGame(gameId).then((data) => {
				setGame({ ...data, console: [data.console] });
			});
		}

		getData();
	}, [gameId]);

	const onSubmit = (form) => {
		updateGame(form, game.id).then(() => {
			navigate(`/game/${game.id}`);
		});
	};

	return (
		<section>
			{game.id && <GameForm gameData={game} onSubmit={onSubmit} submitText="Salvar" />}
		</section>
	);
}

export default UpdateGame;
