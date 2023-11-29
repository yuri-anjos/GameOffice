import { useEffect, useState } from "react";
import useApi from "../../../hook/useApi";
import { Link, useNavigate, useParams } from "react-router-dom";

function Game() {
	const { findGame, getImage } = useApi();
	const { gameId } = useParams();
	const [game, setGame] = useState({});
	const [image, setImage] = useState("");

	useEffect(() => {
		async function fetchData() {
			findGame(gameId).then((data) => {
				setGame(data);
			});
			getImage(gameId).then((data) => {
				setImage(data);
			});
		}

		if (gameId) {
			fetchData();
		}
	}, [gameId]);

	return (
		<section>
			<span>{game.name}</span>
			{image && <img src={`data:;base64,${image}`} alt={`${game.name}-cover`} />}

			<Link to={`/game/${game.id}/update`}>
				<button type="button">Editar</button>
			</Link>
		</section>
	);
}

export default Game;
