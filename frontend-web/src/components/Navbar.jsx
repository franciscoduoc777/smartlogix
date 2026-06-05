import { NavLink } from 'react-router-dom';

export default function Navbar() {
  return (
    <nav style={{
      background: '#1a1a2e',
      color: '#fff',
      padding: '1rem 1.5rem',
      display: 'flex',
      gap: '1.5rem',
      alignItems: 'center',
    }}>
      <strong style={{ fontSize: '1.2rem' }}>SMARTLOGIX</strong>
      <NavLink to="/" style={linkStyle} end>Dashboard</NavLink>
      <NavLink to="/inventario" style={linkStyle}>Inventario</NavLink>
      <NavLink to="/pedidos" style={linkStyle}>Pedidos</NavLink>
      <NavLink to="/envios" style={linkStyle}>Envíos</NavLink>
    </nav>
  );
}

const linkStyle = ({ isActive }) => ({
  color: isActive ? '#74c0fc' : '#dee2e6',
  textDecoration: 'none',
  fontWeight: isActive ? 700 : 400,
});
