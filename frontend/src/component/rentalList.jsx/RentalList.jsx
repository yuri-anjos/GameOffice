import { useEffect, useState } from "react";
import css from "./RentalList.module.css";
import useApi from "../../hook/useApi";
import RentalGame from "../rentalGame/RentalGame";

function RentalList({ userId }) {
	const { getActiveRentalGames } = useApi(0);
	const [rentalGames, setRentalGames] = useState([]);

	useEffect(() => {
		async function fetchData() {
			getActiveRentalGames(userId).then((data) => {
				console.log(data);
				setRentalGames(data);
			});
		}

		if (userId) {
			fetchData();
		}
	}, [userId]);

	return (
		<div>
			{rentalGames.map((i, index) => (
				<RentalGame key={new Date().getTime() + index} rentalGame={i} adminMode={true} />
			))}
		</div>
	);
}

export default RentalList;
