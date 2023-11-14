import css from "./Footer.module.css";

function Footer() {
	return (
		<footer className={css.footer}>
			<p>
				<span className="bold">Game Office</span> &copy; 2023
			</p>
		</footer>
	);
}

export default Footer;
