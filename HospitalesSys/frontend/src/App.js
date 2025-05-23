import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

import Registro from './components/registro'; 
import AgendarCita from './components/AgendarCita'; 
import Login from './components/login';
import Header from './components/Header';
import Footer from './components/Footer';
import Navbar from './components/Navbar';
import EditarHeaderFooter from './components/EditarHeaderFooter';
import FormularioDoctor from './components/FormularioDoctor';
import DetalleDoctor from './components/DetalleDoctor';
import CatalogoDoctores from './components/CatalogoDoctores';
import CatalogoServicios from './components/CatalogoServicios';
import FormularioServicio from './components/FormularioServicio';
import DetalleServicio from './components/DetalleServicios';
import AdminUsuarios from './components/AdminUsuarios';
import VerCitas from './components/VerCitas';
import Home from './components/Home';
import AboutUs from './components/AboutUs';
import ContactUs from './components/ContactUs';
import AdministrarSecciones from './components/AdministrarSecciones';
import FormularioCita from './components/FormularioCita';
import RecetaMedica from './components/RecetaMedica';
import ModeracionCambios from './components/ModeracionCambios';


function App() {
  const [usuarioLogueado, setUsuarioLogueado] = useState(null);

  return (
    <Router>
      <Header usuarioLogueado={usuarioLogueado} setUsuario={setUsuarioLogueado} />
      <Navbar/>
      <main>
        <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/about-us" element={<AboutUs />} />
        <Route path="/contact-us" element={<ContactUs />} />
        <Route path="/admin-pags" element={<AdministrarSecciones />} />
          <Route path="/registro" element={<Registro />} />
          <Route path="/agendarCita" element={<AgendarCita/>}/> 
          <Route path="/login" element={<Login setUsuario={setUsuarioLogueado} />}/>
          <Route path="/editarHF" element={<EditarHeaderFooter/>}/>
          <Route path="/formularioDoctor" element={<FormularioDoctor/>}/>
          <Route path="/catalogoDoctores" element={<CatalogoDoctores/>}/>
          <Route path="/doctor/:id" element={<DetalleDoctor/>}/>
          <Route path="/catalogoServicios" element={<CatalogoServicios/>}/>
          <Route path="/formularioServicios" element={<FormularioServicio/>}/>
          <Route path="/servicio/:id" element={<DetalleServicio/>}/>
          <Route path="/adminUsuarios" element={<AdminUsuarios/>}/>
          <Route path="/servicios/editar/:id" element={<FormularioServicio />}/>
          <Route path="/consultarCitas" element={<VerCitas/>} />
          <Route path="/formularioCita/:idCita" element={<FormularioCita/>} />
          <Route path="/crearReceta/:id" element={<RecetaMedica/>} />
          <Route path="/moderacion" element={<ModeracionCambios />} />
        </Routes>
      </main>
      <Footer/>
    </Router>
  );
}

export default App;
