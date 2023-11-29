import { useEffect, useState } from "react";
import useApi from "../../../hook/useApi";
import css from "./User.module.css";
import { useParams } from "react-router-dom";
import { RentalHistory, RentalList } from "../../../component";

function User() {
	const { findUser } = useApi();
	const { userId } = useParams();
	const [customer, setCustomer] = useState({});

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

	return (
		<section>
			<div>{customer?.id ? <div>{customer.name}</div> : ""}</div>
			<RentalList userId={customer.id} />
			<RentalHistory userId={customer.id} />
		</section>
	);
}

export default User;
