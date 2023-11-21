import { useEffect, useState } from "react";
import useApi from "../../../hook/useApi";
import { useParams } from "react-router-dom";

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
				console.log(data);
				setImage(data);
			});
		}

		if (gameId) {
			fetchData();
		}
	}, [gameId]);

	return (
		<section>
			<div>{game.name}</div>
			<img src={`data:;base64,${image}`} alt={`${game.name}-cover`} />
		</section>
	);
}

export default Game;
