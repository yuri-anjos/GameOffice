import { useEffect, useState } from "react";
import css from "./RentalList.module.css";
import useApi from "../../hook/useApi";
import RentalGameTable from "../rentalGame/RentalGameTable";
import Pagination from "../pagination/Pagination";

function RentalList({ userId }) {
	const { getActiveRentalGames, getRentalGames } = useApi(0);
	const [rentalList, setRentalList] = useState([]);
	const [rentalHistory, setRentalHistory] = useState({ content: [], totalPages: 0 });
	const [historyPage, setHistoryPage] = useState(1);

	useEffect(() => {
		if (userId) {
			fetchData();
		}
	}, [userId]);

	async function fetchData() {
		getActiveRentalGames(userId).then((data) => {
			setRentalList(data);
		});
		getRentalGames(userId, historyPage, null).then((data) => {
			setRentalHistory(data);
		});
	}

	function handlePaginationChange(val) {
		setHistoryPage(val);
		fetchData();
	}

	return (
		<>
			{rentalList.length ? (
				<RentalGameTable data={rentalList} updateData={fetchData} adminMode={true} />
			) : (
				""
			)}

			{rentalHistory.content.length ? (
				<>
					<RentalGameTable data={rentalHistory.content} updateData={fetchData} />
					<Pagination
						page={historyPage}
						totalPages={rentalHistory.totalPages}
						handleChange={handlePaginationChange}
					/>
				</>
			) : (
				""
			)}
		</>
	);
}

export default RentalList;
