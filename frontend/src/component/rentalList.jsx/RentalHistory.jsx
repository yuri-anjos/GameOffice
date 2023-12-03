import { useEffect, useState } from "react";
import css from "./RentalList.module.css";
import useApi from "../../hook/useApi";
import Pagination from "../pagination/Pagination";
import RentalGameTable from "../rentalGame/RentalGameTable";

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
			setRentalGames(data);
		});
	}

	function handlePaginationChange(val) {
		setPage(val);
		fetchData();
	}

	return (
		<>
			<RentalGameTable data={rentalGames.content} updateData={fetchData} />
			<Pagination
				page={page}
				totalPages={rentalGames.totalPages}
				handleChange={handlePaginationChange}
			/>
		</>
	);
}
export default RentalHistory;
