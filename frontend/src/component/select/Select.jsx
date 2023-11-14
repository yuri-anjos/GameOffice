import css from "./Select.module.css";

function Select({ text, name, options, handleOnChange, value }) {
	return (
		<div className={css.form_control}>
			<label htmlFor={name}>{text}:</label>
			<select name={name} id={name} onChange={handleOnChange} value={value || ""}>
				<option value="">Selecione uma Opção:</option>
				{options.map((val, index) => (
					<option value={val.id} key={index}>
						{val.description}
					</option>
				))}
			</select>
		</div>
	);
}

export default Select;
