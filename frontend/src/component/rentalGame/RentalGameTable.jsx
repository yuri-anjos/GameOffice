import moment from "moment/moment";
import useApi from "../../hook/useApi";
import useFlashMessage from "../../hook/useFlashMessage";
import css from "./RentalGameTable.module.css";

function RentalGameTable({ data, updateData, adminMode = false }) {
	const { setFlashMessage } = useFlashMessage();
	const { returnRentalGame } = useApi();

	async function handleReturnGame(rentalGame) {
		returnRentalGame(rentalGame.id).then(({ rent, payment, returnValue }) => {
			const message = `Jogo devolvido com sucesso! Deve ser estorna um valor de R$${returnValue} ao ${rentalGame.user.description}!`;
			const type = "success";
			setFlashMessage(message, type, true);
			updateData();
		});
	}

	return (
		<table className={css.rental_table}>
			<thead>
				<tr>
					<th>#</th>
					<th>Jogo</th>
					<th>Garantia</th>
					<th>Data Aluguel</th>
					<th>Admin Aluguel</th>
					<th>Pagamento</th>
					<th>Data Retorno</th>
					<th>Admin Retorno</th>
					{adminMode && <th>Ações</th>}
				</tr>
			</thead>
			<tbody>
				{data.map((i) => (
					<tr key={new Date().getTime() + i.id}>
						<td>{i.id}</td>
						<td>
							{i.game.name} ({i.game.console.description})
						</td>
						<td>R${i.rent}</td>
						<td>{moment(i.rentDate).format("DD/MM/YYYY")}</td>
						<td>{i.rentAdmin.description}</td>
						<td>{i.payment ? "R$" + i.payment : "-"}</td>
						<td>{i.returnDate ? moment(i.returnDate).format("DD/MM/YYYY") : "-"}</td>
						<td>{i.returnAdmin?.description || "-"}</td>
						{adminMode && i.active && (
							<td>
								<button type="button" onClick={() => handleReturnGame(i)}>
									Devolver
								</button>
							</td>
						)}
					</tr>
				))}
			</tbody>
		</table>
	);
}

export default RentalGameTable;
