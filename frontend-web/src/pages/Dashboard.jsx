import { useEffect, useState } from 'react';
import { getDashboard } from '../api/bffClient';
import { useToast } from '../hooks/useToast';

export default function Dashboard() {
  const [data, setData] = useState(null);
  const { showToast, Toast } = useToast();

  useEffect(() => {
    getDashboard()
      .then((res) => setData(res.data))
      .catch(() => showToast('Error al cargar dashboard desde BFF', 'error'));
  }, [showToast]);

  return (
    <div className="container">
      <Toast />
      <h1>Dashboard logístico</h1>
      {!data ? (
        <p>Cargando...</p>
      ) : (
        <div className="grid-dashboard">
          <div className="card">
            <h3>Último pedido</h3>
            <pre style={{ whiteSpace: 'pre-wrap', fontSize: '0.85rem' }}>
              {Object.keys(data.pedido || {}).length
                ? JSON.stringify(data.pedido, null, 2)
                : 'Sin datos'}
            </pre>
          </div>
          <div className="card">
            <h3>Productos ({data.productos?.length || 0})</h3>
            <ul>
              {(data.productos || []).slice(0, 5).map((p) => (
                <li key={p.id}>{p.nombre} — stock: {p.stock}</li>
              ))}
            </ul>
          </div>
          <div className="card">
            <h3>Último envío</h3>
            <pre style={{ whiteSpace: 'pre-wrap', fontSize: '0.85rem' }}>
              {Object.keys(data.envio || {}).length
                ? JSON.stringify(data.envio, null, 2)
                : 'Sin datos'}
            </pre>
          </div>
        </div>
      )}
    </div>
  );
}
