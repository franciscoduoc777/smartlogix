import { useEffect, useState } from 'react';

const inicial = { codigo: '', nombre: '', descripcion: '', stock: '' };

export default function FormularioProducto({ productoEditar, onGuardar, onCancelar }) {
  const [form, setForm] = useState(inicial);
  const [errores, setErrores] = useState({});

  useEffect(() => {
    if (productoEditar) {
      setForm({
        codigo: productoEditar.codigo || '',
        nombre: productoEditar.nombre || '',
        descripcion: productoEditar.descripcion || '',
        stock: String(productoEditar.stock ?? ''),
      });
    } else {
      setForm(inicial);
    }
    setErrores({});
  }, [productoEditar]);

  const validar = () => {
    const e = {};
    if (!form.codigo.trim()) e.codigo = 'Código obligatorio';
    if (!form.nombre.trim()) e.nombre = 'Nombre obligatorio';
    if (form.stock === '' || Number(form.stock) < 0) e.stock = 'Stock obligatorio y no negativo';
    setErrores(e);
    return Object.keys(e).length === 0;
  };

  const handleSubmit = (ev) => {
    ev.preventDefault();
    if (!validar()) return;
    onGuardar({
      codigo: form.codigo.trim(),
      nombre: form.nombre.trim(),
      descripcion: form.descripcion,
      stock: Number(form.stock),
    });
  };

  return (
    <form onSubmit={handleSubmit} className="card">
      <h3>{productoEditar ? 'Editar producto' : 'Nuevo producto'}</h3>
      <div className="form-group">
        <label>Código *</label>
        <input value={form.codigo} onChange={(e) => setForm({ ...form, codigo: e.target.value })} />
        {errores.codigo && <span className="error-text">{errores.codigo}</span>}
      </div>
      <div className="form-group">
        <label>Nombre *</label>
        <input value={form.nombre} onChange={(e) => setForm({ ...form, nombre: e.target.value })} />
        {errores.nombre && <span className="error-text">{errores.nombre}</span>}
      </div>
      <div className="form-group">
        <label>Descripción</label>
        <input value={form.descripcion} onChange={(e) => setForm({ ...form, descripcion: e.target.value })} />
      </div>
      <div className="form-group">
        <label>Stock *</label>
        <input type="number" value={form.stock} onChange={(e) => setForm({ ...form, stock: e.target.value })} />
        {errores.stock && <span className="error-text">{errores.stock}</span>}
      </div>
      <button type="submit" className="btn btn-primary">Guardar</button>{' '}
      {productoEditar && (
        <button type="button" className="btn btn-secondary" onClick={onCancelar}>Cancelar</button>
      )}
    </form>
  );
}
