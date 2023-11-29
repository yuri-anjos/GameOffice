import { createContext, useEffect, useState } from "react";
import useAuth from "../hook/useAuth";
import useApi from "../hook/useApi";

const UserContext = createContext();

function UserProvider({ children }) {
	const { register, isAuthenticated, logout, login, checkAuthentication } = useAuth();
	const { getUser } = useApi();

	const [user, setUser] = useState(undefined);

	useEffect(() => {
		if (checkAuthentication()) {
			refreshUser();
		}
	}, []);

	async function refreshUser() {
		getUser().then((data) => {
			console.log(data);
			setUser(data);
		});
	}

	return (
		<UserContext.Provider
			value={{
				login,
				register,
				logout,
				isAuthenticated,
				checkAuthentication,
				user,
				refreshUser,
			}}
		>
			{children}
		</UserContext.Provider>
	);
}

export { UserContext, UserProvider };
