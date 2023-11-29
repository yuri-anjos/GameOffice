import { useEffect, useRef, useState } from "react";
import css from "./MultipleSelect.module.css";

function MultipleSelect({ text, options, handleOnChange, value, limit }) {
	const [visibleOptions, setVisibleOptions] = useState(false);

	const refMultipleSelect = useRef(null);

	useEffect(() => {
		function handleClickOutside(e) {
			if (!refMultipleSelect.current?.contains(e.target)) {
				setVisibleOptions(false);
			}
		}

		document.addEventListener("click", handleClickOutside, true);
	}, []);

	function onChange(element) {
		const hasElement = value.filter((i) => i.id === element.id);

		if (hasElement.length) {
			handleOnChange(value.filter((i) => i.id !== element.id));
		} else {
			handleOnChange([...value, element]);
		}
	}

	return (
		<div className={css.form_control} ref={refMultipleSelect}>
			<label>{text}:</label>
			<button
				className={`${css.multiple_select_input} ${
					visibleOptions && css.options_container_open
				}`}
				type="button"
				onClick={() => {
					setVisibleOptions(!visibleOptions);
				}}
			>
				{value.length
					? options
							.filter((op) => value.map((i) => i.id).includes(op.id))
							.map((i) => i.description)
							.join(", ")
					: "Selecione uma Opção:"}
			</button>
			<div className={css.options_container} hidden={!visibleOptions}>
				{options.map((i) => {
					return (
						<label
							key={`${i.id}-${i.description}`}
							htmlFor={`${i.id}-${i.description}`}
						>
							<input
								type="checkbox"
								name={`${i.id}-${i.description}`}
								id={`${i.id}-${i.description}`}
								onChange={() => onChange(i)}
								checked={value.filter((element) => i.id === element.id).length}
								disabled={
									limit &&
									value.length >= limit &&
									!value.find((element) => i.id === element.id)
								}
							/>
							<span>{i.description}</span>
						</label>
					);
				})}
			</div>
		</div>
	);
}

export default MultipleSelect;
