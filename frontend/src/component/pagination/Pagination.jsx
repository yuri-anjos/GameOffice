import css from "./Pagination.module.css";

function Pagination({ page, totalPages, handleChange }) {
	return (
		<nav className={css.pagination_nav}>
			<button disabled={page === 1} onClick={() => handleChange(1)}>
				&#60;&#60;
			</button>
			{page > 1 && <button onClick={() => handleChange(page - 1)}>{page - 1}</button>}
			<button disabled>{page}</button>
			{page < totalPages && (
				<button onClick={() => handleChange(page + 1)}>{page + 1}</button>
			)}
			<button disabled={page >= totalPages} onClick={() => handleChange(totalPages)}>
				&#62;&#62;
			</button>
		</nav>
	);
}

export default Pagination;
