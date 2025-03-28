import React, {useState} from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

import Registro from './components/registro'; 
import AgendarCita from './components/agendarcita'; 
import Login from './components/login';
import Header from './components/Header';
import Footer from './components/Footer';
import Navbar from './components/Navbar';
import EditarHeaderFooter from './components/EditarHeaderFooter';


function App() {
  const [usuarioLogueado, setUsuarioLogueado] = useState(null);

  return (
    <Router>
      <main>
        <Routes>
          <Route path="/registro" element={<Registro />} />
          <Route path="/agendarcita" element={<AgendarCita />} />
          <Route path="/login" element={<Login setUsuario={setUsuarioLogueado} />} />
          <Route path="/editarHF" element={<EditarHeaderFooter/>}/>
        </Routes>
      </main>
    </Router>
  );
}

export default App;