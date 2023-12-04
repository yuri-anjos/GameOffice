import { useEffect, useRef, useState } from "react";
import css from "./InputSearch.module.css";
import useDebounce from "../../hook/useDebounce";
import { ClipLoader } from "react-spinners";

function InputSearch({ getData, handleSelected }) {
	const refInputUserSearch = useRef(null);

	const [search, setSearch] = useState("");
	const [result, setResult] = useState([]);
	const [visibleOptions, setVisibleOptions] = useState(false);
	const [loading, setLoading] = useState(false);
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
		if (!search || search === debouncedSearch) {
			setLoading(false);
		}
	}, [search]);

	useEffect(() => {
		async function fetchData() {
			getData(debouncedSearch)
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

	function handleSearch(option) {
		handleSelected(option);
		setVisibleOptions(false);
		setSearch("");
	}

	return (
		<div className={css.input_user_search} ref={refInputUserSearch}>
			<input
				type="text"
				placeholder="Buscar..."
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
							<button type="button" key={i.id} onClick={() => handleSearch(i)}>
								{i.description}
							</button>
						);
					})}
			</div>
		</div>
	);
}

export default InputSearch;
