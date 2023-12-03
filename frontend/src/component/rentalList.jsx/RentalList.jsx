import { useEffect, useState } from "react";
import css from "./RentalList.module.css";
import useApi from "../../hook/useApi";
import RentalGameTable from "../rentalGame/RentalGameTable";

function RentalList({ userId }) {
	const { getActiveRentalGames } = useApi(0);
	const [rentalGames, setRentalGames] = useState([]);

	useEffect(() => {
		if (userId) {
			fetchData();
		}
	}, [userId]);

	async function fetchData() {
		getActiveRentalGames(userId).then((data) => {
			setRentalGames(data);
		});
	}

	return <RentalGameTable data={rentalGames} updateData={fetchData} adminMode={true} />;
}

export default RentalList;
