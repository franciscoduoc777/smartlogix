import { useCallback, useEffect, useState } from 'react';
import {
  actualizarEstadoEnvio,
  crearEnvio,
  getEnvios,
  getPedidos,
} from '../api/bffClient';
import FormularioEnvio from '../components/FormularioEnvio';
import TablaEnvios from '../components/TablaEnvios';
import { useToast } from '../hooks/useToast';

export default function Envios() {
  const [envios, setEnvios] = useState([]);
  const [pedidos, setPedidos] = useState([]);
  const { showToast, Toast } = useToast();

  const cargar = useCallback(() => {
    getEnvios().then((res) => setEnvios(res.data || [])).catch(() => showToast('Error envíos', 'error'));
    getPedidos().then((res) => setPedidos(res.data || [])).catch(() => {});
  }, [showToast]);

  useEffect(() => { cargar(); }, [cargar]);

  const pedidosAprobados = pedidos.filter(
    (p) => p.estado === 'APROBADO' || p.estado === 'EN_PREPARACION'
  );

  const crear = async (data) => {
    try {
      await crearEnvio(data);
      showToast('Envío creado');
      cargar();
    } catch (err) {
      showToast(err.response?.data?.message || 'Error al crear envío', 'error');
    }
  };

  const cambiarEstado = async (id, estado) => {
    try {
      await actualizarEstadoEnvio(id, estado);
      showToast('Estado actualizado');
      cargar();
    } catch (err) {
      showToast(err.response?.data?.message || 'Error', 'error');
    }
  };

  return (
    <div className="container">
      <Toast />
      <h1>Envíos</h1>
      <FormularioEnvio pedidosAprobados={pedidosAprobados} onCrear={crear} />
      <div className="card">
        <TablaEnvios envios={envios} onCambiarEstado={cambiarEstado} />
      </div>
    </div>
  );
}
