import { BrowserRouter, Route, Routes } from "react-router-dom";
import { Container, Footer, Header, MessageBox } from "./layout";
import { CreateGame, Game, Home, Login, Profile, Register, UpdateGame, User } from "./page";
import { UserProvider } from "./context/UserContext";
import { AxiosInterceptor } from "./component";

function App() {
	return (
		<>
			<BrowserRouter>
				<UserProvider>
					<AxiosInterceptor />
					<Header />
					<MessageBox />
					<Container>
						<Routes>
							<Route path="/" element={<Home />} />
							<Route path="/:pageNum" element={<Home />} />
							<Route path="/login" element={<Login />} />
							<Route path="/register" element={<Register />} />
							<Route path="/user/profile" element={<Profile />} />
							<Route path="/user/:userId" element={<User />} />
							<Route path="/game/:gameId" element={<Game />} />
							<Route path="/game/:gameId/update" element={<UpdateGame />} />
							<Route path="/game/create" element={<CreateGame />} />
						</Routes>
					</Container>
					<Footer />
				</UserProvider>
			</BrowserRouter>
		</>
	);
}

export default App;
