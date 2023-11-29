import { useNavigate } from "react-router-dom";
import api from "../../util/api";
import useFlashMessage from "../../hook/useFlashMessage";
import { useContext, useEffect, useRef } from "react";
import { UserContext } from "../../context/UserContext";

function AxiosInterceptor() {
	const navigate = useNavigate();
	const { setFlashMessage } = useFlashMessage();
	const { checkAuthentication, logout } = useContext(UserContext);

	const interceptorId = useRef(null);

	useEffect(() => {
		interceptorId.current = api.interceptors.response.use(undefined, (error) => {
			const checkedAuth = checkAuthentication();
			if (error.response?.status) {
				if (error.response.status === 401 && !checkedAuth) {
					const msg = error.response.data?.message || "Sessão inválida!";
					const type = "error";
					setFlashMessage(msg, type);
					logout();
					navigate("/login");
					return;
				}

				if (error.response.status === 401 && checkedAuth) {
					const msg = error.response.data?.message || "Aceesso negado!";
					const type = "error";
					setFlashMessage(msg, type);
					logout();
					navigate(-1);
					return;
				}

				if (error.response.status >= 400 && error.response.status !== 401) {
					const msg = error.response.data?.message || "Erro inesperado!";
					const type = "error";
					setFlashMessage(msg, type);
					return;
				}
			}

			return Promise.reject(error);
		});

		return () => {
			api.interceptors.response.eject(interceptorId.current);
		};
	}, []);

	return null;
}

export default AxiosInterceptor;
