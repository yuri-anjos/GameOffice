import { useContext } from "react";
import css from "./Header.module.css";
import { Link } from "react-router-dom";
import { UserContext } from "../../context/UserContext";

function Header() {
	const { isAuthenticated, logout } = useContext(UserContext);

	return (
		<header className={css.header}>
			LOGO
			<nav>
				<ul>
					<li>
						<Link to="/">Home</Link>
					</li>
					{isAuthenticated() ? (
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
