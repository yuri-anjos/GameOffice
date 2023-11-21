import { useEffect, useState } from "react";
import useApi from "../../../hook/useApi";
import css from "./User.module.css";
import { useParams } from "react-router-dom";

function User() {
	const { findUser, getRentedGames } = useApi();
	const { userId } = useParams();
	const [customer, setCustomer] = useState({});
	const [rentedGames, setRentedGames] = useState([]);

	useEffect(() => {
		async function getData() {
			findUser(userId).then((data) => {
				setCustomer(data);
			});

			getRentedGames(userId).then((data) => {
				setRentedGames(data);
			});
		}

		if (userId) {
			getData();
		}
	}, [userId]);

	return (
		<section>
			<div>{customer?.id ? <div>{customer.name}</div> : ""}</div>
			<div>
				{rentedGames.map((i) => {
					return <span>Jogo {i.name}</span>;
				})}
			</div>
		</section>
	);
}

export default User;
