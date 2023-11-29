import { useContext, useEffect, useRef, useState } from "react";
import css from "./InputUserSearch.module.css";
import useApi from "../../hook/useApi";
import { useNavigate } from "react-router-dom";
import useDebounce from "../../hook/useDebounce";
import { ClipLoader } from "react-spinners";
import { UserContext } from "../../context/UserContext";

function InputUserSearch() {
	const refInputUserSearch = useRef(null);
	const [search, setSearch] = useState("");
	const [result, setResult] = useState([]);
	const [visibleOptions, setVisibleOptions] = useState(false);
	const [loading, setLoading] = useState(false);

	const navigate = useNavigate();
	const { searchUsers } = useApi();
	const { isAuthenticated, user } = useContext(UserContext);
	const debouncedSearch = useDebounce(search, 700);

	useEffect(() => {
		function handleClickOutside(e) {
			if (!refInputUserSearch.current?.contains(e.target)) {
				setVisibleOptions(false);
			} else {
				setVisibleOptions(true);
			}
		}

		document.addEventListener("click", handleClickOutside, true);
	}, []);

	useEffect(() => {
		setLoading(true);
		if (!search) {
			setResult([]);
			setLoading(false);
		}
	}, [search]);

	useEffect(() => {
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

	function handleSelectedUser(id) {
		navigate(`/user/${id}`);
		setSearch("");
		setResult([]);
	}

	return (
		<div className={css.input_user_search} ref={refInputUserSearch}>
			<input
				type="text"
				placeholder="Buscar usuários"
				value={search}
				onChange={(e) => setSearch(e.target.value)}
			/>

			<ClipLoader
				color={"black"}
				loading={loading}
				size={20}
				aria-label="Loading Spinner"
				data-testid="loader"
				className={css.loading}
			/>

			<div className={`${css.results} ${!visibleOptions && "d_none"}`}>
				{result?.length > 0 &&
					result.map((i) => {
						return (
							<button
								type="button"
								key={i.id}
								onClick={() => handleSelectedUser(i.id)}
							>
								{i.description}
							</button>
						);
					})}
			</div>
		</div>
	);
}

export default InputUserSearch;
