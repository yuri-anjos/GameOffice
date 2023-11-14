import { useEffect, useState } from "react";
import { Input, Pagination, Select } from "../../component";
import useApi from "../../hook/useApi";
import css from "./Home.module.css";
import { createSearchParams, useNavigate, useParams, useSearchParams } from "react-router-dom";

const INITIAL_FILTER = {
	search: "",
	console: null,
	genres: [],
	year: null,
};

function Home() {
	const { getGames, getGenres, getConsoles } = useApi();
	const { pageParam } = useParams();
	const [searchParams] = useSearchParams();
	const navigate = useNavigate();

	const [formFilter, setFormFilter] = useState(INITIAL_FILTER);
	const [data, setData] = useState({ content: [], totalPages: 0 });
	const [page, setPage] = useState(1);
	const [genres, setGenres] = useState([]);
	const [consoles, setConsoles] = useState([]);

	useEffect(() => {
		searchCombos();
	}, []);

	useEffect(() => {
		searchGames();
	}, [pageParam, searchParams]);

	function searchCombos() {
		getConsoles().then((data) => setConsoles(data));
		getGenres().then((data) => setGenres(data));
	}

	async function searchGames() {
		const pg = !isNaN(pageParam) ? parseInt(pageParam) : 1;
		setPage(pg);

		const getGamesFilter = { page: pg, size: 3 };
		for (const entry of searchParams.entries()) {
			const [param, value] = entry;
			getGamesFilter[param] = value;
		}

		getGames(getGamesFilter).then((response) => {
			setData(response);
		});
	}

	function handleFilterChange(e) {
		setFormFilter((previous) => ({ ...previous, [e.target.name]: e.target.value }));
	}

	function handlePaginationChange(val) {
		navigate({ pathname: "/page/" + val, search: `?${createSearchParams(searchParams)}` });
	}

	function submitFilter(e) {
		e.preventDefault();
		const { search, genres, console, year } = formFilter;

		const params = {
			genres: genres,
		};
		genres && (params.genres = genres);
		search && (params.search = search);
		console && (params.console = console);
		year && (params.year = year);

		navigate({ pathname: "/", search: `?${createSearchParams(params)}` });
	}

	function clearFilter() {
		setFormFilter(INITIAL_FILTER);
		navigate("/");
	}

	return (
		<section className={css.section_home}>
			<aside>
				<form>
					<Input
						name="search"
						placeholder="Buscar por..."
						text="Buscar"
						type="text"
						value={formFilter.search}
						handleOnChange={handleFilterChange}
					/>

					<Input
						name="year"
						placeholder="20XX"
						text="Ano Lançamento"
						type="number"
						value={formFilter.year}
						handleOnChange={handleFilterChange}
					/>
					<Select
						name="console"
						options={consoles}
						text="Console"
						value={formFilter.console}
						handleOnChange={handleFilterChange}
					/>
					<div>
						{genres.length
							? genres.map((genre, index) => (
									<div key={index}>
										<input type="checkbox" value={genre.id} />
										{genre.description}
									</div>
							  ))
							: ""}
					</div>
					<button type="submit" onClick={submitFilter}>
						Aplicar
					</button>
					<button type="reset" onClick={clearFilter}>
						Limpar Filtro
					</button>
				</form>
			</aside>
			<main>
				<div className={css.game_list}>
					{data.content.length > 0
						? data.content.map((game, index) => {
								return (
									<div className={css.game_card} key={index}>
										<h3>{game.name}</h3>
									</div>
								);
						  })
						: ""}
				</div>
				<Pagination
					page={page}
					totalPages={data.totalPages}
					handleChange={handlePaginationChange}
				/>
			</main>
		</section>
	);
}

export default Home;
