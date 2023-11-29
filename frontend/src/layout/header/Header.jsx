import { useContext } from "react";
import css from "./Header.module.css";
import { Link } from "react-router-dom";
import { UserContext } from "../../context/UserContext";
import { InputUserSearch } from "../../component";

function Header() {
	const { isAuthenticated, logout, user } = useContext(UserContext);

	return (
		<header className={css.header}>
			<h1>LOGO</h1>
			{isAuthenticated && user?.role === "ADMIN" && <InputUserSearch />}
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
