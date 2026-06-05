export default function TablaProductos({ productos, onEditar, onEliminar }) {
  return (
    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>Código</th>
          <th>Nombre</th>
          <th>Stock</th>
          <th>Acciones</th>
        </tr>
      </thead>
      <tbody>
        {productos.length === 0 ? (
          <tr><td colSpan="5">Sin productos</td></tr>
        ) : (
          productos.map((p) => (
            <tr key={p.id}>
              <td>{p.id}</td>
              <td>{p.codigo}</td>
              <td>{p.nombre}</td>
              <td>{p.stock}</td>
              <td>
                <button className="btn btn-secondary" onClick={() => onEditar(p)}>Editar</button>{' '}
                <button className="btn btn-danger" onClick={() => onEliminar(p.id)}>Eliminar</button>
              </td>
            </tr>
          ))
        )}
      </tbody>
    </table>
  );
}
