import { useEffect, useState } from 'react';
import { getDashboard } from '../api/bffClient';
import { useToast } from '../hooks/useToast';

export default function Dashboard() {
  const [data, setData] = useState(null);
  const { showToast, Toast } = useToast();

  useEffect(() => {
    getDashboard()
      .then((res) => setData(res.data))
      .catch(() =>
        showToast('Error al cargar dashboard desde BFF', 'error')
      );
  }, [showToast]);

  return (
    <div className="container">
      <Toast />

      <h1>Dashboard logístico</h1>

      {!data ? (
        <p>Cargando...</p>
      ) : (
        <div className="grid-dashboard">

          {/* Último Pedido */}
          <div className="card">
            <h3>Último Pedido</h3>

            {data.pedido && Object.keys(data.pedido).length > 0 ? (
              <>
                <p>
                  <strong>ID:</strong> {data.pedido.id}
                </p>

                <p>
                  <strong>Estado:</strong> {data.pedido.estado}
                </p>

                <p>
                  <strong>Total:</strong> $
                  {data.pedido.total?.toLocaleString('es-CL')}
                </p>

                <p>
                  <strong>Fecha:</strong>{' '}
                  {new Date(data.pedido.fecha).toLocaleString('es-CL')}
                </p>

                <p>
                  <strong>Productos:</strong>{' '}
                  {data.pedido.detalles?.length || 0}
                </p>
              </>
            ) : (
              <p>Sin datos disponibles</p>
            )}
          </div>

          {/* Productos */}
          <div className="card">
            <h3>
              Productos ({data.productos?.length || 0})
            </h3>

            {data.productos?.length ? (
              <ul>
                {data.productos.slice(0, 5).map((p) => (
                  <li key={p.id}>
                    <strong>{p.nombre}</strong> — Stock: {p.stock}
                  </li>
                ))}
              </ul>
            ) : (
              <p>No hay productos registrados</p>
            )}
          </div>

          {/* Último Envío */}
          <div className="card">
            <h3>Último Envío</h3>

            {data.envio && Object.keys(data.envio).length > 0 ? (
              <>
                <p>
                  <strong>ID:</strong> {data.envio.id}
                </p>

                <p>
                  <strong>Estado:</strong>{' '}
                  {data.envio.estado || 'No disponible'}
                </p>

                {data.envio.destino && (
                  <p>
                    <strong>Destino:</strong> {data.envio.destino}
                  </p>
                )}

                {data.envio.fecha && (
                  <p>
                    <strong>Fecha:</strong>{' '}
                    {new Date(data.envio.fecha).toLocaleString('es-CL')}
                  </p>
                )}
              </>
            ) : (
              <p>Sin datos disponibles</p>
            )}
          </div>

        </div>
      )}
    </div>
  );
}