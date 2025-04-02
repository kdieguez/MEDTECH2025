import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './css/Footer.css';

const Footer = () => {
  const [footerItems, setFooterItems] = useState([]);

  useEffect(() => {
    axios.get("http://localhost:7000/headerfooter")
      .then(res => {
        const soloFooter = res.data.filter(item =>
          item.titulo !== "Logo" && item.titulo !== "Titulo"
        );
        console.log("Footer data filtrada:", soloFooter);
        setFooterItems(soloFooter);
      })
      .catch(err => {
        console.error("Error al cargar el footer:", err.message);
      });
  }, []);

  return (
    <footer className="footer">
      {footerItems.map(item => (
        <div key={item.id} className="footer-item">
          <strong>{item.titulo}:</strong> {item.contenido}
        </div>
      ))}
    </footer>
  );
};

export default Footer;
