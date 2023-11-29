import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import useFlashMessage from "./useFlashMessage";
import api from "../util/api";

const AUTH_STORAGE = "tokenInfo";

const useAuth = () => {
	const [isAuthenticated, setIsAuthenticated] = useState(false);
	const { setFlashMessage } = useFlashMessage();
	const navigate = useNavigate();

	useEffect(() => {
		const checkedAuth = checkAuthentication();
		setIsAuthenticated(checkedAuth);

		if (checkedAuth) {
			const tokenInfo = JSON.parse(localStorage.getItem(AUTH_STORAGE));
			api.defaults.headers.Authorization = `Bearer ${tokenInfo.token}`;
		}
	}, []);

	function checkAuthentication() {
		const tokenInfo = JSON.parse(localStorage.getItem(AUTH_STORAGE));
		const date = new Date();

		const checkedAuth =
			tokenInfo?.token && new Date(tokenInfo.expiresAtDateTime) > date ? true : false;
		// const checkedAuth = tokenInfo?.token && new Date(tokenInfo.expiresAtInstant) > date;

		return checkedAuth;
	}

	function authUser(tokenInfo) {
		localStorage.setItem(AUTH_STORAGE, JSON.stringify(tokenInfo));
		api.defaults.headers.Authorization = `Bearer ${tokenInfo.token}`;
		navigate("/");
		setIsAuthenticated(true);
	}

	async function register(user) {
		api.post("/auth/register", user).then(({ data }) => {
			const type = "success";
			const message = "Cadastro realizado com sucesso!";
			setFlashMessage(message, type);
			authUser(data);
		});
	}

	async function login(login) {
		api.post("/auth/login", login).then(({ data }) => {
			authUser(data);
		});
	}

	function logout() {
		localStorage.removeItem(AUTH_STORAGE);
		api.defaults.headers.Authorization = undefined;
		setIsAuthenticated(false);
		navigate("/");
	}

	return { register, isAuthenticated, checkAuthentication, login, logout };
};

export default useAuth;
