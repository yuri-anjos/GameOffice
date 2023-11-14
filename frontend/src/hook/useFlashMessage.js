import emmitter from "../util/bus";

export default function useFlashMessage() {
	function setFlashMessage(message, type) {
		emmitter.emit("flash-message", {
			message,
			type,
		});
	}

	return { setFlashMessage };
}
