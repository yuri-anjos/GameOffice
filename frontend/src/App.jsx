import { BrowserRouter, Route, Routes } from "react-router-dom";
import { Container, Footer, Header, MessageBox } from "./layout";
import { Home, Login, Profile, Register } from "./page";
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
							<Route path="/page/:pageParam" element={<Home />} />
							<Route path="/login" element={<Login />} />
							<Route path="/register" element={<Register />} />
							<Route path="/user/profile" element={<Profile />} />
						</Routes>
					</Container>
					<Footer />
				</UserProvider>
			</BrowserRouter>
		</>
	);
}

export default App;
