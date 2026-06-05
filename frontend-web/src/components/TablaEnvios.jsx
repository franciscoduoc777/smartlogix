export default function TablaEnvios({ envios, onCambiarEstado }) {
  const estados = ['ASIGNADO', 'EN_TRANSITO', 'ENTREGADO', 'INCIDENCIA'];

  return (
    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>Pedido</th>
          <th>Dirección</th>
          <th>Transportista</th>
          <th>Estado</th>
          <th>Acciones</th>
        </tr>
      </thead>
      <tbody>
        {envios.length === 0 ? (
          <tr><td colSpan="6">Sin envíos</td></tr>
        ) : (
          envios.map((e) => (
            <tr key={e.id}>
              <td>{e.id}</td>
              <td>{e.pedidoId}</td>
              <td>{e.direccion}</td>
              <td>{e.transportista}</td>
              <td>{e.estado}</td>
              <td>
                <select
                  defaultValue=""
                  onChange={(ev) => {
                    if (ev.target.value) onCambiarEstado(e.id, ev.target.value);
                  }}
                >
                  <option value="">Cambiar estado</option>
                  {estados.map((s) => (
                    <option key={s} value={s}>{s}</option>
                  ))}
                </select>
              </td>
            </tr>
          ))
        )}
      </tbody>
    </table>
  );
}
