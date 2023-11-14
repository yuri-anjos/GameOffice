import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import useFlashMessage from "./useFlashMessage";
import api from "../util/api";

const AUTH_STORAGE = "tokenInfo";

const useAuth = () => {
	const { setFlashMessage } = useFlashMessage();
	const navigate = useNavigate();

	useEffect(() => {
		const checkAuth = isAuthenticated();

		if (checkAuth) {
			const tokenInfo = JSON.parse(localStorage.getItem(AUTH_STORAGE));
			api.defaults.headers.Authorization = `Bearer ${tokenInfo.token}`;
		}
	}, []);

	function isAuthenticated() {
		const tokenInfo = JSON.parse(localStorage.getItem(AUTH_STORAGE));
		const date = new Date();
		// new Date(tokenInfo?.expiresAtInstant) > date;
		// new Date(tokenInfo?.expiresAtDateTime) > date;

		const checkAuth = tokenInfo?.token && new Date(tokenInfo.expiresAtInstant) > date;

		return checkAuth;
	}

	function authUser(tokenInfo) {
		localStorage.setItem(AUTH_STORAGE, JSON.stringify(tokenInfo));
		api.defaults.headers.Authorization = `Bearer ${tokenInfo.token}`;
		navigate("/");
	}

	async function register(user) {
		return api.post("/auth/register", user).then(({ data }) => {
			const type = "success";
			const message = "Cadastro realizado com sucesso!";
			setFlashMessage(message, type);
			authUser(data);
		});
	}

	async function login(login) {
		return api.post("/auth/login", login).then(({ data }) => {
			const type = "success";
			const message = "Login realizado com sucesso!";
			setFlashMessage(message, type);
			authUser(data);
		});
	}

	function logout() {
		const message = "Logout realizado com sucesso!";
		const type = "success";

		localStorage.removeItem(AUTH_STORAGE);
		api.defaults.headers.Authorization = undefined;
		setFlashMessage(message, type);
		navigate("/");
	}

	return { register, isAuthenticated, login, logout };
};

export default useAuth;
