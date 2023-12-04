import { useContext } from "react";
import css from "./Header.module.css";
import { Link, useNavigate } from "react-router-dom";
import { UserContext } from "../../context/UserContext";
import { InputSearch } from "../../component";
import useApi from "../../hook/useApi";

function Header() {
	const { isAuthenticated, logout, user } = useContext(UserContext);
	const navigate = useNavigate();
	const { searchUsers } = useApi();

	function handleSelectedUser(option) {
		navigate(`/user/${option.id}`);
	}

	return (
		<header className={css.header}>
			<h1>LOGO</h1>
			{isAuthenticated && user?.role === "ADMIN" && (
				<div className={css.input_search}>
					<InputSearch getData={searchUsers} handleSelected={handleSelectedUser} />
				</div>
			)}
			<nav>
				<ul>
					<li>
						<Link to="/">Home</Link>
					</li>
					{isAuthenticated ? (
						<>
							<li>
								<Link to="/user/profile">Perfil</Link>
							</li>
							<li onClick={logout}>Sair</li>
						</>
					) : (
						<>
							<li>
								<Link to="/login">Entrar</Link>
							</li>
							<li>
								<Link to="/register">Cadastrar</Link>
							</li>
						</>
					)}
					<li></li>
				</ul>
			</nav>
		</header>
	);
}

export default Header;
