import { useState } from 'react';

export default function FormularioPedido({ productos, onCrear }) {
  const [productoId, setProductoId] = useState('');
  const [cantidad, setCantidad] = useState('');
  const [errores, setErrores] = useState({});

  const validar = () => {
    const e = {};
    if (!productoId) e.productoId = 'Seleccione un producto';
    if (!cantidad || Number(cantidad) <= 0) e.cantidad = 'Cantidad debe ser mayor a cero';
    setErrores(e);
    return Object.keys(e).length === 0;
  };

  const handleSubmit = (ev) => {
    ev.preventDefault();
    if (!validar()) return;
    onCrear({
      detalles: [{ productoId: Number(productoId), cantidad: Number(cantidad) }],
    });
    setProductoId('');
    setCantidad('');
  };

  return (
    <form onSubmit={handleSubmit} className="card">
      <h3>Nuevo pedido</h3>
      <div className="form-group">
        <label>Producto *</label>
        <select value={productoId} onChange={(e) => setProductoId(e.target.value)}>
          <option value="">-- Seleccionar --</option>
          {productos.map((p) => (
            <option key={p.id} value={p.id}>{p.nombre} (stock: {p.stock})</option>
          ))}
        </select>
        {errores.productoId && <span className="error-text">{errores.productoId}</span>}
      </div>
      <div className="form-group">
        <label>Cantidad *</label>
        <input type="number" value={cantidad} onChange={(e) => setCantidad(e.target.value)} />
        {errores.cantidad && <span className="error-text">{errores.cantidad}</span>}
      </div>
      <button type="submit" className="btn btn-primary">Crear pedido</button>
    </form>
  );
}
