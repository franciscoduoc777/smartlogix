import axios from 'axios';

const bff = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: { 'Content-Type': 'application/json' },
});

export const getDashboard = () => bff.get('/dashboard');
export const getProductos = () => bff.get('/productos');
export const crearProducto = (data) => bff.post('/productos', data);
export const actualizarProducto = (id, data) => bff.put(`/productos/${id}`, data);
export const eliminarProducto = (id) => bff.delete(`/productos/${id}`);
export const getPedidos = () => bff.get('/pedidos');
export const crearPedido = (data) => bff.post('/pedidos', data);
export const aprobarPedido = (id) => bff.put(`/pedidos/${id}/aprobar`);
export const rechazarPedido = (id) => bff.put(`/pedidos/${id}/rechazar`);
export const getEnvios = () => bff.get('/envios');
export const crearEnvio = (data) => bff.post('/envios', data);
export const actualizarEstadoEnvio = (id, estado) =>
  bff.put(`/envios/${id}/estado`, null, { params: { estado } });

export default bff;
