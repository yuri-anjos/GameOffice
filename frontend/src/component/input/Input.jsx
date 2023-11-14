import css from "./Input.module.css";

function Input({ type, text, name, placeholder, handleOnChange, value, hidden }) {
	return (
		<div className={css.form_control}>
			<label htmlFor={name} hidden={hidden}>
				{text}:
			</label>
			<input
				type={type}
				name={name}
				id={name}
				placeholder={placeholder}
				value={value || ""}
				onChange={handleOnChange}
			/>
		</div>
	);
}

export default Input;
