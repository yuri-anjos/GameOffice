import api from "../util/api";
import useFlashMessage from "./useFlashMessage";

function useApi() {
	const { setFlashMessage } = useFlashMessage();

	async function getGames(searchFilters) {
		return api
			.get("/game", {
				params: { ...searchFilters, page: searchFilters.page - 1 },
			})
			.then(({ data }) => {
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

	async function getUser() {
		return api.get("/user").then(({ data }) => {
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

	return { getGames, getGenres, getConsoles, getUser, updateUser };
}

export default useApi;
