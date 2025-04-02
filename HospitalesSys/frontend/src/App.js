import React, {useState} from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

import Registro from './components/registro'; 
import AgendarCita from './components/agendarcita'; 
import Login from './components/login';
import Header from './components/Header';
import Footer from './components/Footer';
import Navbar from './components/Navbar';
import EditarHeaderFooter from './components/EditarHeaderFooter';
import FormularioDoctor from './components/FormularioDoctor';
import DetalleDoctor from './components/DetalleDoctor';
import CatalogoDoctores from './components/CatalogoDoctores';


function App() {
  const [usuarioLogueado, setUsuarioLogueado] = useState(null);

  return (
    <Router>
      <Header usuarioLogueado={usuarioLogueado} setUsuario={setUsuarioLogueado} />
      <Navbar/>
      <main>
        <Routes>
          <Route path="/registro" element={<Registro />} />
          <Route path="/agendarcita" element={<AgendarCita />} />
          <Route path="/login" element={<Login setUsuario={setUsuarioLogueado} />} />
          <Route path="/editarHF" element={<EditarHeaderFooter/>}/>
          <Route path="/formularioDoctor" element={<FormularioDoctor/>}/>
          <Route path="/catalogoDoctores" element={<CatalogoDoctores/>}/>
          <Route path="/doctor/:id" element={<DetalleDoctor/>}/>

        </Routes>
      </main>
      <Footer/>
    </Router>
  );
}

export default App;