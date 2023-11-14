import css from "./Container.module.css";

function Container({ children }) {
	return <main className={css.container}>{children}</main>;
}

export default Container;
