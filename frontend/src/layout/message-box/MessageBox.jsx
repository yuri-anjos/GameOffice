import { useEffect, useState } from "react";
import css from "./MessageBox.module.css";
import emmitter from "../../util/bus";

function MessageBox() {
	const [isVisible, setIsVisible] = useState(false);
	const [type, setType] = useState("");
	const [message, setMessage] = useState("Minha Mensagem");

	useEffect(() => {
		emmitter.addListener("flash-message", ({ message, type }) => {
			setMessage(message);
			setType(type);
			setIsVisible(true);

			setTimeout(() => {
				setIsVisible(false);
			}, 5000);
		});
	}, []);

	return isVisible && <div className={`${css.message_box} ${css[type]}`}>{message}</div>;
}

export default MessageBox;
