import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

import Registro from './components/registro'; 
import AgendarCita from './components/agendarcita'; 

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/registro" element={<Registro />} />
        <Route path="/agendarcita" element={<AgendarCita />} />
      </Routes>
    </Router>
  );
}

export default App;