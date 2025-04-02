import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const RutaProtegida = ({ children }) => {
  const navigate = useNavigate();
  const [verificado, setVerificado] = useState(false);

  useEffect(() => {
    const verificarPerfilDoctor = async () => {
      const token = localStorage.getItem("token");
      const usuario = JSON.parse(localStorage.getItem("usuario"));

      if (!token || !usuario) {
        navigate("/login");
        return;
      }

      if (usuario.rol === "2" && usuario.cargo === 1) {
        try {
          const res = await axios.get(`http://localhost:7000/doctores/verificar-perfil/${usuario.id}`);
          if (!res.data) {
            navigate("/formularioDoctor");
            return;
          }
        } catch (err) {
          console.error("Error al verificar perfil:", err);
          navigate("/login");
          return;
        }
      }

      setVerificado(true);
    };

    verificarPerfilDoctor();
  }, [navigate]);

  if (!verificado) return null;
  return children;
};

export default RutaProtegida;
