import css from "./User.module.css";
import { useEffect, useState } from "react";
import useApi from "../../../hook/useApi";
import { useParams } from "react-router-dom";
import { InputSearch, RentalList } from "../../../component";

function User() {
	const { findUser, searchGamesCombo, findGame, rentGame } = useApi();
	const { userId } = useParams();
	const [customer, setCustomer] = useState({});
	const [selectedGame, setSelectedGame] = useState(null);

	useEffect(() => {
		async function fetchData() {
			findUser(userId).then((data) => {
				setCustomer(data);
			});
		}

		if (userId) {
			fetchData();
		}
	}, [userId]);

	async function handleSelectedUser(option) {
		findGame(option.id).then((data) => {
			setSelectedGame(data);
			console.log(data);
		});
	}

	async function handleCalculateRent() {}

	async function handleRentGame() {
		rentGame(customer.id, selectedGame.id).then(() => {
			setSelectedGame(null);
		});
	}

	return (
		<section>
			{customer?.id ? (
				<div>
					<span>
						<div>{customer.name}</div>
						<div>{customer.email}</div>
					</span>
				</div>
			) : (
				""
			)}

			<InputSearch getData={searchGamesCombo} handleSelected={handleSelectedUser} />
			{selectedGame?.id ? (
				<div>
					<div>{selectedGame.id}</div>
					<div>{selectedGame.name}</div>
					<div>{selectedGame.year}</div>
					<div>{selectedGame.console.description}</div>
					<ul></ul>
					{selectedGame.genres.map((i) => (
						<li>{i.description}</li>
					))}
					<button type="button" onClick={handleCalculateRent}>
						Calcular Aluguel
					</button>
					<button type="button" onClick={handleRentGame}>
						Alugar Jogo
					</button>
				</div>
			) : (
				""
			)}

			{customer.id ? <RentalList userId={customer.id} /> : ""}
		</section>
	);
}

export default User;
