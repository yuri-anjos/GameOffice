import { useEffect, useState } from "react";
import css from "./InputUserSearch.module.css";
import useApi from "../../hook/useApi";
import { useNavigate } from "react-router-dom";
import useDebounce from "../../hook/useDebounce";
import { ClipLoader } from "react-spinners";

function InputUserSearch() {
	const [search, setSearch] = useState("");
	const [result, setResult] = useState([]);
	const [focus, setFocus] = useState(false);
	const [loading, setLoading] = useState(false);

	const navigate = useNavigate();
	const { searchUsers } = useApi();
	const debouncedSearch = useDebounce(search, 700);

	useEffect(() => {
		setResult([]);
		setLoading(true);
		if (!search) {
			setLoading(false);
		}
	}, [search]);

	useEffect(() => {
		setResult([]);

		async function fetchData() {
			searchUsers(debouncedSearch)
				.then((data) => {
					setResult(data);
				})
				.finally(() => {
					setLoading(false);
				});
		}

		if (debouncedSearch) {
			fetchData();
		}
	}, [debouncedSearch]);

	function onFocus() {
		setFocus(true);
	}

	function onBlur() {
		setTimeout(() => {
			setFocus(false);
		}, 100);
	}

	function handleSelectedUser(id) {
		navigate(`/user/${id}`);
		setSearch("");
		setResult([]);
	}

	return (
		<div className={css.input_user_search}>
			<input
				type="text"
				placeholder="Buscar usuários"
				value={search}
				onChange={(e) => setSearch(e.target.value)}
				onFocus={onFocus}
				onBlur={onBlur}
			/>

			<ClipLoader
				color={"black"}
				loading={loading}
				size={20}
				aria-label="Loading Spinner"
				data-testid="loader"
				className={css.loading}
			/>

			<div className={`${css.results} ${focus || "d_none"}`}>
				{result?.length > 0
					? result.map((i) => {
							return (
								<button
									type="button"
									key={i.id}
									onClick={() => handleSelectedUser(i.id)}
								>
									{i.description}
								</button>
							);
					  })
					: ""}
			</div>
		</div>
	);
}

export default InputUserSearch;
