import { Route, Routes } from 'react-router-dom';
import Navbar from './components/Navbar';
import Dashboard from './pages/Dashboard';
import Envios from './pages/Envios';
import Inventario from './pages/Inventario';
import Pedidos from './pages/Pedidos';

export default function App() {
  return (
    <>
      <Navbar />
      <Routes>
        <Route path="/" element={<Dashboard />} />
        <Route path="/inventario" element={<Inventario />} />
        <Route path="/pedidos" element={<Pedidos />} />
        <Route path="/envios" element={<Envios />} />
      </Routes>
    </>
  );
}
