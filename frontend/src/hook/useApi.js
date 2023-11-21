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
		return api.put(`/game/${gameId}`, { params: form }).then(({}) => {
			return true;
		});
	}

	async function createGame(form) {
		return api.post("/game", { params: form }).then(({ data }) => {
			return data?.id;
		});
	}

	async function getRentedGames(userId) {
		return api.get(`/rented-game/user/${userId}`).then(({ data }) => {
			return data;
		});
	}

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
		return api.get("/user/search", { params: { search } }).then(({ data }) => {
			return data;
		});
	}

	async function updateUser(form) {
		return api.put("/user", form).then(({}) => {
			const type = "success";
			const message = "Alteração realizada com sucesso!";
			setFlashMessage(message, type);

			return true;
		});
	}

	return {
		searchGames,
		findGame,
		getImage,
		createGame,
		updateGame,
		getRentedGames,
		getGenres,
		getConsoles,
		getUser,
		findUser,
		searchUsers,
		updateUser,
	};
}

export default useApi;
