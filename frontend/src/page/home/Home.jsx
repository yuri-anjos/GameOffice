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
	const { searchGames, getImage, getGenres, getConsoles } = useApi();
	const { pageNum } = useParams();
	const [searchParams] = useSearchParams();
	const navigate = useNavigate();

	const [formFilter, setFormFilter] = useState(INITIAL_FILTER);
	const [data, setData] = useState({ content: [], totalPages: 0 });
	const [page, setPage] = useState(1);
	const [genres, setGenres] = useState([]);
	const [consoles, setConsoles] = useState([]);

	useEffect(() => {
		async function fetchData() {
			getConsoles().then((data) => setConsoles(data));
			getGenres().then((data) => setGenres(data));
		}

		fetchData();
	}, []);

	useEffect(() => {
		async function getGames() {
			const pg = parseInt(pageNum) || 1;
			setPage(pg);

			const getGamesFilter = { page: pg, size: 3 };
			for (const entry of searchParams.entries()) {
				const [param, value] = entry;
				getGamesFilter[param] = value;
			}

			searchGames(getGamesFilter).then((data) => {
				setData(data);
				loadImages(data);
			});
		}

		getGames();
	}, [pageNum, searchParams]);

	async function loadImages(result) {
		await result.content.forEach(async (i) => {
			i.image = await getImage(i.id);
			return i;
		});
		console.log(result.content);
		setData(result);
	}

	function handleFilterChange(e) {
		setFormFilter((previous) => ({ ...previous, [e.target.name]: e.target.value }));
	}

	function handlePaginationChange(val) {
		navigate({ pathname: `/${val}`, search: `?${createSearchParams(searchParams)}` });
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
				<div>
					<h3>Jogos</h3>
					<button type="button" onClick={() => navigate("/game/create")}>
						Cadastrar Jogo
					</button>
				</div>
				<div className={css.game_list}>
					{data.content
						? data.content.map((game, index) => {
								return (
									<div
										onClick={() => navigate(`/game/${game.id}`)}
										className={css.game_card}
										key={index}
									>
										<h3>{game.id}</h3>
										<h3>{game.name}</h3>
										{game.image && (
											<img
												src={`data:;base64,${game.image}`}
												alt={`${game.name}-cover`}
											/>
										)}
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
