import { useCallback, useEffect, useState } from 'react';
import {
  aprobarPedido,
  crearPedido,
  getPedidos,
  getProductos,
  rechazarPedido,
} from '../api/bffClient';
import FormularioPedido from '../components/FormularioPedido';
import TablaPedidos from '../components/TablaPedidos';
import { useToast } from '../hooks/useToast';

export default function Pedidos() {
  const [pedidos, setPedidos] = useState([]);
  const [productos, setProductos] = useState([]);
  const { showToast, Toast } = useToast();

  const cargar = useCallback(() => {
    getPedidos().then((res) => setPedidos(res.data || [])).catch(() => showToast('Error pedidos', 'error'));
    getProductos().then((res) => setProductos(res.data || [])).catch(() => {});
  }, [showToast]);

  useEffect(() => { cargar(); }, [cargar]);

  const crear = async (data) => {
    try {
      await crearPedido(data);
      showToast('Pedido creado');
      cargar();
    } catch (err) {
      showToast(err.response?.data?.message || 'Error al crear pedido', 'error');
    }
  };

  const aprobar = async (id) => {
    try {
      await aprobarPedido(id);
      showToast('Pedido aprobado');
      cargar();
    } catch (err) {
      showToast(err.response?.data?.message || 'No se pudo aprobar', 'error');
    }
  };

  const rechazar = async (id) => {
    try {
      await rechazarPedido(id);
      showToast('Pedido rechazado');
      cargar();
    } catch (err) {
      showToast(err.response?.data?.message || 'Error', 'error');
    }
  };

  return (
    <div className="container">
      <Toast />
      <h1>Pedidos</h1>
      <FormularioPedido productos={productos} onCrear={crear} />
      <div className="card">
        <TablaPedidos pedidos={pedidos} onAprobar={aprobar} onRechazar={rechazar} />
      </div>
    </div>
  );
}
