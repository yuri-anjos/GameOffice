import api from "../util/api";
import useFlashMessage from "./useFlashMessage";

function useApi() {
	const { setFlashMessage } = useFlashMessage();

	//Game
	async function searchGames(searchFilters) {
		return api
			.get("/game/search", {
				params: { ...searchFilters, page: searchFilters.page - 1 },
			})
			.then(({ data }) => {
				return data;
			});
	}

	async function searchGamesCombo(search) {
		return api
			.get("/game/combos", {
				params: { search, page: 0, size: 5 },
			})
			.then(({ data }) => {
				return data;
			});
	}

	async function findGame(gameId) {
		return api.get(`/game/${gameId}`).then(({ data }) => {
			return data;
		});
	}

	async function getImage(gameId) {
		return api
			.get(`/game/${gameId}/image`, {
				responseType: "arraybuffer",
			})
			.then((res) => {
				const base64 = btoa(
					new Uint8Array(res.data).reduce(
						(data, byte) => data + String.fromCharCode(byte),
						""
					)
				);
				return base64;
			});
	}

	async function updateGame(form, gameId) {
		const body = {
			...form,
			genres: form.genres.map((i) => i.id),
			console: form.console[0].id,
		};
		return api.put(`/game/${gameId}`, body).then(() => {
			return;
		});
	}

	async function createGame(form) {
		const body = {
			...form,
			genres: form.genres.map((i) => i.id),
			console: form.console[0].id,
		};
		return api.post("/game", body).then(({ data }) => {
			const type = "success";
			const message = "Cadastro realizado com sucesso!";
			setFlashMessage(message, type);

			return data;
		});
	}

	//Rental Game
	async function getActiveRentalGames(userId) {
		return api.get(`/rental-game/active/user/${userId}`).then(({ data }) => {
			return data;
		});
	}

	async function getRentalGames(userId, page, size) {
		return api
			.get(`/rental-game/user/${userId}`, {
				params: { page: page - 1, size },
			})
			.then(({ data }) => {
				return data;
			});
	}

	async function returnRentalGame(rentalGameId) {
		return api.post(`/rental-game/${rentalGameId}/return`).then(({ data }) => {
			return data;
		});
	}

	async function rentGame(userId, rentalGameId) {
		return api.post(`/rental-game/user/${userId}/rent/${rentalGameId}`).then(({ _ }) => {
			return;
		});
	}

	//User
	async function getUser() {
		return api.get("/user").then(({ data }) => {
			return data;
		});
	}

	async function findUser(userId) {
		return api.get(`/user/${userId}`).then(({ data }) => {
			return data;
		});
	}

	async function searchUsers(search) {
		return api
			.get("/user/combos", { params: { search, page: 0, size: 5 } })
			.then(({ data }) => {
				return data;
			});
	}

	async function updateUser(form) {
		return api.put("/user", form).then(({ _ }) => {
			const type = "success";
			const message = "Alteração realizada com sucesso!";
			setFlashMessage(message, type);

			return true;
		});
	}

	//Combos
	async function getGenres() {
		return api.get("/genre").then(({ data }) => {
			return data;
		});
	}

	async function getConsoles() {
		return api.get("/console").then(({ data }) => {
			return data;
		});
	}

	return {
		searchGames,
		searchGamesCombo,
		findGame,
		getImage,
		createGame,
		updateGame,
		getActiveRentalGames,
		getRentalGames,
		returnRentalGame,
		rentGame,
		getUser,
		findUser,
		searchUsers,
		updateUser,
		getGenres,
		getConsoles,
	};
}

export default useApi;
