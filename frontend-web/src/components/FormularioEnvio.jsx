import { useState } from 'react';

export default function FormularioEnvio({ pedidosAprobados, onCrear }) {
  const [form, setForm] = useState({
    pedidoId: '',
    direccion: '',
    transportista: '',
    fechaEstimada: '',
  });
  const [errores, setErrores] = useState({});

  const validar = () => {
    const e = {};
    if (!form.pedidoId) e.pedidoId = 'Pedido obligatorio';
    if (!form.direccion.trim()) e.direccion = 'Dirección obligatoria';
    if (!form.transportista.trim()) e.transportista = 'Transportista obligatorio';
    if (!form.fechaEstimada) e.fechaEstimada = 'Fecha estimada obligatoria';
    setErrores(e);
    return Object.keys(e).length === 0;
  };

  const handleSubmit = (ev) => {
    ev.preventDefault();
    if (!validar()) return;
    onCrear({
      pedidoId: Number(form.pedidoId),
      direccion: form.direccion.trim(),
      transportista: form.transportista.trim(),
      fechaEstimada: form.fechaEstimada,
    });
    setForm({ pedidoId: '', direccion: '', transportista: '', fechaEstimada: '' });
  };

  return (
    <form onSubmit={handleSubmit} className="card">
      <h3>Nuevo envío</h3>
      <div className="form-group">
        <label>Pedido aprobado *</label>
        <select
          value={form.pedidoId}
          onChange={(e) => setForm({ ...form, pedidoId: e.target.value })}
        >
          <option value="">-- Seleccionar --</option>
          {pedidosAprobados.map((p) => (
            <option key={p.id} value={p.id}>Pedido #{p.id}</option>
          ))}
        </select>
        {errores.pedidoId && <span className="error-text">{errores.pedidoId}</span>}
      </div>
      <div className="form-group">
        <label>Dirección *</label>
        <input value={form.direccion} onChange={(e) => setForm({ ...form, direccion: e.target.value })} />
        {errores.direccion && <span className="error-text">{errores.direccion}</span>}
      </div>
      <div className="form-group">
        <label>Transportista *</label>
        <input value={form.transportista} onChange={(e) => setForm({ ...form, transportista: e.target.value })} />
        {errores.transportista && <span className="error-text">{errores.transportista}</span>}
      </div>
      <div className="form-group">
        <label>Fecha estimada *</label>
        <input type="date" value={form.fechaEstimada} onChange={(e) => setForm({ ...form, fechaEstimada: e.target.value })} />
        {errores.fechaEstimada && <span className="error-text">{errores.fechaEstimada}</span>}
      </div>
      <button type="submit" className="btn btn-primary">Crear envío</button>
    </form>
  );
}
