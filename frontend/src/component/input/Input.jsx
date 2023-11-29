import css from "./Input.module.css";

function Input({ type, text, name, placeholder, handleOnChange, value, min, max, step }) {
	return (
		<div className={css.form_control}>
			<label htmlFor={name}>{text}:</label>
			<input
				type={type}
				name={name}
				id={name}
				placeholder={placeholder}
				value={value || ""}
				onChange={handleOnChange}
				min={min}
				max={max}
				step={step}
			/>
		</div>
	);
}

export default Input;
