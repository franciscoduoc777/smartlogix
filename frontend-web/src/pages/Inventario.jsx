import { useCallback, useEffect, useState } from 'react';
import {
  actualizarProducto,
  crearProducto,
  eliminarProducto,
  getProductos,
} from '../api/bffClient';
import FormularioProducto from '../components/FormularioProducto';
import TablaProductos from '../components/TablaProductos';
import { useToast } from '../hooks/useToast';

export default function Inventario() {
  const [productos, setProductos] = useState([]);
  const [editar, setEditar] = useState(null);
  const { showToast, Toast } = useToast();

  const cargar = useCallback(() => {
    getProductos()
      .then((res) => setProductos(res.data || []))
      .catch((err) => showToast(err.response?.data?.message || 'Error al cargar productos', 'error'));
  }, [showToast]);

  useEffect(() => { cargar(); }, [cargar]);

  const guardar = async (data) => {
    try {
      if (editar) {
        await actualizarProducto(editar.id, data);
        showToast('Producto actualizado');
      } else {
        await crearProducto(data);
        showToast('Producto creado');
      }
      setEditar(null);
      cargar();
    } catch (err) {
      showToast(err.response?.data?.message || 'Error al guardar', 'error');
    }
  };

  const eliminar = async (id) => {
    try {
      await eliminarProducto(id);
      showToast('Producto eliminado');
      cargar();
    } catch (err) {
      showToast(err.response?.data?.message || 'Error al eliminar', 'error');
    }
  };

  return (
    <div className="container">
      <Toast />
      <h1>Inventario</h1>
      <FormularioProducto productoEditar={editar} onGuardar={guardar} onCancelar={() => setEditar(null)} />
      <div className="card">
        <TablaProductos productos={productos} onEditar={setEditar} onEliminar={eliminar} />
      </div>
    </div>
  );
}
