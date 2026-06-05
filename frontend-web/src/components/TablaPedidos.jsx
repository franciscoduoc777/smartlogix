export default function TablaPedidos({ pedidos, onAprobar, onRechazar }) {
  return (
    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>Fecha</th>
          <th>Estado</th>
          <th>Total</th>
          <th>Acciones</th>
        </tr>
      </thead>
      <tbody>
        {pedidos.length === 0 ? (
          <tr><td colSpan="5">Sin pedidos</td></tr>
        ) : (
          pedidos.map((p) => (
            <tr key={p.id}>
              <td>{p.id}</td>
              <td>{p.fecha}</td>
              <td>{p.estado}</td>
              <td>{p.total}</td>
              <td>
                {p.estado !== 'APROBADO' && p.estado !== 'RECHAZADO' && (
                  <>
                    <button className="btn btn-primary" onClick={() => onAprobar(p.id)}>Aprobar</button>{' '}
                    <button className="btn btn-danger" onClick={() => onRechazar(p.id)}>Rechazar</button>
                  </>
                )}
              </td>
            </tr>
          ))
        )}
      </tbody>
    </table>
  );
}
