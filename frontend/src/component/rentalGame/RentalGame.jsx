import css from "./RentalGame.module.css";

function RentalGame({ rentalGame, adminMode = false }) {
	return <div>{rentalGame.game.name}</div>;
}

export default RentalGame;
