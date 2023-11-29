import { useEffect, useState } from "react";
import css from "./RentalList.module.css";
import useApi from "../../hook/useApi";
import RentalGame from "../rentalGame/RentalGame";
import Pagination from "../pagination/Pagination";

function RentalHistory({ userId }) {
	const { getRentalGames } = useApi();
	const [rentalGames, setRentalGames] = useState({ content: [], totalPages: 0 });
	const [page, setPage] = useState(1);

	useEffect(() => {
		if (userId) {
			fetchData();
		}
	}, [userId]);

	async function fetchData() {
		getRentalGames(userId, page, null).then((data) => {
			console.log(data.content);
			setRentalGames(data);
		});
	}

	function handlePaginationChange(val) {
		setPage(val);
		fetchData();
	}

	return (
		<div>
			{rentalGames.content.map((i, index) => (
				<RentalGame key={index} rentalGame={i} adminMode={true} />
			))}
			<Pagination
				page={page}
				totalPages={rentalGames.totalPages}
				handleChange={handlePaginationChange}
			/>
		</div>
	);
}
export default RentalHistory;
