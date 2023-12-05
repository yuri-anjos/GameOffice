import { useEffect, useState } from "react";
import { Input, MultipleSelect, Pagination } from "../../component";
import useApi from "../../hook/useApi";
import css from "./Home.module.css";
import {
	Link,
	createSearchParams,
	useNavigate,
	useParams,
	useSearchParams,
} from "react-router-dom";

const INITIAL_FILTER = {
	search: "",
	console: [],
	genres: [],
	year: undefined,
};

function Home() {
	const { searchGames, getImage, getGenres, getConsoles } = useApi();
	const { pageNum } = useParams();
	const [searchParams] = useSearchParams();
	const navigate = useNavigate();

	const [formFilter, setFormFilter] = useState(INITIAL_FILTER);
	const [data, setData] = useState({ content: [], totalPages: 0 });
	const [genres, setGenres] = useState([]);
	const [consoles, setConsoles] = useState([]);

	useEffect(() => {
		async function fetchCombosData() {
			getConsoles().then((data) => setConsoles(data));
			getGenres().then((data) => setGenres(data));
		}

		async function loadFilters() {
			const searchFilters = {
				genres: searchParams.getAll("genres")[0],
				console: searchParams.get("console"),
				year: searchParams.get("year"),
				search: searchParams.get("search"),
			};

			setFormFilter({
				genres: searchFilters.genres
					? searchFilters.genres.split(",").map((i) => {
							return { id: parseInt(i) };
					  })
					: [],
				console: searchFilters.console
					? [{ id: parseInt(searchParams.get("console")) }]
					: [],
				year: searchFilters.year,
				search: searchFilters.search,
			});
		}

		fetchCombosData();
		loadFilters();
		fetchData();
	}, []);

	async function fetchData(page) {
		const pg = page || parseInt(pageNum) || 1;

		const params = {
			genres: searchParams.getAll("genres")[0],
			console: searchParams.get("console"),
			year: searchParams.get("year"),
			search: searchParams.get("search"),
		};

		searchGames({ page: pg, ...params }).then((data) => {
			setData(data);
		});
	}

	// async function loadImage(game) {
	// 	return await getImage(game.id);
	// }

	function submitFilter(e) {
		e.preventDefault();

		const { search, genres, console, year } = formFilter;

		genres.length
			? searchParams.set(
					"genres",
					genres.map((i) => i.id)
			  )
			: searchParams.delete("genres");
		console.length
			? searchParams.set(
					"console",
					console.map((i) => i.id)
			  )
			: searchParams.delete("console");
		search ? searchParams.set("search", search) : searchParams.delete("search");
		year ? searchParams.set("year", year) : searchParams.delete("year");

		navigate(
			{ pathname: "/", search: `?${createSearchParams(searchParams)}` },
			{ replace: true }
		);
		fetchData(1);
	}

	function handleFilterChange(e) {
		setFormFilter((previous) => ({ ...previous, [e.target.name]: e.target.value }));
	}

	function handlePaginationChange(val) {
		navigate({ pathname: `/${val}`, search: `?${createSearchParams(searchParams)}` });
		fetchData(val);
	}

	function clearFilter() {
		setFormFilter(INITIAL_FILTER);
		navigate("/");
		fetchData(1);
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

					<MultipleSelect
						text="Console"
						options={consoles}
						handleOnChange={(val) => {
							setFormFilter({ ...formFilter, console: val });
						}}
						value={formFilter.console || []}
						limit={1}
					/>

					<MultipleSelect
						text="Gêneros"
						options={genres}
						handleOnChange={(val) => {
							setFormFilter({ ...formFilter, genres: val });
						}}
						value={formFilter.genres || []}
					/>

					<button type="submit" onClick={submitFilter}>
						Aplicar
					</button>
					<button type="reset" onClick={clearFilter}>
						Limpar Filtro
					</button>
				</form>
			</aside>
			<main>
				<div>
					<h3>Jogos</h3>
					<Link to="/game/create">
						<button type="button">Cadastrar Jogo</button>
					</Link>
				</div>
				<div className={css.game_list}>
					{data.content.length
						? data.content.map((game, index) => {
								return (
									<div
										onClick={() => navigate(`/game/${game.id}`)}
										className={css.game_card}
										key={index}
									>
										<h3>{game.id}</h3>
										<h3>{game.name}</h3>
										{/* <img
											src={`data:;base64,${loadImage(game)}`}
											alt={`${game.name}-cover`}
										/> */}
									</div>
								);
						  })
						: ""}
				</div>
				<Pagination
					page={parseInt(pageNum) || 1}
					totalPages={data.totalPages}
					handleChange={handlePaginationChange}
				/>
			</main>
		</section>
	);
}

export default Home;
