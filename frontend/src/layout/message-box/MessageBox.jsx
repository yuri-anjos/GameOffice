import { useEffect, useState } from "react";
import css from "./MessageBox.module.css";
import emmitter from "../../util/bus";

function MessageBox() {
	const [isVisible, setIsVisible] = useState(false);
	const [type, setType] = useState("");
	const [message, setMessage] = useState("Minha Mensagem");

	useEffect(() => {
		emmitter.addListener("flash-message", ({ message, type, infinite = false }) => {
			setMessage(message);
			setType(type);
			setIsVisible(true);

			if (!infinite) {
				setTimeout(() => {
					setIsVisible(false);
				}, 5000);
			}
		});
	}, []);

	return (
		isVisible && (
			<div className={`${css.message_box} ${css[type]}`}>
				<button
					type="button"
					onClick={() => {
						setIsVisible(false);
					}}
				>
					&#x2716;
				</button>
				{message}
			</div>
		)
	);
}

export default MessageBox;
